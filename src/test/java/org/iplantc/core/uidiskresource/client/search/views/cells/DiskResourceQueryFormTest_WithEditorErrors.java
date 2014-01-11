package org.iplantc.core.uidiskresource.client.search.views.cells;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Element;
import com.google.gwtmockito.GwtMockito;
import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.gwtmockito.fakes.FakeSimpleBeanEditorDriverProvider;

import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uidiskresource.client.search.views.cells.DiskResourceQueryForm;
import org.iplantc.core.uidiskresource.client.search.views.cells.DiskResourceQueryFormNamePrompt;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * This test verifies the functionality of the {@link DiskResourceQueryForm} class when there are editor
 * errors.
 * 
 * @author jstroot
 * 
 */
@RunWith(GxtMockitoTestRunner.class)
public class DiskResourceQueryFormTest_WithEditorErrors {

    @Mock DiskResourceQueryFormNamePrompt namePrompt;

    @Mock DiskResourceQueryTemplate mockedTemplate;

    private DiskResourceQueryForm form;

    @Before public void setUp() {
        GwtMockito.useProviderForType(SimpleBeanEditorDriver.class, new FakeSimpleBeanEditorDriverProvider(true));
        form = new DiskResourceQueryForm(mockedTemplate);
        form.namePrompt = namePrompt;
    }

    /**
     * Verify the following when {@link DiskResourceQueryForm#createFilterLink} is selected;<br/>
     * <ol>
     * <li>the editor driver is flushed</li>
     * <li>the {@link DiskResourceQueryForm#namePrompt} is not shown when</li>
     * </ol>
     * 
     */
    @Test public void testOnCreateQueryTemplateClicked_withErrors() {
        form.onCreateQueryTemplateClicked(mock(ClickEvent.class));

        // Verify that name prompt is not shown
        verify(namePrompt, never()).show(any(DiskResourceQueryTemplate.class), any(Element.class), any(AnchorAlignment.class));
    }

    /**
     * Verify the following when {@link DiskResourceQueryForm#searchButton} is clicked;<br/>
     * <ol>
     * <li>the editor driver is flushed</li>
     * <li>no events are fired</li>
     * <li>the form is not hidden</li>
     * </ol>
     * 
     */
    @Test public void testOnSearchBtnSelected_withErrors() {
        DiskResourceQueryForm spy = spy(form);
        spy.onSearchBtnSelected(mock(SelectEvent.class));

        // Verify no events are fired
        verify(spy, never()).fireEvent(any(GwtEvent.class));

        // Verify that the form is not hidden
        verify(spy, never()).hide();
    }
}
