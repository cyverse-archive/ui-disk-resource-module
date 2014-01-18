package org.iplantc.core.uidiskresource.client.presenters.proxy;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.core.uicommons.client.info.ErrorAnnouncementConfig;
import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.models.diskresources.RootFolders;
import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;
import org.iplantc.core.uicommons.client.views.IsMaskable;
import org.iplantc.core.uidiskresource.client.presenters.proxy.FolderRpcProxy.GetSavedQueryTemplatesCallback;
import org.iplantc.core.uidiskresource.client.presenters.proxy.FolderRpcProxy.RootFolderCallback;
import org.iplantc.core.uidiskresource.client.presenters.proxy.FolderRpcProxy.SubFoldersCallback;
import org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

@RunWith(GxtMockitoTestRunner.class)
public class FolderRpcProxyTest {

    @Mock FolderRpcProxy proxyUnderTestMock;
    @Mock DiskResourceServiceFacade drServiceMock;
    @Mock SearchServiceFacade searchServiceMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock IsMaskable maskableMock;
    @Mock DataSearchPresenter searchPresenterMock;

    @Mock AsyncCallback<List<Folder>> folderCallbackMock;
    
    @Captor ArgumentCaptor<List<Folder>> folderListCaptor;

    @Before public void setUp() {
        proxyUnderTestMock = new FolderRpcProxy(drServiceMock, searchServiceMock, announcerMock);
        proxyUnderTestMock.init(searchPresenterMock, maskableMock);
    }

    /**
     * Verifies functionality of load(..) method when given folder is null.
     */
    @Test public void testLoad_Case1() {

        proxyUnderTestMock.load(null, folderCallbackMock);

        verify(maskableMock).mask(any(String.class));

        /* Verify that the DiskResourceService getRootFolders(..) method is called */
        ArgumentCaptor<RootFolderCallback> rootFolderCallbackCaptor = ArgumentCaptor.forClass(FolderRpcProxy.RootFolderCallback.class);
        verify(drServiceMock).getRootFolders(rootFolderCallbackCaptor.capture());

        /* Verify that no other methods are called in DiskResourceService */
        verifyNoMoreInteractions(drServiceMock);
        
        /* Verify that nothing was done with initial callback */
        verifyZeroInteractions(folderCallbackMock);

        /* Verify that callback was created with intended properties */
        assertEquals(folderCallbackMock, rootFolderCallbackCaptor.getValue().callback);
        assertEquals(maskableMock, rootFolderCallbackCaptor.getValue().maskable);
    }

    /**
     * Verify functionality of load(..) method when given folder is not null, the folder.isFilter()
     * method returns true, and given callback is null.
     */
    @Test public void testLoad_Case2() {
        Folder parentFolderMock = mock(Folder.class);
        when(parentFolderMock.isFilter()).thenReturn(true);

        // Call method under test
        AsyncCallback<List<Folder>> nullCallback = null;
        proxyUnderTestMock.load(parentFolderMock, nullCallback);

        verify(parentFolderMock).isFilter();

        verifyNoMoreInteractions(parentFolderMock);

        verifyZeroInteractions(maskableMock, drServiceMock);
    }

    /**
     * Verify functionality of load(..) method when given folder is not null, the folder.isFilter()
     * method returns true, and given callback is not null.
     */
    @Test public void testLoad_Case3() {
        Folder parentFolderMock = mock(Folder.class);
        when(parentFolderMock.isFilter()).thenReturn(true);

        proxyUnderTestMock.load(parentFolderMock, folderCallbackMock);

        verify(parentFolderMock).isFilter();

        verify(folderCallbackMock).onSuccess(folderListCaptor.capture());

        /* Verify that an empty list is passed */
        assertTrue(folderListCaptor.getValue().isEmpty());

        verifyNoMoreInteractions(parentFolderMock);

        verifyZeroInteractions(maskableMock, drServiceMock);
    }

    /**
     * Verify functionality of load(..) method when given folder is not null, and the folder.isFilter()
     * method returns false.
     */
    @Test public void testLoad_Case4() {
        Folder parentFolderMock = mock(Folder.class);
        when(parentFolderMock.isFilter()).thenReturn(false);
        final String stubPath = "stubPath";
        when(parentFolderMock.getPath()).thenReturn(stubPath);

        proxyUnderTestMock.load(parentFolderMock, folderCallbackMock);

        verify(parentFolderMock).isFilter();

        ArgumentCaptor<SubFoldersCallback> subFoldersCallbackCaptor = ArgumentCaptor.forClass(SubFoldersCallback.class);
        verify(drServiceMock).getSubFolders(eq(parentFolderMock), subFoldersCallbackCaptor.capture());

        /* Verify that callback was created with intended properties */
        assertEquals(folderCallbackMock, subFoldersCallbackCaptor.getValue().callback);

        verifyNoMoreInteractions(parentFolderMock, drServiceMock);

    }
    
    /**
     * onSuccess callback !null
     */
    @Test public void testRootFolderCallbackOnSuccess_Case1() {
        proxyUnderTestMock.load(null, folderCallbackMock);

        verify(maskableMock).mask(any(String.class));

        /* Verify that the DiskResourceService getRootFolders(..) method is called */
        ArgumentCaptor<RootFolderCallback> rootFolderCallbackCaptor = ArgumentCaptor.forClass(FolderRpcProxy.RootFolderCallback.class);
        verify(drServiceMock).getRootFolders(rootFolderCallbackCaptor.capture());

        final RootFolders rootFoldersMock = mock(RootFolders.class);
        final ArrayList<Folder> newArrayList = Lists.<Folder> newArrayList(mock(Folder.class), mock(Folder.class));
        when(rootFoldersMock.getRoots()).thenReturn(newArrayList);

        rootFolderCallbackCaptor.getValue().onSuccess(rootFoldersMock);

        verify(folderCallbackMock).onSuccess(folderListCaptor.capture());

        assertEquals(newArrayList, folderListCaptor.getValue());

        ArgumentCaptor<GetSavedQueryTemplatesCallback> savedQueriesCaptor = ArgumentCaptor.forClass(GetSavedQueryTemplatesCallback.class);
        verify(searchServiceMock).getSavedQueryTemplates(savedQueriesCaptor.capture());

        verify(maskableMock).unmask();

        assertEquals(searchPresenterMock, savedQueriesCaptor.getValue().searchPresenter2);

        verifyNoMoreInteractions(drServiceMock, searchServiceMock);

        /* Verify savedQueriesCallback onSuccess */
        final List<DiskResourceQueryTemplate> qtList = Lists.newArrayList(mock(DiskResourceQueryTemplate.class), mock(DiskResourceQueryTemplate.class));
        savedQueriesCaptor.getValue().onSuccess(qtList);

        verify(searchPresenterMock).loadSavedQueries(eq(qtList));

        /* Verify savedQueriesCallback onFailure */
        savedQueriesCaptor.getValue().onFailure(null);

        verify(announcerMock).schedule(any(ErrorAnnouncementConfig.class));
    }

    /**
     * onSuccess callback null
     */
    @Test public void testRootFolderCallbackOnSuccess_Case2() {
        
        AsyncCallback<List<Folder>> nullCallback = null;
        proxyUnderTestMock.load(null, nullCallback);
        
        verify(maskableMock).mask(any(String.class));
        
        /* Verify that the DiskResourceService getRootFolders(..) method is called */
        ArgumentCaptor<RootFolderCallback> rootFolderCallbackCaptor = ArgumentCaptor.forClass(FolderRpcProxy.RootFolderCallback.class);
        verify(drServiceMock).getRootFolders(rootFolderCallbackCaptor.capture());
        
        final RootFolders rootFoldersMock = mock(RootFolders.class);
        verifyZeroInteractions(rootFoldersMock);
        
        rootFolderCallbackCaptor.getValue().onSuccess(rootFoldersMock);
        
        ArgumentCaptor<GetSavedQueryTemplatesCallback> savedQueriesCaptor = ArgumentCaptor.forClass(GetSavedQueryTemplatesCallback.class);
        verify(searchServiceMock).getSavedQueryTemplates(savedQueriesCaptor.capture());
        
        verify(maskableMock).unmask();
        
        assertEquals(searchPresenterMock, savedQueriesCaptor.getValue().searchPresenter2);
        
        verifyNoMoreInteractions(drServiceMock, searchServiceMock);
    }

    /**
     * onFailure callback !null
     */
    @Ignore
    @Test public void testRootFolderCallbackOnFailure_Case1() {
        // TODO Complete test case 
    }

    /**
     * onFailure callback null
     */
    @Ignore
    @Test public void testRootFolderCallbackOnFailure_Case2() {
       // TODO Complete test case 
    }
    
    /**
     * callback !null
     */
    @Ignore
    @Test public void testSubFoldersCallbackOnSuccess_Case1() {
       // TODO Complete test case 
    }
    
    /**
     * callback null
     */
    @Ignore
    @Test public void testSubFoldersCallbackOnSuccess_Case2() {
       // TODO Complete test case 
    }

    /**
     * callback !null
     */
    @Ignore
    @Test public void testSubFoldersCallbackOnFailure_Case1() {
       // TODO Complete test case 
    }
    
    /**
     * callback null
     */
    @Ignore
    @Test public void testSubFoldersCallbackOnFailure_Case2() {
       // TODO Complete test case 
    }

}