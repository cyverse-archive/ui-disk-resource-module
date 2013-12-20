package org.iplantc.core.uidiskresource.client.presenters.proxy;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Performs tests on the {@link FolderContentsRpcProxy} and its underlying classes.
 *
 * @author jstroot
 */
@RunWith(GxtMockitoTestRunner.class)
public class FolderContentsRpcProxyTest {

    @Mock DiskResourceServiceFacade diskResourceService;
    @Mock SearchServiceFacade searchService;

    @Mock AsyncCallback<PagingLoadResult<DiskResource>> pagingAsyncMock;

    @Captor ArgumentCaptor<AsyncCallback<PagingLoadResult<DiskResource>>> pagingAsyncCaptor;
    @Captor ArgumentCaptor<PagingLoadResult<DiskResource>> pagingLoadResultArgumentCaptor;

    private FolderContentsRpcProxy folderContentsRpcProxy;

    @Before public void setUp() {
        folderContentsRpcProxy = new FolderContentsRpcProxy(diskResourceService, searchService);
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
    }


    /**
     *
     */
    @Test public void testFolderContentsCallback_onSucceess() {

        FolderContentsLoadConfig loadConfigMock = mock(FolderContentsLoadConfig.class);
        Folder mockFolder = mock(Folder.class);
        when(mockFolder.isFilter()).thenReturn(false);

        when(loadConfigMock.getFolder()).thenReturn(mockFolder);

        folderContentsRpcProxy.load(loadConfigMock, pagingAsyncMock);

        ArgumentCaptor<FolderContentsRpcProxy.FolderContentsCallback> callBackCaptor
                = ArgumentCaptor.forClass(FolderContentsRpcProxy.FolderContentsCallback.class);
        verify(diskResourceService).getFolderContents(any(Folder.class), eq(loadConfigMock), callBackCaptor.capture());

        callBackCaptor.getValue().onSuccess(mock(Folder.class));
        verify(mockFolder).setTotalFiltered(anyInt());
        verify(pagingAsyncMock).onSuccess(any(PagingLoadResultBean.class));
    }

    /**
     *
     */
    @Test public void testFolderContentsCallbac_onFailure() {

    }
}
