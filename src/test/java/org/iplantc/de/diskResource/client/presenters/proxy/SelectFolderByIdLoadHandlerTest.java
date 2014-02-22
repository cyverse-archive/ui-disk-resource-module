package org.iplantc.de.diskResource.client.presenters.proxy;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.views.DiskResourceView;

import com.google.gwt.core.client.Scheduler;
import com.google.gwtmockito.GxtMockitoTestRunner;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.LoadEvent;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

/**
 * Tests various lazy-loading scenarios using the SelectFolderByIdLoadHandler.
 * 
 * @author psarando
 * 
 */
@RunWith(GxtMockitoTestRunner.class)
@SuppressWarnings("nls")
public class SelectFolderByIdLoadHandlerTest {

    @Mock DiskResourceView.Presenter presenterMock;

    @Mock IplantAnnouncer announcerMock;

    @Mock DiskResourceView viewMock;

    @Mock TreeStore<Folder> treeStoreMock;

    @Mock Folder folderToSelectMock;

    @Mock LoadEvent<Folder, List<Folder>> eventMock;

    @Mock Scheduler deferredSchedulerMock;

    private SelectFolderByIdLoadHandler loadHandlerUnderTest;

    /**
     * Path to the target folder that should be loaded and selected by the SelectFolderByIdLoadHandler.
     */
    private final String targetFolderPath = "/test/path/target/folder";

    /**
     * Path to the parent folder of the target folder.
     */
    private final String targetFolderParentPath = "/test/path/target";

    /**
     * Path to the parent of the parent folder of the target folder.
     */
    private final String targetFolderParentParentPath = "/test/path";

    /**
     * Path to the root folder, which is also the parent of the parent of the parent folder of the target
     * folder.
     */
    private final String rootPath = "/test";

    @Before public void setUp() {
        when(presenterMock.getView()).thenReturn(viewMock);
        when(viewMock.getTreeStore()).thenReturn(treeStoreMock);
        when(folderToSelectMock.getId()).thenReturn(targetFolderPath);
        when(folderToSelectMock.getPath()).thenReturn(targetFolderPath);
    }

    private Folder initMockFolder(String path) {
        Folder folder = mock(Folder.class);
        when(folder.getId()).thenReturn(path);
        when(folder.getPath()).thenReturn(path);
        return folder;
    }

    /**
     * Tests the scenario where the root folders have already been loaded into the view's TreeStore, but
     * none of the child paths have been loaded yet.
     */
    @Test public void testLoad_OnlyRootsLoaded() {
        Folder rootPathFolderMock = initMockFolder(rootPath);
        Folder targerFolderParentParent = initMockFolder(targetFolderParentParentPath);
        Folder targetFolderParent = initMockFolder(targetFolderParentPath);

        // Start with only the rootPath loaded in the treeStoreMock, but no children loaded under it.
        when(treeStoreMock.getAllItemsCount()).thenReturn(1);

        // The SelectFolderByIdLoadHandler constructor will search as far down the path to the target
        // folder as possible for a folder already loaded in the viewMock.
        when(viewMock.getFolderById(targetFolderPath)).thenReturn(null);
        when(viewMock.getFolderById(targetFolderParentPath)).thenReturn(null);
        when(viewMock.getFolderById(targetFolderParentParentPath)).thenReturn(null);
        when(viewMock.getFolderById(rootPath)).thenReturn(rootPathFolderMock);

        // The handler's constructor should call viewMock#expandFolder on rootPath.
        loadHandlerUnderTest = spy(new SelectFolderByIdLoadHandler(folderToSelectMock, presenterMock,
                announcerMock));
        verifyPresenterInit();
        verify(viewMock).expandFolder(rootPathFolderMock);

        // The next onLoad method should call viewMock#expandFolder on targetFolderParentParentPath.
        ArgumentCaptor<Folder> folderCaptor = ArgumentCaptor.forClass(Folder.class);
        when(eventMock.getLoadConfig()).thenReturn(rootPathFolderMock);
        when(viewMock.getFolderById(targetFolderParentParentPath)).thenReturn(targerFolderParentParent);

        loadHandlerUnderTest.onLoad(eventMock);
        verify(viewMock, times(2)).expandFolder(folderCaptor.capture());
        assertEquals("SelectFolderByIdLoadHandler did not expand expected folder.",
                targerFolderParentParent, folderCaptor.getValue());

        // The next onLoad method should call viewMock#expandFolder on targetFolderParentPath.
        when(eventMock.getLoadConfig()).thenReturn(targerFolderParentParent);
        when(viewMock.getFolderById(targetFolderParentPath)).thenReturn(targetFolderParent);

        loadHandlerUnderTest.onLoad(eventMock);
        verify(viewMock, times(3)).expandFolder(folderCaptor.capture());
        assertEquals("SelectFolderByIdLoadHandler did not expand expected folder.", targetFolderParent,
                folderCaptor.getValue());

        // The last onLoad method should find folderToSelectMock in the viewMock.
        when(viewMock.getFolderById(targetFolderPath)).thenReturn(folderToSelectMock);
        loadHandlerUnderTest.onLoad(eventMock);
        verify(viewMock).setSelectedFolder(folderToSelectMock);
        verify(loadHandlerUnderTest).unmaskView();
        verifyPresenterCleanup();

        verifyNoMoreInteractions(presenterMock);
    }

    /**
     * Tests the scenario where the root folders have already been loaded into the view's TreeStore, none
     * of the child paths have been loaded yet, but the target folder does not exist under its parent
     * folder.
     */
    @Test public void testLoad_OnlyRootsLoaded_TargetDeleted() {
        Folder rootPathFolderMock = initMockFolder(rootPath);
        Folder targerFolderParentParent = initMockFolder(targetFolderParentParentPath);
        Folder targetFolderParent = initMockFolder(targetFolderParentPath);

        // Start with only the rootPath loaded in the treeStoreMock, but no children loaded under it.
        when(treeStoreMock.getAllItemsCount()).thenReturn(1);

        // The SelectFolderByIdLoadHandler constructor will search as far down the path to the target
        // folder as possible for a folder already loaded in the viewMock.
        when(viewMock.getFolderById(targetFolderPath)).thenReturn(null);
        when(viewMock.getFolderById(targetFolderParentPath)).thenReturn(null);
        when(viewMock.getFolderById(targetFolderParentParentPath)).thenReturn(null);
        when(viewMock.getFolderById(rootPath)).thenReturn(rootPathFolderMock);

        loadHandlerUnderTest = spy(new SelectFolderByIdLoadHandler(folderToSelectMock, presenterMock,
                announcerMock));
        verifyPresenterInit();
        verify(viewMock).expandFolder(rootPathFolderMock);

        // The next onLoad method should call viewMock#expandFolder on targetFolderParentParentPath.
        ArgumentCaptor<Folder> folderCaptor = ArgumentCaptor.forClass(Folder.class);
        when(eventMock.getLoadConfig()).thenReturn(rootPathFolderMock);
        when(viewMock.getFolderById(targetFolderParentParentPath)).thenReturn(targerFolderParentParent);

        loadHandlerUnderTest.onLoad(eventMock);
        verify(viewMock, times(2)).expandFolder(folderCaptor.capture());
        assertEquals("SelectFolderByIdLoadHandler did not expand expected folder.",
                targerFolderParentParent, folderCaptor.getValue());

        // The next onLoad method should call viewMock#expandFolder on targetFolderParentPath.
        when(eventMock.getLoadConfig()).thenReturn(targerFolderParentParent);
        when(viewMock.getFolderById(targetFolderParentPath)).thenReturn(targetFolderParent);

        loadHandlerUnderTest.onLoad(eventMock);
        verify(viewMock, times(3)).expandFolder(folderCaptor.capture());
        assertEquals("SelectFolderByIdLoadHandler did not expand expected folder.", targetFolderParent,
                folderCaptor.getValue());

        // The next onLoad method should expect the target folder to be loaded in the view.
        when(eventMock.getLoadConfig()).thenReturn(targetFolderParent);
        when(viewMock.getFolderById(targetFolderPath)).thenReturn(null);
        loadHandlerUnderTest.onLoad(eventMock);

        // Since the targetFolderParentPath was loaded but targetFolderPath was not found in the
        // viewMock, the handler should select the target folder's parent (folderMock) and display an
        // error message.
        verify(viewMock).setSelectedFolder(targetFolderParent);
        verify(announcerMock).schedule(any(ErrorAnnouncementConfig.class));
        verify(loadHandlerUnderTest).unmaskView();
        verifyPresenterCleanup();
        verifyNoMoreInteractions(presenterMock);
    }

    /**
     * Tests the scenario where the target folder, and all folders along the path to the target folder,
     * have already been loaded from the service (i.e. they are already cached by the service facade),
     * but they have not yet been loaded into the view's TreeStore.
     */
    @Test public void testLoad_TargetCached() {
        // Start with no roots loaded in the treeStoreMock. This causes the handler to wait until the
        // first onLoad callback, which means that the roots have just been loaded into the viewMock.
        when(treeStoreMock.getAllItemsCount()).thenReturn(0);
        loadHandlerUnderTest = new SelectFolderByIdLoadHandler(folderToSelectMock, presenterMock, announcerMock);
        verifyPresenterInit();

        when(viewMock.getFolderById(targetFolderPath)).thenReturn(folderToSelectMock);
        loadHandlerUnderTest.onLoad(eventMock);

        verify(presenterMock).getSelectedFolder();
        // The onLoad method should have found folderToSelectMock in the viewMock.
        verify(viewMock).setSelectedFolder(folderToSelectMock);
        verifyPresenterCleanup();
        verifyNoMoreInteractions(presenterMock);
        verifyZeroInteractions(eventMock);
    }

    /**
     * Tests the scenario where the target folder has not been loaded yet (it was created by another
     * service), and its parent has already been loaded from the service but not yet loaded into the
     * view's TreeStore (i.e. it's been cached by the service facade).
     */
    @Test public void testLoad_ParentCached_TargetNew() {
        Folder targetFolderParent = initMockFolder(targetFolderParentPath);

        // Start with the target's parent and its children already loaded in the treeStoreMock, but not
        // the target.
        when(treeStoreMock.getAllItemsCount()).thenReturn(1);
        when(viewMock.getFolderById(targetFolderPath)).thenReturn(null);
        when(viewMock.getFolderById(targetFolderParentPath)).thenReturn(targetFolderParent);
        when(viewMock.isLoaded(targetFolderParent)).thenReturn(true);

        // Since deferred commands can't be tested, the refreshFolder method will be overridden to ensure
        // the DiskResourceView.Presenter#doRefresh method is called.
        loadHandlerUnderTest = new SelectFolderByIdLoadHandler(folderToSelectMock, presenterMock, announcerMock) {
            @Override
            void refreshFolder(final Folder folder) {
                presenterMock.doRefresh(folder);
            }
        };
        verifyPresenterInit();

        // The handler's constructor should call presenterMock#doRefresh on targetFolderParentPath.
        ArgumentCaptor<Folder> folderCaptor = ArgumentCaptor.forClass(Folder.class);
        verify(presenterMock).doRefresh(folderCaptor.capture());
        assertEquals("SelectFolderByIdLoadHandler did not refresh expected folder.", targetFolderParent,
                folderCaptor.getValue());

        when(eventMock.getLoadConfig()).thenReturn(targetFolderParent);
        when(viewMock.getFolderById(targetFolderPath)).thenReturn(folderToSelectMock);
        loadHandlerUnderTest.onLoad(eventMock);

        // The onLoad method should have found folderToSelectMock in the viewMock.
        verify(viewMock).setSelectedFolder(folderToSelectMock);
        verifyPresenterCleanup();
        verifyNoMoreInteractions(presenterMock);
    }

    /**
     * Tests the scenario where the target folder no longer exists, but all other folders along the path
     * to the target folder have already been loaded from the service (i.e. they are already cached by
     * the service facade), but they have not yet been loaded into the view's TreeStore.
     */
    @Test public void testLoad_ParentCached_TargetDeleted() {
        Folder targetFolderParent = initMockFolder(targetFolderParentPath);

        // Start with the target's parent and its children already loaded in the treeStoreMock, but not
        // the target.
        when(treeStoreMock.getAllItemsCount()).thenReturn(1);
        when(viewMock.getFolderById(targetFolderPath)).thenReturn(null);
        when(viewMock.getFolderById(targetFolderParentPath)).thenReturn(targetFolderParent);
        when(viewMock.isLoaded(targetFolderParent)).thenReturn(true);

        // Since deferred commands can't be tested, the refreshFolder method will be overridden to ensure
        // the DiskResourceView.Presenter#doRefresh method is called.
        loadHandlerUnderTest = new SelectFolderByIdLoadHandler(folderToSelectMock, presenterMock, announcerMock) {
            @Override
            void refreshFolder(final Folder folder) {
                presenterMock.doRefresh(folder);
            }
        };
        verifyPresenterInit();
        // The handler's constructor should call presenterMock#doRefresh on targetFolderParentPath.
        ArgumentCaptor<Folder> folderCaptor = ArgumentCaptor.forClass(Folder.class);
        verify(presenterMock).doRefresh(folderCaptor.capture());
        assertEquals("SelectFolderByIdLoadHandler did not refresh expected folder.", targetFolderParent,
                folderCaptor.getValue());

        when(eventMock.getLoadConfig()).thenReturn(targetFolderParent);
        when(viewMock.getFolderById(targetFolderPath)).thenReturn(null);
        loadHandlerUnderTest.onLoad(eventMock);

        // Since the targetFolderParentPath was reloaded but targetFolderPath was not found in the
        // viewMock, the handler should select the target folder's parent (folderMock) and display an
        // error message.
        verify(viewMock).setSelectedFolder(targetFolderParent);
        verify(announcerMock).schedule(any(ErrorAnnouncementConfig.class));
        verifyPresenterCleanup();
        verifyNoMoreInteractions(presenterMock);
    }

    /**
     * The SelectFolderByIdLoadHandler constructor will mask the presenter and get a reference to its
     * view.
     */
    private void verifyPresenterInit() {
        verify(presenterMock).mask(any(String.class));
        verify(presenterMock).getView();
    }

    /**
     * When the SelectFolderByIdLoadHandler finishes loading and searching for the target folder, it will
     * unregister itself as a handler and unmask the presenter.
     */
    private void verifyPresenterCleanup() {
        verify(presenterMock).unregisterHandler(loadHandlerUnderTest);
        verify(presenterMock).unmask();
    }
}
