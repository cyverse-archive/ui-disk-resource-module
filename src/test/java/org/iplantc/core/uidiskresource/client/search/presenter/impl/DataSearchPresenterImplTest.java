package org.iplantc.core.uidiskresource.client.search.presenter.impl;

import com.google.gwtmockito.GwtMockitoTestRunner;

import junit.framework.TestCase;

import org.iplantc.core.uicommons.client.services.SearchServiceFacade;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(GwtMockitoTestRunner.class)
public class DataSearchPresenterImplTest extends TestCase {

    DataSearchPresenterImpl dsPresenter;
    @Mock DiskResourceView drView;
    @Mock SearchServiceFacade searchService;

    @Test
    public void testDoSaveDiskResourceQueryTemplate() {
        fail("Not yet implemented");
    }

    @Test
    public void testDoSubmitDiskResourceQuery() {
        fail("Not yet implemented");
    }

    @Test
    public void testSearchInit() {
        fail("Not yet implemented");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dsPresenter = new DataSearchPresenterImpl(searchService);
    }

}
