package org.iplantc.core.uidiskresource.client.search.presenter.impl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;

import com.sencha.gxt.data.shared.TreeStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceAutoBeanFactory;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

/**
 * @author jstroot
 * 
 */
@RunWith(GxtMockitoTestRunner.class)
public class DataSearchPresenterImplTest {

    private DataSearchPresenterImpl dsPresenter;

    @Mock DiskResourceView drView;
    @Mock TreeStore<Folder> viewTreeStore;
    @Mock SearchServiceFacade searchService;
    @Mock DiskResourceAutoBeanFactory diskResourceAbFactory;

    @Captor ArgumentCaptor<List<DiskResourceQueryTemplate>> drqtListCaptor;
    @Captor ArgumentCaptor<AsyncCallback<String>> stringAsyncCbCaptor;

    @Before public void setUp() {
        dsPresenter = new DataSearchPresenterImpl(searchService, diskResourceAbFactory);
        dsPresenter.view = drView;
        when(drView.getTreeStore()).thenReturn(viewTreeStore);
    }

    /**
     * Verifies functionality of the {@link DataSearchPresenter#doSaveDiskResourceQueryTemplate()} method
     * for the following pre-conditions:<br/>
     * 
     * <ul>
     * 
     * </ul>
     * 
     * Verify the following when {@link DataSearchPresenter#doSaveDiskResourceQueryTemplate()} is
     * invoked:<br/>
     * 
     * <ol>
     * <li>verify that given query templates are stored and persisted.</li>
     * <li>verify that the query template is given a unique id when the template's id is null or empty</li>
     * <li>verify that updated queries are persisted over their previous values</li>
     * <li>verify that a search is requested after successfully persisting the query</li>
     * </ol>
     */
    @Test public void testDoSaveDiskResourceQueryTemplate() {
        
        DataSearchPresenterImpl spy = spy(dsPresenter);
        SaveDiskResourceQueryEvent mockEvent = mock(SaveDiskResourceQueryEvent.class);
        DiskResourceQueryTemplate mockTemplate = mock(DiskResourceQueryTemplate.class);
        when(mockEvent.getQueryTemplate()).thenReturn(mockTemplate);

        spy.doSaveDiskResourceQueryTemplate(mockEvent);

        // Verify that a unique id is set when the template's id is null. The mock will return null by
        // default
        verify(searchService).getUniqueId();
        verify(mockTemplate).setId(any(String.class));

        // Verify that the service was called to save the template, and only one template was saved
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), stringAsyncCbCaptor.capture());
        assertEquals(1, drqtListCaptor.getValue().size());
        assertEquals(drqtListCaptor.getValue().get(0), mockTemplate);

        // Verify that a search is requested after a successful persist.
        stringAsyncCbCaptor.getValue().onSuccess("");
        ArgumentCaptor<SubmitDiskResourceQueryEvent> submitEventCaptor = ArgumentCaptor.forClass(SubmitDiskResourceQueryEvent.class);
        verify(spy).doSubmitDiskResourceQuery(submitEventCaptor.capture());
        assertEquals(submitEventCaptor.getValue().getQueryTemplate(), mockTemplate);

        /*
         * ============= RESET ================
         */
        reset(searchService);
        reset(mockEvent);
        reset(spy);
        when(mockTemplate.getId()).thenReturn("firstMock");
        DiskResourceQueryTemplate mockTemplate_2 = mock(DiskResourceQueryTemplate.class);
        when(mockTemplate_2.getId()).thenReturn("");
        when(mockEvent.getQueryTemplate()).thenReturn(mockTemplate_2);

        spy.doSaveDiskResourceQueryTemplate(mockEvent);

        // Verify that a unique id is set when the template's id is an empty string.
        verify(searchService).getUniqueId();
        verify(mockTemplate_2).setId(any(String.class));

        // Verify that the service was called to save the template, and 2 templates were saved
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), stringAsyncCbCaptor.capture());
        assertEquals(2, drqtListCaptor.getValue().size());
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate));
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate_2));

        // Verify that a search is requested after a successful persist.
        stringAsyncCbCaptor.getValue().onSuccess("");
        verify(spy).doSubmitDiskResourceQuery(submitEventCaptor.capture());
        assertEquals(submitEventCaptor.getValue().getQueryTemplate(), mockTemplate_2);
        spy.doSaveDiskResourceQueryTemplate(mockEvent);

        /*
         * ============= RESET ================
         */
        reset(searchService);
        reset(mockEvent);
        reset(mockTemplate_2);
        reset(spy);
        when(mockTemplate_2.getId()).thenReturn("secondMock");
        DiskResourceQueryTemplate mockTemplate_3 = mock(DiskResourceQueryTemplate.class);
        when(mockTemplate_3.getId()).thenReturn("firstMock");
        when(mockEvent.getQueryTemplate()).thenReturn(mockTemplate_3);

        spy.doSaveDiskResourceQueryTemplate(mockEvent);

        // Verify that the template's id is not set when it is not null or empty
        verify(searchService, never()).getUniqueId();
        verify(mockTemplate_3, never()).setId(any(String.class));

        // Verify that the service was called to save the template, and only 2 templates were saved.
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), stringAsyncCbCaptor.capture());
        assertEquals(2, drqtListCaptor.getValue().size());
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate_2));
        assertTrue(drqtListCaptor.getValue().contains(mockTemplate_3));
        assertFalse(drqtListCaptor.getValue().contains(mockTemplate));

        // Verify that a search is not requested after failure to persist
        stringAsyncCbCaptor.getValue().onFailure(null);
        verify(spy, never()).doSubmitDiskResourceQuery(any(SubmitDiskResourceQueryEvent.class));
    }

    /**
     * Verifies functionality of the {@link DataSearchPresenter#doSubmitDiskResourceQuery()} method for
     * the following preconditions:<br/>
     * <ul>
     * <li>the presenter's list of query templates <b>does not</b> contain the passed
     * </ul>
     * Verify the following when {@link DataSearchPresenter#doSubmitDiskResourceQuery()} is invoked;<br/>
     * 
     * <ol>
     * <li>the query passed with the {@link SubmitDiskResourceQueryEvent} input parameter is set as the
     * active query.</li>
     * <li>the query passed in is added to the presenter's list of querytemplates.</li>
     * <li>the {@link TreeStore} associated with the {@link DataSearchPresenter#getView()} is updated</li>
     * </ol>
     */
    @Test public void testDoSubmitDiskResourceQuery() {
        // TODO Test to be implemented

    }


}
