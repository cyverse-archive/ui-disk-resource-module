package org.iplantc.core.uidiskresource.client.presenters;

import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;

final class ToolbarButtonVisibilityFolderSelectionHandler implements SelectionHandler<Folder> {
    private final DiskResourceViewToolbar toolbar;

    ToolbarButtonVisibilityFolderSelectionHandler(DiskResourceViewToolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public void onSelection(SelectionEvent<Folder> event) {
        if (event.getSelectedItem() == null) {
            return;
        }
        toolbar.setRefreshButtonEnabled(true);
        if (event.getSelectedItem().getPermissions().isOwner()) {
            toolbar.setBulkUploadEnabled(true);
            toolbar.setSimpleUploadEnabled(true);
            toolbar.setImportButtonEnabled(true);
            toolbar.setNewFolderButtonEnabled(true);
            toolbar.setSimpleDowloadButtonEnabled(true);
            toolbar.setBulkDownloadButtonEnabled(true);
            toolbar.setRenameButtonEnabled(true);
            toolbar.setDeleteButtonEnabled(true);
            toolbar.setShareButtonEnabled(true);
            toolbar.setMetadataButtonEnabled(false);
            toolbar.setDataQuotaButtonEnabled(false);
        } else {

            toolbar.setBulkUploadEnabled(false);
            toolbar.setSimpleUploadEnabled(false);
            toolbar.setImportButtonEnabled(false);
            toolbar.setNewFolderButtonEnabled(false);
            toolbar.setSimpleDowloadButtonEnabled(false);
            toolbar.setBulkDownloadButtonEnabled(false);
            toolbar.setRenameButtonEnabled(false);
            toolbar.setDeleteButtonEnabled(false);
            toolbar.setShareButtonEnabled(false);
            toolbar.setMetadataButtonEnabled(false);
            toolbar.setDataQuotaButtonEnabled(false);
        }
    }
}