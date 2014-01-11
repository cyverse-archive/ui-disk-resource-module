package org.iplantc.core.uidiskresource.client.search.views;

import org.iplantc.core.uidiskresource.client.search.views.cells.DiskResourceQueryFormNamePromptTest_NoEditorErrors;
import org.iplantc.core.uidiskresource.client.search.views.cells.DiskResourceQueryFormNamePromptTest_WithEditorErrors;
import org.iplantc.core.uidiskresource.client.search.views.cells.DiskResourceQueryFormTest_NoEditorErrors;
import org.iplantc.core.uidiskresource.client.search.views.cells.DiskResourceQueryFormTest_WithEditorErrors;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({DiskResourceSearchFieldTest.class, 
    DiskResourceQueryFormNamePromptTest_NoEditorErrors.class, 
    DiskResourceQueryFormNamePromptTest_WithEditorErrors.class, 
    DiskResourceQueryFormTest_NoEditorErrors.class, 
    DiskResourceQueryFormTest_WithEditorErrors.class})
public class AllSearchViewTests {

}
