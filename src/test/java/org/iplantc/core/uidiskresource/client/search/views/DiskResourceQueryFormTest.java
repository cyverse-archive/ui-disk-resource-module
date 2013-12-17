package org.iplantc.core.uidiskresource.client.search.views;

import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.core.client.resources.CommonStyles.CommonStylesAppearance;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.Composite;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.models.search.SearchAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.search.views.DiskResourceQueryForm.SearchFormEditorDriver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * <a href="https://github.com/google/gwtmockito">mockito</a>
 * 
 * @author jstroot
 * 
 */
@RunWith(GwtMockitoTestRunner.class)
public class DiskResourceQueryFormTest {

    @Mock SearchFormEditorDriver editorDriver;
    @Mock
    SearchAutoBeanFactory searchFactory;
    @Mock
    Composite cp;
    @Mock
    Component comp;
    @Mock
    CommonStylesAppearance commonStyles;
    DiskResourceQueryForm form;
    private DiskResourceQueryTemplate mockedTemplate;

    @Test
    public void testOnCancelSaveFilter() {
        fail("Not yet implemented");
    }

    /**
     * Verify that when the {@link DiskResourceQueryForm#cancelSaveFilterBtn} is selected that the
     * editorDriver is flushed.
     * In this case, we want to verify that the editorDriver does not have any errors
     */
    @Test
    public void testOnCreateQueryTemplateClicked_noErrors() {

        when(editorDriver.flush()).thenReturn(mockedTemplate);
        when(editorDriver.hasErrors()).thenReturn(false);

        // Verify that the name prompt is shown

    }

    @Test
    public void testOnSaveFilterSelected() {
        fail("Not yet implemented");
    }

    @Test
    public void testOnSearchBtnSelected() {
        fail("Not yet implemented");
    }

    @Before
    public void setUp() throws Exception {
        mockedTemplate = mock(DiskResourceQueryTemplate.class);
        form = new DiskResourceQueryForm(mockedTemplate);
    }

}
