package org.iplantc.de.diskResource.client.presenters.handlers;

import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;
import org.iplantc.de.diskResource.client.views.widgets.DiskResourceViewToolbar;

import java.util.List;

/**
 * A ToolbarButtonVisibilitySelectionHandler for Folder selections in the Data Window Navigation panel.
 * 
 * @author psarando
 * 
 */
public class ToolbarButtonVisibilityNavigationHandler extends
        ToolbarButtonVisibilitySelectionHandler<Folder> implements SubmitDiskResourceQueryEventHandler {

    public ToolbarButtonVisibilityNavigationHandler(DiskResourceViewToolbar toolbar) {
        super(toolbar);
    }

    @Override
    protected void updateToolbar(List<Folder> selection) {
        boolean oneSelected = selection.size() == 1;
        boolean canUpload = oneSelected && DiskResourceUtil.canUploadTo(selection.get(0));
        boolean newFolderEnabled = canUpload;

        toolbar.setUploadsEnabled(canUpload);
        toolbar.setBulkUploadEnabled(canUpload);
        toolbar.setSimpleUploadEnabled(canUpload);
        toolbar.setImportButtonEnabled(canUpload);

        toolbar.setNewButtonEnabled(newFolderEnabled);
        toolbar.setNewFileButtonEnabled(newFolderEnabled);
        toolbar.setNewFolderButtonEnabled(newFolderEnabled);
        toolbar.setRefreshButtonEnabled(oneSelected);
    }

    @Override
    public void doSubmitDiskResourceQuery(SubmitDiskResourceQueryEvent event) {
        toolbar.setRefreshButtonEnabled(event.getQueryTemplate() != null);
    }
}
