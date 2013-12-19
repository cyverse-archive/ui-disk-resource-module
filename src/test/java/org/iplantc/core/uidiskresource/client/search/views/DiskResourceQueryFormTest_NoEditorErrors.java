package org.iplantc.core.uidiskresource.client.search.views;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Element;
import com.google.gwtmockito.GwtMockito;
import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.gwtmockito.fakes.FakeSimpleBeanEditorDriverProvider;

import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * This test verifies the functionality of the {@link DiskResourceQueryForm} class when there are no
 * editor errors.
 * 
 * @author jstroot
 * 
 */
@RunWith(GxtMockitoTestRunner.class)
public class DiskResourceQueryFormTest_NoEditorErrors {

    @Mock DiskResourceQueryFormNamePrompt namePrompt;

    @Mock DiskResourceQueryTemplate mockedTemplate;

    private DiskResourceQueryForm form;

    @Before public void setUp() {
        GwtMockito.useProviderForType(SimpleBeanEditorDriver.class, new FakeSimpleBeanEditorDriverProvider(false));
        form = new DiskResourceQueryForm(mockedTemplate);
        // form = new DiskResourceQueryForm();
        form.namePrompt = namePrompt;
    }


    /**
     * Verify the following when {@link DiskResourceQueryForm#createFilterLink} is selected;<br/>
     * <ol>
     * <li>the editor driver is flushed</li>
     * <li>the {@link DiskResourceQueryForm#namePrompt} is shown</li>
     * </ol>
     */
    @Test public void testOnCreateQueryTemplateClicked_noErrors() {
        form.onCreateQueryTemplateClicked(mock(ClickEvent.class));

        // Verify that the name prompt is shown
        verify(namePrompt).show(any(DiskResourceQueryTemplate.class), any(Element.class), any(AnchorAlignment.class));
    }

    /**
     * Verify the following when {@link DiskResourceQueryForm#searchButton} is clicked <br/>
     * 
     * <ol>
     * <li>the editor driver is flushed</li>
     * <li>a {@link SubmitDiskResourceQueryEvent} is fired with the flushed template</li>
     * <li>the form is hidden</li>
     * </ol>
     */
    @Test
    public void testOnSearchBtnSelected_noErrors() {
        DiskResourceQueryForm spy = spy(form);
        spy.onSearchBtnSelected(mock(SelectEvent.class));

        // Verify that the appropriate event is fired
        verify(spy).fireEvent(any(SubmitDiskResourceQueryEvent.class));

        // Verify that the form is hidden
        verify(spy).hide();
    }


}
