package org.iplantc.core.uidiskresource.client.presenters.handlers;

import java.util.List;

import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.views.IsDiskResourceRoot;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;

import com.google.common.collect.Lists;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public final class ToolbarButtonVisibilitySelectionHandler<R extends DiskResource> implements
        SelectionChangedHandler<R>, SelectionHandler<R> {
    private final DiskResourceViewToolbar toolbar;
    private final IsDiskResourceRoot rootFinder;

    public ToolbarButtonVisibilitySelectionHandler(final DiskResourceViewToolbar toolbar, final IsDiskResourceRoot rootFinder) {
        this.toolbar = toolbar;
        this.rootFinder = rootFinder;
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<R> event) {
        updateToolbar(event.getSelection());
    }

    @Override
    public void onSelection(SelectionEvent<R> event) {
        List<R>  selection = Lists.newArrayList();
        selection.add(event.getSelectedItem());
        updateToolbar(selection);
    }
    
    private void updateToolbar(List<R> selection){
        updateToolbarDownloadButtons(selection);
        updateToolbarUploadButtons(selection);

        if (selection.isEmpty()) {
            // Set all disabled
            toolbar.setNewFolderButtonEnabled(false);
            toolbar.setRefreshButtonEnabled(false);
            toolbar.setRenameButtonEnabled(false);
            toolbar.setDeleteButtonEnabled(false);
            toolbar.setShareButtonEnabled(false);
        } else if (selection.size() == 1) {
            toolbar.setRefreshButtonEnabled(true);
            // Check Ownership
            if (selection.get(0).getPermissions().isOwner() 
                    && !rootFinder.isRoot(selection.get(0))) {
                toolbar.setNewFolderButtonEnabled(true);
                toolbar.setRenameButtonEnabled(true);
                toolbar.setDeleteButtonEnabled(true);
                toolbar.setShareButtonEnabled(true);
            } else {
                toolbar.setNewFolderButtonEnabled(false);
                toolbar.setRenameButtonEnabled(false);
                toolbar.setDeleteButtonEnabled(false);
                toolbar.setShareButtonEnabled(false);
            }

        } else {
            toolbar.setNewFolderButtonEnabled(false);
            toolbar.setRefreshButtonEnabled(true);
            toolbar.setRenameButtonEnabled(false);
            toolbar.setDeleteButtonEnabled(true);
            toolbar.setShareButtonEnabled(true);
        }

        updateRestoreMenuItem(selection);
    }

    private void updateRestoreMenuItem(List<R> selection) {
        if (selection.isEmpty()) {
            toolbar.setRestoreMenuItemEnabled(false);
            return;
        }
        for (R item : selection) {
                DiskResource dr = item;
                String trashPath = UserInfo.getInstance().getTrashPath();
                if(dr.getId().equals(trashPath)) {
                    toolbar.setRestoreMenuItemEnabled(false);
                    return;
                }
                    
                if (!dr.getId().startsWith(trashPath)) {
                    toolbar.setRestoreMenuItemEnabled(false);
                    return;
                }
        }
        toolbar.setRestoreMenuItemEnabled(true);
    }

    private void updateToolbarDownloadButtons(List<R> selection) {
        if(selection.isEmpty()){
            toolbar.setDownloadsEnabled(false);
            toolbar.setSimpleDowloadButtonEnabled(false);
            toolbar.setBulkDownloadButtonEnabled(false);
        } else if(DiskResourceUtil.containsFolder(selection)){
            toolbar.setDownloadsEnabled(true);
            toolbar.setSimpleDowloadButtonEnabled(false);
            toolbar.setBulkDownloadButtonEnabled(true);
        }else{
            toolbar.setDownloadsEnabled(true);
            toolbar.setSimpleDowloadButtonEnabled(true);
            toolbar.setBulkDownloadButtonEnabled(true);
        }
    }

    private void updateToolbarUploadButtons(List<R> selection) {
        if((selection.size() == 1) && DiskResourceUtil.canUploadTo(selection.get(0))){
            toolbar.setUploadsEnabled(true);
            toolbar.setBulkUploadEnabled(true);
            toolbar.setSimpleUploadEnabled(true);
            toolbar.setImportButtonEnabled(true);
        } else {
            toolbar.setUploadsEnabled(false);
            toolbar.setBulkUploadEnabled(false);
            toolbar.setSimpleUploadEnabled(false);
            toolbar.setImportButtonEnabled(false);
        }        
    }
}