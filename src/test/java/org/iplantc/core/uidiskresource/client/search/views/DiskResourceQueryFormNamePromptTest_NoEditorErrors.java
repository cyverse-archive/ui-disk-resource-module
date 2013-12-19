package org.iplantc.core.uidiskresource.client.search.views;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwtmockito.GwtMockito;
import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.gwtmockito.fakes.FakeSimpleBeanEditorDriverProvider;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GxtMockitoTestRunner.class)
public class DiskResourceQueryFormNamePromptTest_NoEditorErrors {

    private DiskResourceQueryFormNamePrompt namePrompt;

    @Before public void setUp() {
        GwtMockito.useProviderForType(SimpleBeanEditorDriver.class, new FakeSimpleBeanEditorDriverProvider(false));
        namePrompt = new DiskResourceQueryFormNamePrompt();
    }

    /**
     * Verify the following when {@link DiskResourceQueryFormNamePrompt#cancelSaveFilterBtn} is clicked;<br/>
     * 
     * <ol>
     * <li>the {@link DiskResourceQueryFormNamePrompt#name} field is reset to its original value</li>
     * <li>the form is hidden</li>
     * </ol>
     */
    @Test public void testOnCancelSaveFilter_noErrors() {
        DiskResourceQueryFormNamePrompt spy = spy(namePrompt);
        spy.onCancelSaveFilter(mock(SelectEvent.class));

        // Verify that the name field is reset
        verify(spy.name).reset();

        // Verify that the form is hidden
        verify(spy).hide();
    }

    /**
     * Verify the following when {@link DiskResourceQueryFormNamePrompt#saveFilterBtn} is clicked;<br/>
     * 
     * <ol>
     * <li>the editor driver is flushed</li>
     * <li>a {@link SubmitDiskResourceQueryEvent} is fired with the flushed template</li>
     * <li>the form is hidden</li>
     * </ol>
     */
    @Test public void testOnSaveFilterSelected_noErrors() {
        DiskResourceQueryFormNamePrompt spy = spy(namePrompt);
        spy.onSaveFilterSelected(mock(SelectEvent.class));

        // Verify that the appropriate event is fired
        verify(spy).fireEvent(any(SubmitDiskResourceQueryEvent.class));

        // Verify that the form is hidden
        verify(spy).hide();
    }

}
