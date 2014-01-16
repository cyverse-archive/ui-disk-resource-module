package org.iplantc.core.uidiskresource.client.search.presenter.impl;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;

import com.sencha.gxt.data.shared.TreeStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;
import org.iplantc.core.uidiskresource.client.events.FolderSelectedEvent;
import org.iplantc.core.uidiskresource.client.events.FolderSelectedEvent.FolderSelectedEventHandler;
import org.iplantc.core.uidiskresource.client.events.FolderSelectedEvent.HasFolderSelectedEventHandlers;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.views.DiskResourceSearchField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

/**
 * 
 * @author jstroot
 * 
 */
@RunWith(GxtMockitoTestRunner.class)
public class DataSearchPresenterImplTest {

    @Mock DiskResourceSearchField viewMock;
    @Mock TreeStore<Folder> treeStoreMock;
    @Mock SearchServiceFacade searchService;
    @Mock IplantAnnouncer announcer;

    @Captor ArgumentCaptor<List<DiskResourceQueryTemplate>> drqtListCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<DiskResourceQueryTemplate>>> stringAsyncCbCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<DiskResourceQueryTemplate>>> drqtListAsyncCaptor;
    @Captor ArgumentCaptor<AsyncCallback<Boolean>> booleanAsyncCaptor;

    private DataSearchPresenterImpl dsPresenter;

    @Before public void setUp() {
        dsPresenter = new DataSearchPresenterImpl(searchService, announcer);
        dsPresenter.view = viewMock;
        dsPresenter.treeStore = treeStoreMock;
    }

    /**
     * Verifies that a query template whose id is null will be given a unique id.
     *
     * @see org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter#doSaveDiskResourceQueryTemplate(org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent)
     */
    @Test public void testDoSaveDiskResourceQueryTemplate_Case1() {
        DataSearchPresenterImpl spy = spy(dsPresenter);
        SaveDiskResourceQueryEvent mockEvent = mock(SaveDiskResourceQueryEvent.class);
        DiskResourceQueryTemplate mockTemplate = mock(DiskResourceQueryTemplate.class);
        when(mockEvent.getQueryTemplate()).thenReturn(mockTemplate);

        spy.doSaveDiskResourceQueryTemplate(mockEvent);

        /* Verify that a unique id is set when the template's id is null. The mock will return null by
         * default
         */
        verify(searchService).getUniqueId();
        verify(mockTemplate).setId(any(String.class));

        /* Verify that the service was called to save the template, and only one template was saved */
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), booleanAsyncCaptor.capture());
        assertEquals(1, drqtListCaptor.getValue().size());
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate));
    }

    /**
     * Verifies that a query template whose id is an empty string will be given a unique id.
     *
     * @see org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter#doSaveDiskResourceQueryTemplate(org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent)
     */
    @Test public void testDoSaveDiskResourceQueryTemplate_Case2() {
        DataSearchPresenterImpl spy = spy(dsPresenter);
        SaveDiskResourceQueryEvent mockEvent = mock(SaveDiskResourceQueryEvent.class);
        DiskResourceQueryTemplate mockTemplate = mock(DiskResourceQueryTemplate.class);
        when(mockEvent.getQueryTemplate()).thenReturn(mockTemplate);

        when(mockTemplate.getId()).thenReturn("");
        when(mockEvent.getQueryTemplate()).thenReturn(mockTemplate);

        spy.doSaveDiskResourceQueryTemplate(mockEvent);

        /* Verify that a unique id is set when the template's id is an empty string */
        verify(searchService).getUniqueId();
        verify(mockTemplate).setId(any(String.class));

        /* Verify that the service was called to save the template, and 1 template was saved */
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), booleanAsyncCaptor.capture());
        assertEquals(1, drqtListCaptor.getValue().size());
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate));
    }

    /**
     * Verifies that an existing query template will be replaced when a request to save a template of the same id is
     * received.
     *
     * @see org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter#doSaveDiskResourceQueryTemplate(org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent)
     */
    @Test public void testDoSaveDiskResourceQueryTemplate_Case3() {
        DataSearchPresenterImpl spy = spy(dsPresenter);
        SaveDiskResourceQueryEvent mockEvent = mock(SaveDiskResourceQueryEvent.class);

        /* ================ Save first template =================== */
        DiskResourceQueryTemplate mockTemplate = mock(DiskResourceQueryTemplate.class);
        when(mockTemplate.getId()).thenReturn("firstMock");
        when(mockEvent.getQueryTemplate()).thenReturn(mockTemplate);

        // Call method under test
        spy.doSaveDiskResourceQueryTemplate(mockEvent);

        /* Verify that the template's id is not set when it is not null or empty */
        verify(searchService, never()).getUniqueId();
        verify(mockTemplate, never()).setId(any(String.class));

        /* Verify that the service was called to save the template, and only 1 template was saved */
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), booleanAsyncCaptor.capture());
        assertEquals(1, drqtListCaptor.getValue().size());
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate));
        // Mock expected behavior from service success
        booleanAsyncCaptor.getValue().onSuccess(true);


        /* ================ Save second template =================== */
        DiskResourceQueryTemplate mockTemplate_2 = mock(DiskResourceQueryTemplate.class);
        when(mockTemplate_2.getId()).thenReturn("secondMock");
        when(mockEvent.getQueryTemplate()).thenReturn(mockTemplate_2);

        // Call method under test
        spy.doSaveDiskResourceQueryTemplate(mockEvent);

        /* Verify that the template's id is not set when it is not null or empty */
        verify(searchService, never()).getUniqueId();
        verify(mockTemplate_2, never()).setId(any(String.class));


        /* Verify that the service was called to save the template, and only 2 templates were saved */
        verify(searchService, times(2)).saveQueryTemplates(drqtListCaptor.capture(), booleanAsyncCaptor.capture());
        assertEquals(2, drqtListCaptor.getValue().size());
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate));
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate_2));
        // Mock expected behavior from service success
        booleanAsyncCaptor.getValue().onSuccess(true);


        /* ================ Save third template =================== */
        DiskResourceQueryTemplate mockTemplate_3 = mock(DiskResourceQueryTemplate.class);
        when(mockTemplate_3.getId()).thenReturn("firstMock");
        when(mockEvent.getQueryTemplate()).thenReturn(mockTemplate_3);
        spy.doSaveDiskResourceQueryTemplate(mockEvent);

        /* Verify that the template's id is not set when it is not null or empty */
        verify(searchService, never()).getUniqueId();
        verify(mockTemplate_3, never()).setId(any(String.class));

        /* Verify that the service was called to save the template, and only 2 templates were saved but one of them was
         * replaced.
         */
        verify(searchService, times(3)).saveQueryTemplates(drqtListCaptor.capture(), booleanAsyncCaptor.capture());
        assertEquals(2, drqtListCaptor.getValue().size());
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate_2));
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate_3));
        assertFalse(drqtListCaptor.getValue().contains(mockTemplate));
    }

    /**
     * Verifies that a search of a given query will be requested after it is successfully persisted.
     *
     * @see org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter#doSaveDiskResourceQueryTemplate(org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent)
     */
    @Test public void testDoSaveDiskResourceQueryTemplate_Case4() {
        DataSearchPresenterImpl spy = spy(dsPresenter);
        SaveDiskResourceQueryEvent mockEvent = mock(SaveDiskResourceQueryEvent.class);
        DiskResourceQueryTemplate mockTemplate = mock(DiskResourceQueryTemplate.class);
        when(mockEvent.getQueryTemplate()).thenReturn(mockTemplate);

        spy.doSaveDiskResourceQueryTemplate(mockEvent);
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), booleanAsyncCaptor.capture());

        /* Verify that the query has not been added to the presenter's list */
        assertEquals(0, spy.getQueryTemplates().size());

        /* Verify that a search is requested after a successful persist. */
        booleanAsyncCaptor.getValue().onSuccess(true);
        ArgumentCaptor<SubmitDiskResourceQueryEvent> submitEventCaptor = ArgumentCaptor.forClass(SubmitDiskResourceQueryEvent.class);
        verify(spy).doSubmitDiskResourceQuery(submitEventCaptor.capture());
        assertEquals(submitEventCaptor.getValue().getQueryTemplate(), mockTemplate);

        /* Verify that the query has been added to the presenter's list after successful persist */
        assertEquals(1, spy.getQueryTemplates().size());
    }

    /**
     * Verifies that a search will not be requested after a failure to persist a query.
     *
     * @see org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter#doSaveDiskResourceQueryTemplate(org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent)
     */
    @Test public void testDoSaveDiskResourceQueryTemplate_Case5() {
        DataSearchPresenterImpl spy = spy(dsPresenter);
        SaveDiskResourceQueryEvent mockEvent = mock(SaveDiskResourceQueryEvent.class);
        DiskResourceQueryTemplate mockTemplate = mock(DiskResourceQueryTemplate.class);
        when(mockEvent.getQueryTemplate()).thenReturn(mockTemplate);

        spy.doSaveDiskResourceQueryTemplate(mockEvent);
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), booleanAsyncCaptor.capture());

        /* Verify that the query has not been added to the presenter's list */
        assertEquals(0, spy.getQueryTemplates().size());

        /* Verify that a search is not requested after failure to persist */
        booleanAsyncCaptor.getValue().onFailure(null);
        verify(spy, never()).doSubmitDiskResourceQuery(any(SubmitDiskResourceQueryEvent.class));

        /* Verify that the query has not been added to the presenter's list after failed persist */
        assertEquals(0, spy.getQueryTemplates().size());
    }

    /**
     * Verifies that the item to be submitted will be set as the active query, the given template will be
     * fired in a {@link FolderSelectedEvent}, and the
     * {@link DataSearchPresenterImpl#updateDataNavigationWindow(List, TreeStore)} method will be called.
     * 
     * @see org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter#doSubmitDiskResourceQuery(SubmitDiskResourceQueryEvent)
     */
    @Test public void testDoSubmitDiskResourceQuery_Case1() {
        DataSearchPresenterImpl spy = spy(dsPresenter);
        DiskResourceQueryTemplate mockedTemplate = mock(DiskResourceQueryTemplate.class);
        when(mockedTemplate.getId()).thenReturn("mockedTemplateId");
        SubmitDiskResourceQueryEvent mockEvent = mock(SubmitDiskResourceQueryEvent.class);
        when(mockEvent.getQueryTemplate()).thenReturn(mockedTemplate);

        spy.setCleanCopyQueryTemplates(Collections.<DiskResourceQueryTemplate> emptyList());

        // Call method under test
        spy.doSubmitDiskResourceQuery(mockEvent);

        /* Verify that the updateDataNavigationWindow method has been called */
        verify(spy).updateDataNavigationWindow(any(List.class), eq(treeStoreMock));

        /* Verify that the active query has been set */
        assertEquals(mockedTemplate, spy.getActiveQuery());

        /* Verify that a folder selected event has been fired with the mocked template */
        ArgumentCaptor<FolderSelectedEvent> fseCaptor = ArgumentCaptor.forClass(FolderSelectedEvent.class);
        verify(spy).fireEvent(fseCaptor.capture());
        assertEquals(mockedTemplate, fseCaptor.getValue().getSelectedFolder());
    }

    /**
     * Verifies that the list passed to the updateNavigationWindow method is correct when the given query
     * template is a new query.
     * 
     * @see org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter#doSubmitDiskResourceQuery(SubmitDiskResourceQueryEvent)
     */
    @Test public void testDoSubmitDiskResourceQuery_Case2() {
        DataSearchPresenterImpl spy = spy(dsPresenter);
        DiskResourceQueryTemplate eventMockTemplate = mock(DiskResourceQueryTemplate.class);
        // Return empty string to indicate that the template is new
        when(eventMockTemplate.getId()).thenReturn("");
        SubmitDiskResourceQueryEvent mockEvent = mock(SubmitDiskResourceQueryEvent.class);
        when(mockEvent.getQueryTemplate()).thenReturn(eventMockTemplate);

        // Add an existing mock to the classes template list
        DiskResourceQueryTemplate existingMock = mock(DiskResourceQueryTemplate.class);
        spy.getQueryTemplates().add(existingMock);

        // Set up Folder root tree store items
        List<Folder> toReturn = createTreeStoreRootFolderList();
        when(treeStoreMock.getRootItems()).thenReturn(toReturn);

        // Call method under test
        spy.doSubmitDiskResourceQuery(mockEvent);

        /* Verify that the given list only contains the existing and event mock templates */
        verify(spy).updateDataNavigationWindow(drqtListCaptor.capture(), eq(treeStoreMock));
        assertEquals(2, drqtListCaptor.getValue().size());
        assertTrue(drqtListCaptor.getValue().contains(existingMock));
        assertTrue(drqtListCaptor.getValue().contains(eventMockTemplate));
    }

    /**
     * Verifies the list passed to the updateNavigationWindow method when the given query template is not
     * new, and has been changed.
     * 
     * The list should be equivalent to what was returned by getQueryTemplates() prior to the method
     * call, but with the changed item in place of the non-changed item. getQueryTemplates() should be
     * equal to what is passed to updateNavigationWindow after the method is called.
     * 
     * @see org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter#doSubmitDiskResourceQuery(SubmitDiskResourceQueryEvent)
     */
    @Test public void testDoSubmitDiskResourceQuery_Case3() {
        // Create presenter with overridden method to control test execution.
        dsPresenter = new DataSearchPresenterImpl(searchService, announcer) {
            @Override
            boolean areTemplatesEqual(DiskResourceQueryTemplate lhs, DiskResourceQueryTemplate rhs) {
                if (lhs.getId().equals("qtMockId1") && rhs.getId().equals("qtMockId1")) {
                    return false;
                }
                return true;
            }
        };
        dsPresenter.view = viewMock;
        dsPresenter.treeStore = treeStoreMock;
        DataSearchPresenterImpl spy = spy(dsPresenter);
        DiskResourceQueryTemplate eventMockTemplate = mock(DiskResourceQueryTemplate.class);
        // Return empty string to indicate that the template is new
        when(eventMockTemplate.getId()).thenReturn("qtMockId1");
        SubmitDiskResourceQueryEvent mockEvent = mock(SubmitDiskResourceQueryEvent.class);
        when(mockEvent.getQueryTemplate()).thenReturn(eventMockTemplate);

        // Add an existing mock to the classes template list
        DiskResourceQueryTemplate existingMock1 = mock(DiskResourceQueryTemplate.class);
        DiskResourceQueryTemplate existingMock2 = mock(DiskResourceQueryTemplate.class);
        when(existingMock1.getId()).thenReturn("qtMockId1");
        when(existingMock2.getId()).thenReturn("qtMockId2");
        spy.getQueryTemplates().clear();
        spy.getQueryTemplates().addAll(Lists.<DiskResourceQueryTemplate> newArrayList(existingMock1, existingMock2));

        spy.setCleanCopyQueryTemplates(Lists.<DiskResourceQueryTemplate> newArrayList(existingMock1, existingMock2));

        // Call method under test
        spy.doSubmitDiskResourceQuery(mockEvent);

        /* Verify that the setDirty flag of the changed template has been set */
        verify(eventMockTemplate).setDirty(eq(true));

        /* Verify that the given list only contains the existing and event mock templates */
        verify(spy).updateDataNavigationWindow(drqtListCaptor.capture(), eq(treeStoreMock));
        assertEquals(2, drqtListCaptor.getValue().size());
        assertTrue(drqtListCaptor.getValue().contains(eventMockTemplate));
        assertTrue(drqtListCaptor.getValue().contains(existingMock2));

        /*
         * This method can be called from two places, the view and the presenter's doSave.. method
         * 
         * When it is called from the doSave.. method, the query will have been persisted
         * 
         * When it is called from the view, the user will have clicked the "submit" button.
         * This does not guarantee that the query has been persisted, or that the query is not
         * an altered version of a previously persisted query. This may be a completely new query.
         * 
         * When user clicks save, expectation is for the folder to show up in navigation window if it
         * is not already there.
         * So, when a user clicks "Submit" and the query is new
         * When a user clicks "Submit" and the query is saved, but altered (dirty flag)
         */
    }

    /**
     * Verifies that a template will be added to the tree store if it is not already there.
     * 
     * @see DataSearchPresenterImpl#updateDataNavigationWindow(List, TreeStore)
     */
    @Test public void testUpdateDataNavigationWindow_Case1() {
        DiskResourceQueryTemplate mock1 = mock(DiskResourceQueryTemplate.class);
        DiskResourceQueryTemplate mock2 = mock(DiskResourceQueryTemplate.class);
        DiskResourceQueryTemplate mock3 = mock(DiskResourceQueryTemplate.class);
        when(mock1.getId()).thenReturn("qtMock1Id");
        when(mock2.getId()).thenReturn("qtMock2Id");
        when(mock3.getId()).thenReturn("qtMock3Id");
    
        // Set up Folder root tree store items
        List<Folder> toReturn = createTreeStoreRootFolderList();
        // Add 2 mocks to tree store root items
        toReturn.add(mock1);
        toReturn.add(mock2);
        when(treeStoreMock.getRootItems()).thenReturn(toReturn);
        when(treeStoreMock.findModelWithKey("qtMock1Id")).thenReturn(mock1);
        when(treeStoreMock.findModelWithKey("qtMock2Id")).thenReturn(mock2);

        // Create list to pass which contains all 3 query template mocks
        List<DiskResourceQueryTemplate> queryTemplates = Lists.newArrayList(mock1, mock2, mock3);
        // Call method under test
        dsPresenter.updateDataNavigationWindow(queryTemplates, treeStoreMock);

        /* Verify that nothing is removed from the store */
        verify(treeStoreMock, never()).remove(any(Folder.class));

        /* Verify that no item is updated in the store */
        verify(treeStoreMock, never()).update(any(Folder.class));

        /* Verify that the mock not previously contained in the tree store is added */
        verify(treeStoreMock).add(eq(mock3));
    }

    /**
     * Verifies that an item which is dirty and already in the tree store will be updated.
     * 
     * @see DataSearchPresenterImpl#updateDataNavigationWindow(List, TreeStore)
     */
    @Test public void testUpdateDataNavigationWindow_Case2() {
        DiskResourceQueryTemplate mock1 = mock(DiskResourceQueryTemplate.class);
        DiskResourceQueryTemplate mock2 = mock(DiskResourceQueryTemplate.class);
        when(mock1.getId()).thenReturn("qtMock1Id");
        when(mock1.isDirty()).thenReturn(true);
        when(mock2.getId()).thenReturn("qtMock2Id");

        // Set up Folder root tree store items
        List<Folder> toReturn = createTreeStoreRootFolderList();
        // Add 2 mocks to tree store root items
        toReturn.add(mock1);
        toReturn.add(mock2);
        when(treeStoreMock.getRootItems()).thenReturn(toReturn);
        when(treeStoreMock.findModelWithKey("qtMock1Id")).thenReturn(mock1);
        when(treeStoreMock.findModelWithKey("qtMock2Id")).thenReturn(mock2);

        // Create list to pass which contains all 3 query template mocks
        List<DiskResourceQueryTemplate> queryTemplates = Lists.newArrayList(mock1, mock2);
        // Call method under test
        dsPresenter.updateDataNavigationWindow(queryTemplates, treeStoreMock);

        /* Verify that nothing is removed from the store */
        verify(treeStoreMock, never()).remove(any(Folder.class));

        /* Verify that the tree store is updated with the dirty query template */
        verify(treeStoreMock).update(eq(mock1));

        /* Verify that nothing is added to the store */
        verify(treeStoreMock, never()).add(any(Folder.class));
    }

    /**
     * Verify #searchInit functionality when the call to retrieve saved templates is successful and returns queries.
     *
     * @see org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter#searchInit(org.iplantc.core.uidiskresource.client.views.DiskResourceView)
     */
    @Test public void testSearchInit_Case1() {
        HasFolderSelectedEventHandlers hasHandlersMock = mock(HasFolderSelectedEventHandlers.class);
        FolderSelectedEventHandler folderSelectedEventHandlerMock = mock(FolderSelectedEventHandler.class);
        dsPresenter.searchInit(hasHandlersMock, folderSelectedEventHandlerMock, treeStoreMock, viewMock);

        /* Verify that presenter adds itself as handler to hasHandlersMock */
        verify(hasHandlersMock).addFolderSelectedEventHandler(eq(dsPresenter));

        /* Verify that view is saved */
        assertEquals(viewMock, dsPresenter.view);
        /* Verify that the treeStore is saved */
        assertEquals(treeStoreMock, dsPresenter.treeStore);

        /* Verify that presenter registers itself to SaveDiskResourceQueryEvent and SubmitDiskResourceQueryEvents on
         * the view toolbar. */
        ArgumentCaptor<SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler> saveEventHandlerCaptor
                = ArgumentCaptor.forClass(SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler.class);
        ArgumentCaptor<SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler> submitEventHandlerCaptor
                = ArgumentCaptor.forClass(SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler.class);
        verify(viewMock).addSaveDiskResourceQueryEventHandler(saveEventHandlerCaptor.capture());
        verify(viewMock).addSubmitDiskResourceQueryEventHandler(submitEventHandlerCaptor.capture());
        assertEquals(saveEventHandlerCaptor.getValue(), dsPresenter);
        assertEquals(submitEventHandlerCaptor.getValue(), dsPresenter);

        /* Verify that presenter calls service to retrieve saved query templates */
        verify(searchService).getSavedQueryTemplates(drqtListAsyncCaptor.capture());

        List<Folder> rootItems = createTreeStoreRootFolderList();
        when(treeStoreMock.getRootItems()).thenReturn(rootItems);
        DiskResourceQueryTemplate retrieved1 = mock(DiskResourceQueryTemplate.class);
        DiskResourceQueryTemplate retrieved2 = mock(DiskResourceQueryTemplate.class);
        DiskResourceQueryTemplate retrieved3 = mock(DiskResourceQueryTemplate.class);
        List<DiskResourceQueryTemplate> retrievedTemplates = Lists.newArrayList(retrieved1, retrieved2, retrieved3);
        drqtListAsyncCaptor.getValue().onSuccess(retrievedTemplates);

        /* Verify that this method applies unique ids to the retrieved templates */
        verify(retrieved1).setId(any(String.class));
        verify(retrieved2).setId(any(String.class));
        verify(retrieved3).setId(any(String.class));

        /* Verify that a frozen list was created */
        verify(searchService).createFrozenList(eq(retrievedTemplates));

        /* Verify that the retrieved list is added to the presenters list */
        assertTrue(dsPresenter.getQueryTemplates().containsAll(retrievedTemplates));
        assertEquals(retrievedTemplates.size(), dsPresenter.getQueryTemplates().size());

        ArgumentCaptor<Folder> folderArgumentCaptor = ArgumentCaptor.forClass(Folder.class);
        /* Verify that the tree store is updated, and all retrieved templates are added */
        verify(treeStoreMock, times(retrievedTemplates.size())).add(folderArgumentCaptor.capture());
        assertTrue(retrievedTemplates.containsAll(folderArgumentCaptor.getAllValues()));

    }

    /**
     * Verify #searchInit functionality when the call to retrieve saved templates fails
     *
     * @see org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter#searchInit(org.iplantc.core.uidiskresource.client.views.DiskResourceView)
     */
    @Test public void testSearchInit_Case2() {
        /* Verify size of presenter query template list prior to calling method under test */
        assertTrue(dsPresenter.getQueryTemplates().isEmpty());

        HasFolderSelectedEventHandlers hasHandlersMock = mock(HasFolderSelectedEventHandlers.class);
        FolderSelectedEventHandler folderSelectedEventHandlerMock = mock(FolderSelectedEventHandler.class);
        dsPresenter.searchInit(hasHandlersMock, folderSelectedEventHandlerMock, treeStoreMock, viewMock);

        /* Verify that presenter adds itself as handler to hasHandlersMock */
        verify(hasHandlersMock).addFolderSelectedEventHandler(eq(dsPresenter));

        /* Verify that view is saved */
        assertEquals(viewMock, dsPresenter.view);
        /* Verify that the treeStore is saved */
        assertEquals(treeStoreMock, dsPresenter.treeStore);

        /* Verify that presenter registers itself to SaveDiskResourceQueryEvent and SubmitDiskResourceQueryEvents on
         * the view toolbar. */
        ArgumentCaptor<SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler> saveEventHandlerCaptor
                = ArgumentCaptor.forClass(SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler.class);
        ArgumentCaptor<SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler> submitEventHandlerCaptor
                = ArgumentCaptor.forClass(SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler.class);
        verify(viewMock).addSaveDiskResourceQueryEventHandler(saveEventHandlerCaptor.capture());
        verify(viewMock).addSubmitDiskResourceQueryEventHandler(submitEventHandlerCaptor.capture());
        assertEquals(saveEventHandlerCaptor.getValue(), dsPresenter);
        assertEquals(submitEventHandlerCaptor.getValue(), dsPresenter);

        /* Verify that presenter calls service to retrieve saved query templates */
        verify(searchService).getSavedQueryTemplates(drqtListAsyncCaptor.capture());

        drqtListAsyncCaptor.getValue().onFailure(null);

        /* Verify that nothing has been added to the presenters query template list and there have been no interactions
           with the view's tree store */
        assertTrue(dsPresenter.getQueryTemplates().isEmpty());
        verify(treeStoreMock, never()).add(any(Folder.class));
        verify(treeStoreMock, never()).remove(any(Folder.class));
    }
    
    @Test public void testOnFolderSelected_Case1() {
        
    }
    
    @Test public void testOnFolderSelected_Case2() {
        
    }

    List<Folder> createTreeStoreRootFolderList(){
        // Set up Folder root tree store items
        Folder root1 = mock(Folder.class);
        Folder root2 = mock(Folder.class);
        Folder root3 = mock(Folder.class);
        Folder root4 = mock(Folder.class);
        when(root1.getId()).thenReturn("root1Id");
        when(root2.getId()).thenReturn("root2Id");
        when(root3.getId()).thenReturn("root3Id");
        when(root4.getId()).thenReturn("root4Id");
        List<Folder> toReturn = Lists.newArrayList(root1, root2, root3, root4);
        return toReturn;
    }

}
