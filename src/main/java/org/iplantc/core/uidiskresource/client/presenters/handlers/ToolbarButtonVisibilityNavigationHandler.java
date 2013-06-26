package org.iplantc.core.uidiskresource.client.presenters.handlers;

import java.util.List;

import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;

/**
 * A ToolbarButtonVisibilitySelectionHandler for Folder selections in the Data Window Navigation panel.
 * 
 * @author psarando
 * 
 */
public class ToolbarButtonVisibilityNavigationHandler extends
        ToolbarButtonVisibilitySelectionHandler<Folder> {

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

        toolbar.setNewFolderButtonEnabled(newFolderEnabled);
        toolbar.setRefreshButtonEnabled(oneSelected);
    }
}
