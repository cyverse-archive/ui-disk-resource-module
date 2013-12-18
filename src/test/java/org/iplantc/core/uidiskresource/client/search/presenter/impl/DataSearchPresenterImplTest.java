package org.iplantc.core.uidiskresource.client.search.presenter.impl;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.data.shared.TreeStore;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter;
import org.iplantc.core.uidiskresource.client.search.views.GxtMockitoTestRunner;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(GxtMockitoTestRunner.class)
public class DataSearchPresenterImplTest {

    private DataSearchPresenterImpl dsPresenter;

    @Mock DiskResourceView drView;
    @Mock SearchServiceFacade searchService;

    @Before public void setUp() {
        dsPresenter = new DataSearchPresenterImpl(searchService);
    }

    /**
     * 
     */
    @Test public void testDoSaveDiskResourceQueryTemplate() {
        
        SaveDiskResourceQueryEvent mockEvent = mock(SaveDiskResourceQueryEvent.class);
        DiskResourceQueryTemplate mockTemplate = mock(DiskResourceQueryTemplate.class);
        when(mockEvent.getQueryTemplate()).thenReturn(mockTemplate);

        dsPresenter.doSaveDiskResourceQueryTemplate(mockEvent);

        verify(searchService).saveQueryTemplate(eq(mockTemplate), any(AsyncCallback.class));
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

    }


}
