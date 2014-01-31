package org.iplantc.core.uidiskresource.client.search.views;

import com.google.gwtmockito.GxtMockitoTestRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.iplantc.core.uicommons.client.events.SubmitTextSearchEvent;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GxtMockitoTestRunner.class)
public class DiskResourceSearchFieldTest {

    @Test public void testDoSubmitDiskResourceQuery() {
        DiskResourceSearchField spy = spy(new DiskResourceSearchField());

        // Call method under test
        spy.doSubmitDiskResourceQuery(any(SubmitDiskResourceQueryEvent.class));
        
        verify(spy).clear();
    }
    
    @Test public void testOnSubmitTextSearch() {
        DiskResourceSearchField spy = spy(new DiskResourceSearchField());

        // Call method under test
        spy.onSubmitTextSearch(any(SubmitTextSearchEvent.class));
        
        verify(spy).finishEditing();
    }

}
