package org.iplantc.core.uidiskresource.client.presenters;

import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;

import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

final class ToolbarButtonVisibilityDiskResourceSelectionChangedHandler implements
        SelectionChangedHandler<DiskResource> {
    private final DiskResourceViewToolbar toolbar;

    ToolbarButtonVisibilityDiskResourceSelectionChangedHandler(DiskResourceViewToolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<DiskResource> event) {
        if (event.getSelection().isEmpty()) {
            return;
        }

        if (event.getSelection().size() == 1) {
            toolbar.setRefreshButtonEnabled(true);
            // Check Ownership
            if (event.getSelection().get(0).getPermissions().isOwner()) {
                toolbar.setBulkUploadEnabled(true);
                toolbar.setSimpleUploadEnabled(true);
                toolbar.setImportButtonEnabled(true);
                toolbar.setNewFolderButtonEnabled(true);
                toolbar.setSimpleDowloadButtonEnabled(true);
                toolbar.setBulkDownloadButtonEnabled(true);
                toolbar.setRenameButtonEnabled(true);
                toolbar.setDeleteButtonEnabled(true);
                toolbar.setShareButtonEnabled(true);
                toolbar.setMetadataButtonEnabled(true);
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

        } else {

            toolbar.setBulkUploadEnabled(false);
            toolbar.setSimpleUploadEnabled(false);
            toolbar.setImportButtonEnabled(false);
            toolbar.setNewFolderButtonEnabled(false);
            toolbar.setRefreshButtonEnabled(true);
            toolbar.setSimpleDowloadButtonEnabled(false);
            toolbar.setBulkDownloadButtonEnabled(false);
            toolbar.setRenameButtonEnabled(false);
            toolbar.setDeleteButtonEnabled(true);
            toolbar.setShareButtonEnabled(true);
            toolbar.setMetadataButtonEnabled(false);
            toolbar.setDataQuotaButtonEnabled(false);
        }
    }
}