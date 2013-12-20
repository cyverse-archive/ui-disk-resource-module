package org.iplantc.core.uidiskresource.client.search.presenter.impl;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;
import com.sencha.gxt.data.shared.TreeStore;
import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * TODO Test presenter initialization and fetching of previously persisted templates.
 * 
 * 
 * @author jstroot
 * 
 */
@RunWith(GxtMockitoTestRunner.class)
public class DataSearchPresenterImplTest {

    private DataSearchPresenterImpl dsPresenter;

    @Mock DiskResourceView drView;
    @Mock DiskResourceViewToolbar drToolbar;
    @Mock TreeStore<Folder> viewTreeStore;
    @Mock SearchServiceFacade searchService;
    @Mock IplantAnnouncer announcer;

    @Captor ArgumentCaptor<List<DiskResourceQueryTemplate>> drqtListCaptor;
    @Captor ArgumentCaptor<AsyncCallback<String>> stringAsyncCbCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<DiskResourceQueryTemplate>>> drqtListAsyncCaptor;

    @Before public void setUp() {
        dsPresenter = new DataSearchPresenterImpl(searchService, announcer);
        dsPresenter.view = drView;

        when(drView.getTreeStore()).thenReturn(viewTreeStore);
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
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), stringAsyncCbCaptor.capture());
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
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), stringAsyncCbCaptor.capture());
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
        spy.doSaveDiskResourceQueryTemplate(mockEvent);

        /* Verify that the template's id is not set when it is not null or empty */
        verify(searchService, never()).getUniqueId();
        verify(mockTemplate, never()).setId(any(String.class));

        /* Verify that the service was called to save the template, and only 1 template was saved */
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), stringAsyncCbCaptor.capture());
        assertEquals(1, drqtListCaptor.getValue().size());
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate));
        stringAsyncCbCaptor.getValue().onSuccess("");


        /* ================ Save second template =================== */
        DiskResourceQueryTemplate mockTemplate_2 = mock(DiskResourceQueryTemplate.class);
        when(mockTemplate_2.getId()).thenReturn("secondMock");
        when(mockEvent.getQueryTemplate()).thenReturn(mockTemplate_2);
        spy.doSaveDiskResourceQueryTemplate(mockEvent);


        /* Verify that the template's id is not set when it is not null or empty */
        verify(searchService, never()).getUniqueId();
        verify(mockTemplate_2, never()).setId(any(String.class));


        /* Verify that the service was called to save the template, and only 2 templates were saved */
        verify(searchService, times(2)).saveQueryTemplates(drqtListCaptor.capture(), stringAsyncCbCaptor.capture());
        assertEquals(2, drqtListCaptor.getValue().size());
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate));
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate_2));
        stringAsyncCbCaptor.getValue().onSuccess("");


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
        verify(searchService, times(3)).saveQueryTemplates(drqtListCaptor.capture(), stringAsyncCbCaptor.capture());
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
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), stringAsyncCbCaptor.capture());

        /* Verify that the query has not been added to the presenter's list */
        assertEquals(0, spy.getQueryTemplates().size());

        /* Verify that a search is requested after a successful persist. */
        stringAsyncCbCaptor.getValue().onSuccess("");
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
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), stringAsyncCbCaptor.capture());

        /* Verify that the query has not been added to the presenter's list */
        assertEquals(0, spy.getQueryTemplates().size());

        /* Verify that a search is not requested after failure to persist */
        stringAsyncCbCaptor.getValue().onFailure(null);
        verify(spy, never()).doSubmitDiskResourceQuery(any(SubmitDiskResourceQueryEvent.class));

        /* Verify that the query has not been added to the presenter's list after failed persist */
        assertEquals(0, spy.getQueryTemplates().size());
    }

    /**
     *
     * Verifies that when a search is requested of a given query template, the template will be added to the view's
     * tree store if it is not already there.
     *
     * <h3>Preconditions</h3>
     * <ul>
     *     <li>the view's treestore does not contain the given query template</li>
     * </ul>
     *
     * <h3>Verification tasks</h3>
     * <ol>
     *     <li>the query passed with the {@link SubmitDiskResourceQueryEvent} input parameter is set as the
     *         active query after the search is successful.</li>
     *     <li>the {@link TreeStore} associated with the {@link DataSearchPresenter#getView()} is updated</li>
     * </ol>
     *
     * @see org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter#doSubmitDiskResourceQuery(org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent)
     */
    @Test public void testDoSubmitDiskResourceQuery_Case1() {
        DiskResourceQueryTemplate mockedTemplate = mock(DiskResourceQueryTemplate.class);
        when(mockedTemplate.getId()).thenReturn("mockedTemplateId");
        SubmitDiskResourceQueryEvent mockEvent = mock(SubmitDiskResourceQueryEvent.class);
        when(mockEvent.getQueryTemplate()).thenReturn(mockedTemplate);
        dsPresenter.getQueryTemplates().add(mockedTemplate);

        // Set up Folder root tree store items
        List<Folder> toReturn = createTreeStoreRootFolderList();
        when(viewTreeStore.getRootItems()).thenReturn(toReturn);

        // Save template
        dsPresenter.doSubmitDiskResourceQuery(mockEvent);

        /* Verify that the template is added to the store, but no call to remove ever happens */
        verify(viewTreeStore, never()).remove(any(Folder.class));
        verify(viewTreeStore).add(eq(mockedTemplate));

        /* Verify that a search is submitted with the templated passed with given event */
        verify(searchService).submitSearchFromQueryTemplate(eq(mockedTemplate), stringAsyncCbCaptor.capture());

        /* Verify that the active query is still null after the search was requested, but before the search returns
         * successfully
         */
        assertTrue(dsPresenter.getActiveQuery() == null);

        /* Verify that the active query is set after successful search submission */
        stringAsyncCbCaptor.getValue().onSuccess("");
        assertEquals(dsPresenter.getActiveQuery(), mockedTemplate);
    }

    /**
     * Verifies that when a search is requested of a given query template, the template will be removed and re-added to
     * the view's tree store if it is already there.
     *
     * <h3>Preconditions</h3>
     * <ul>
     *     <li>the view's treestore contains the given query template</li>
     * </ul>
     *
     * <h3>Verification tasks</h3>
     * <ol>
     *     <li>the query passed with the {@link SubmitDiskResourceQueryEvent} input parameter is not set as the
     *         active query after the search fails.</li>
     *     <li>the {@link TreeStore} associated with the {@link DataSearchPresenter#getView()} is updated</li>
     * </ol>
     */
    @Test public void testDoSubmitDiskResourceQuery_Case2() {
        DiskResourceQueryTemplate mockedTemplate = mock(DiskResourceQueryTemplate.class);
        when(mockedTemplate.getId()).thenReturn("mockedTemplateId");
        SubmitDiskResourceQueryEvent mockEvent = mock(SubmitDiskResourceQueryEvent.class);
        when(mockEvent.getQueryTemplate()).thenReturn(mockedTemplate);
        dsPresenter.getQueryTemplates().add(mockedTemplate);

        // Set up Folder root tree store items
        List<Folder> toReturn = createTreeStoreRootFolderList();
        toReturn.add(mockedTemplate);
        when(viewTreeStore.getRootItems()).thenReturn(toReturn);

        InOrder inOrder = inOrder(viewTreeStore);
        // Save template
        dsPresenter.doSubmitDiskResourceQuery(mockEvent);

        /* Verify that the template is removed and re-added to the store */
        inOrder.verify(viewTreeStore).remove(eq(mockedTemplate));
        inOrder.verify(viewTreeStore).add(eq(mockedTemplate));

        /* Verify that a search is submitted with the templated passed with given event */
        verify(searchService).submitSearchFromQueryTemplate(eq(mockedTemplate), stringAsyncCbCaptor.capture());

        /* Verify that the active query is still null after the search was requested, but before the search returns
         * successfully
         */
        assertTrue(dsPresenter.getActiveQuery() == null);

        /* Verify that the active query is still null after failed search */
        stringAsyncCbCaptor.getValue().onFailure(any(Throwable.class));
        assertTrue(dsPresenter.getActiveQuery() == null);
    }

    /**
     * Verify #searchInit functionality when the call to retrieve saved templates is successful and returns queries.
     *
     * @see org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter#searchInit(org.iplantc.core.uidiskresource.client.views.DiskResourceView)
     */
    @Test public void testSearchInit_Case1() {
        when(drView.getToolbar()).thenReturn(drToolbar);
        dsPresenter.searchInit(drView);

        /* Verify that view is saved */
        assertEquals(drView, dsPresenter.getView());

        /* Verify that presenter registers itself to SaveDiskResourceQueryEvent and SubmitDiskResourceQueryEvents on
         * the view toolbar. */
        ArgumentCaptor<SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler> saveEventHandlerCaptor
                = ArgumentCaptor.forClass(SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler.class);
        ArgumentCaptor<SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler> submitEventHandlerCaptor
                = ArgumentCaptor.forClass(SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler.class);
        verify(drToolbar).addSaveDiskResourceQueryTemplateEventHandler(saveEventHandlerCaptor.capture());
        verify(drToolbar).addSubmitDiskResourceQueryEventHandler(submitEventHandlerCaptor.capture());
        assertEquals(saveEventHandlerCaptor.getValue(), dsPresenter);
        assertEquals(submitEventHandlerCaptor.getValue(), dsPresenter);

        /* Verify that presenter calls service to retrieve saved query templates */
        verify(searchService).getSavedQueryTemplates(drqtListAsyncCaptor.capture());

        List<Folder> rootItems = createTreeStoreRootFolderList();
        when(viewTreeStore.getRootItems()).thenReturn(rootItems);
        DiskResourceQueryTemplate retrieved1 = mock(DiskResourceQueryTemplate.class);
        DiskResourceQueryTemplate retrieved2 = mock(DiskResourceQueryTemplate.class);
        DiskResourceQueryTemplate retrieved3 = mock(DiskResourceQueryTemplate.class);
        List<DiskResourceQueryTemplate> retrievedTemplates = Lists.newArrayList(retrieved1, retrieved2, retrieved3);
        drqtListAsyncCaptor.getValue().onSuccess(retrievedTemplates);

        /* Verify that the retrieved list is added to the presenters list */
        assertTrue(dsPresenter.getQueryTemplates().containsAll(retrievedTemplates));
        assertEquals(retrievedTemplates.size(), dsPresenter.getQueryTemplates().size());

        ArgumentCaptor<Folder> folderArgumentCaptor = ArgumentCaptor.forClass(Folder.class);
        /* Verify that the tree store is updated, and all retrieved templates are added */
        verify(viewTreeStore, times(retrievedTemplates.size())).add(folderArgumentCaptor.capture());
        assertTrue(retrievedTemplates.containsAll(folderArgumentCaptor.getAllValues()));

    }

    /**
     * Verify #searchInit functionality when the call to retrieve saved templates fails
     *
     * @see org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter#searchInit(org.iplantc.core.uidiskresource.client.views.DiskResourceView)
     */
    @Test public void testSearchInit_Case2() {
        when(drView.getToolbar()).thenReturn(drToolbar);
        /* Verify size of presenter query template list prior to calling method under test */
        assertTrue(dsPresenter.getQueryTemplates().isEmpty());

        dsPresenter.searchInit(drView);

        /* Verify that view is saved */
        assertEquals(drView, dsPresenter.getView());

        /* Verify that presenter registers itself to SaveDiskResourceQueryEvent and SubmitDiskResourceQueryEvents on
         * the view toolbar. */
        ArgumentCaptor<SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler> saveEventHandlerCaptor
                = ArgumentCaptor.forClass(SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler.class);
        ArgumentCaptor<SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler> submitEventHandlerCaptor
                = ArgumentCaptor.forClass(SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler.class);
        verify(drToolbar).addSaveDiskResourceQueryTemplateEventHandler(saveEventHandlerCaptor.capture());
        verify(drToolbar).addSubmitDiskResourceQueryEventHandler(submitEventHandlerCaptor.capture());
        assertEquals(saveEventHandlerCaptor.getValue(), dsPresenter);
        assertEquals(submitEventHandlerCaptor.getValue(), dsPresenter);

        /* Verify that presenter calls service to retrieve saved query templates */
        verify(searchService).getSavedQueryTemplates(drqtListAsyncCaptor.capture());

        drqtListAsyncCaptor.getValue().onFailure(null);

        /* Verify that nothing has been added to the presenters query template list and there have been no interactions
           with the view's tree store */
        assertTrue(dsPresenter.getQueryTemplates().isEmpty());
        verify(viewTreeStore, never()).add(any(Folder.class));
        verify(viewTreeStore, never()).remove(any(Folder.class));
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
