package org.iplantc.core.uidiskresource.client.presenters.proxy;

import com.google.common.collect.Lists;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;

import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.iplantc.core.resources.client.messages.IplantDisplayStrings;
import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade.SearchType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

/**
 * Performs tests on the {@link FolderContentsRpcProxy} and its underlying classes.
 *
 * @author jstroot
 */
@RunWith(GxtMockitoTestRunner.class)
public class FolderContentsRpcProxyTest {

    @Mock DiskResourceServiceFacade diskResourceService;
    @Mock
    SearchServiceFacade searchServiceMock;
    @Mock IplantAnnouncer announcer;
    @Mock IplantDisplayStrings displayStringsMock;

    @Mock AsyncCallback<PagingLoadResult<DiskResource>> pagingAsyncMock;

    @Captor ArgumentCaptor<AsyncCallback<PagingLoadResult<DiskResource>>> pagingAsyncCaptor;
    @Captor ArgumentCaptor<PagingLoadResult<DiskResource>> pagingLoadResultArgumentCaptor;

    private FolderContentsRpcProxy folderContentsRpcProxy;

    @Before public void setUp() {
        folderContentsRpcProxy = new FolderContentsRpcProxy(diskResourceService, searchServiceMock, announcer, displayStringsMock);
    }

    /**
     *  Verifies functionality of load method when the given load config contains a {@link Folder}
     *  whose {@link Folder#isFilter()} method returns false.
     */
    @Test public void testLoad_Case1() {
        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        Folder mockFolder = mock(Folder.class);
        when(mockFolder.isFilter()).thenReturn(false);

        when(loadConfigMock.getFolder()).thenReturn(mockFolder);

        SortInfoBean sortInfoBeanMock = mock(SortInfoBean.class);
        when(sortInfoBeanMock.getSortField()).thenReturn("");
        when(sortInfoBeanMock.getSortDir()).thenReturn(SortDir.ASC);
        List<SortInfoBean> sortInfos = Lists.newArrayList();
        when(loadConfigMock.getSortInfo()).thenReturn(sortInfos);

        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);

        ArgumentCaptor<FolderContentsRpcProxy.FolderContentsCallback> callBackCaptor
                = ArgumentCaptor.forClass(FolderContentsRpcProxy.FolderContentsCallback.class);
        verify(diskResourceService).getFolderContents(eq(mockFolder), eq(loadConfigMock), callBackCaptor.capture());
        verify(searchServiceMock, never()).submitSearchFromQueryTemplate(any(DiskResourceQueryTemplate.class), any(FilterPagingLoadConfigBean.class), any(SearchType.class), any(AsyncCallback.class));

        assertEquals(loadConfigMock, callBackCaptor.getValue().getLoadConfig());
        assertEquals(pagingAsyncMock, callBackCaptor.getValue().getCallback());
    }

    /**
     *  Verifies functionality of load method when the given load config contains a {@link Folder}
     *  whose {@link Folder#isFilter()} method returns true.
     */
    @Test public void testLoad_Case2() {
        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        Folder mockFolder = mock(Folder.class);
        when(mockFolder.isFilter()).thenReturn(true);

        when(loadConfigMock.getFolder()).thenReturn(mockFolder);

        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);
        verify(pagingAsyncMock).onSuccess(pagingLoadResultArgumentCaptor.capture());

        assertEquals(0, pagingLoadResultArgumentCaptor.getValue().getTotalLength());
        assertEquals(0, pagingLoadResultArgumentCaptor.getValue().getOffset());
        assertTrue(pagingLoadResultArgumentCaptor.getValue().getData().isEmpty());

        verify(diskResourceService, never()).getFolderContents(any(Folder.class), any(FolderContentsLoadConfig.class), any(AsyncCallback.class));
        verify(searchServiceMock, never()).submitSearchFromQueryTemplate(any(DiskResourceQueryTemplate.class), any(FilterPagingLoadConfigBean.class), any(SearchType.class), any(AsyncCallback.class));
    }
    
    /**
     * Verifies functionality of load method when the given load config contains a {@link DiskResourceQueryTemplate}
     */
    @Test public void testLoad_Case3() {
    	FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        DiskResourceQueryTemplate mockQueryTemplate = mock(DiskResourceQueryTemplate.class);

        when(loadConfigMock.getFolder()).thenReturn(mockQueryTemplate);
        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);
        
  	
        verify(diskResourceService, never()).getFolderContents(any(Folder.class), any(FolderContentsLoadConfig.class), any(AsyncCallback.class));
        ArgumentCaptor<FolderContentsRpcProxy.FolderContentsCallback> callBackCaptor
                = ArgumentCaptor.forClass(FolderContentsRpcProxy.FolderContentsCallback.class);
        verify(searchServiceMock).submitSearchFromQueryTemplate(eq(mockQueryTemplate), eq(loadConfigMock), any(SearchType.class), callBackCaptor.capture());

        assertEquals(loadConfigMock, callBackCaptor.getValue().getLoadConfig());
        assertEquals(pagingAsyncMock, callBackCaptor.getValue().getCallback());
    }
    
	/**
	 * Verifies functionality of the inner callback class onSuccess method when
	 * the result and given callback (the one which is accessed vi
	 * {@link FolderContentsCallback#getCallback()) are not null.
	 */
	@SuppressWarnings("unchecked")
	@Test public void testFolderContentsCallback_onSucceess_Case1() {

        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        Folder mockFolder = mock(Folder.class);
        when(mockFolder.isFilter()).thenReturn(false);

        when(loadConfigMock.getFolder()).thenReturn(mockFolder);

        final HasSafeHtml hasSafeHtmlMock = mock(HasSafeHtml.class);
        folderContentsRpcProxy.init(hasSafeHtmlMock);

        // Call method under test
        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);

        ArgumentCaptor<FolderContentsRpcProxy.FolderContentsCallback> callBackCaptor
                = ArgumentCaptor.forClass(FolderContentsRpcProxy.FolderContentsCallback.class);
        verify(diskResourceService).getFolderContents(any(Folder.class), eq(loadConfigMock), callBackCaptor.capture());

        callBackCaptor.getValue().onSuccess(mock(Folder.class));
        verify(mockFolder).setTotalFiltered(anyInt());
		verify(pagingAsyncMock).onSuccess(any(PagingLoadResultBean.class));
    }

    /**
	 * Verifies functionality of the inner callback class onSuccess method when
	 * the result is null.
     */
    @Test public void testFolderContentsCallback_onSucceess_Case2() {

        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        Folder mockFolder = mock(Folder.class);
        when(mockFolder.isFilter()).thenReturn(false);

        when(loadConfigMock.getFolder()).thenReturn(mockFolder);

        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);

        ArgumentCaptor<FolderContentsRpcProxy.FolderContentsCallback> callBackCaptor
                = ArgumentCaptor.forClass(FolderContentsRpcProxy.FolderContentsCallback.class);
        verify(diskResourceService).getFolderContents(any(Folder.class), eq(loadConfigMock), callBackCaptor.capture());

        callBackCaptor.getValue().onSuccess(null);
        verify(pagingAsyncMock).onFailure(any(Throwable.class));
        verifyZeroInteractions(searchServiceMock);
    }

    /**
     * Verifies functionality of the inner callback class onSuccess when displaystrings is null
     */
    @Test public void testFolderContentsCallback_onSucceess_Case3() {
        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        DiskResourceQueryTemplate mockFolder = mock(DiskResourceQueryTemplate.class);
        when(mockFolder.isFilter()).thenReturn(false);
        String displayString = "test display string";
        when(displayStringsMock.searchAppResultsHeader(anyString(), anyInt())).thenReturn(displayString);

        when(loadConfigMock.getFolder()).thenReturn(mockFolder);

        final HasSafeHtml hasSafeHtmlMock = mock(HasSafeHtml.class);
        folderContentsRpcProxy.init(hasSafeHtmlMock);

        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);

        ArgumentCaptor<FolderContentsRpcProxy.FolderContentsCallback> callBackCaptor = ArgumentCaptor.forClass(FolderContentsRpcProxy.FolderContentsCallback.class);
        verify(searchServiceMock).submitSearchFromQueryTemplate(any(DiskResourceQueryTemplate.class), eq(loadConfigMock), isNull(SearchType.class), callBackCaptor.capture());

        callBackCaptor.getValue().onSuccess(mockFolder);
        verifyZeroInteractions(searchServiceMock);
    }

    /**
	 * Verifies functionality of the inner callback class onFailure method.
	 * 
     */
    @Test public void testFolderContentsCallback_onFailure() {
        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        Folder mockFolder = mock(Folder.class);
        when(mockFolder.isFilter()).thenReturn(false);

        when(loadConfigMock.getFolder()).thenReturn(mockFolder);

        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);

        ArgumentCaptor<FolderContentsRpcProxy.FolderContentsCallback> callBackCaptor
                = ArgumentCaptor.forClass(FolderContentsRpcProxy.FolderContentsCallback.class);
        verify(diskResourceService).getFolderContents(any(Folder.class), eq(loadConfigMock), callBackCaptor.capture());

        callBackCaptor.getValue().onFailure(mock(Throwable.class));
        verify(pagingAsyncMock).onFailure(any(Throwable.class));
    }
}
