package org.iplantc.core.uidiskresource.client.presenters.handlers;

import java.util.Collection;
import java.util.Set;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.File;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.events.DiskResourceRenamedEvent.DiskResourceRenamedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DiskResourceSelectedEvent;
import org.iplantc.core.uidiskresource.client.events.DiskResourceSelectedEvent.DiskResourceSelectedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DiskResourcesDeletedEvent.DiskResourcesDeletedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DiskResourcesMovedEvent;
import org.iplantc.core.uidiskresource.client.events.DiskResourcesMovedEvent.DiskResourcesMovedEventHandler;
import org.iplantc.core.uidiskresource.client.events.FileUploadedEvent;
import org.iplantc.core.uidiskresource.client.events.FileUploadedEvent.FileUploadedEventHandler;
import org.iplantc.core.uidiskresource.client.events.FolderCreatedEvent.FolderCreatedEventHandler;
import org.iplantc.core.uidiskresource.client.events.ShowFilePreviewEvent;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

public final class DiskResourcesEventHandler implements DiskResourcesDeletedEventHandler,
        DiskResourceSelectedEventHandler, DiskResourcesMovedEventHandler,
        DiskResourceRenamedEventHandler, FolderCreatedEventHandler, FileUploadedEventHandler {
    private final DiskResourceView.Presenter presenter;
    private final DiskResourceView view;

    public DiskResourcesEventHandler(DiskResourceView.Presenter presenter) {
        this.presenter = presenter;
        this.view = presenter.getView();
    }

    @Override
    public void onDiskResourcesDeleted(Collection<DiskResource> resources, Folder parentFolder) {
        presenter.refreshFolder(parentFolder);
    }

    @Override
    public void onSelect(DiskResourceSelectedEvent event) {
        if (event.getSource() != view) {
            return;
        }

        if (event.getSelectedItem() instanceof Folder) {
            presenter.setSelectedFolderById(event.getSelectedItem());
        } else if (event.getSelectedItem() instanceof File) {
            EventBus.getInstance().fireEvent(
                    new ShowFilePreviewEvent((File)event.getSelectedItem(), this));
        }
    }

    @Override
    public void onDiskResourcesMoved(DiskResourcesMovedEvent event) {
        Set<DiskResource> resourcesToMove = event.getResourcesToMove();
        Folder destinationFolder = event.getDestinationFolder();
        Folder selectedFolder = presenter.getSelectedFolder();

        if (resourcesToMove.contains(selectedFolder)) {
            selectedFolderMovedFromNavTree(selectedFolder, destinationFolder);
        } else {
            diskResourcesMovedFromGrid(resourcesToMove, selectedFolder, destinationFolder);
        }
    }

    private void selectedFolderMovedFromNavTree(Folder selectedFolder, Folder destinationFolder) {
        // If the selected folder happens to be one of the moved items, then view the destination by
        // setting it as the selected folder.
        String parentFolderId = DiskResourceUtil.parseParent(selectedFolder.getId());
        Folder parentFolder = view.getFolderById(parentFolderId);

        if (DiskResourceUtil.isDescendantOfFolder(parentFolder, destinationFolder)) {
            // The destination is under the parent, so if we prune the parent and set the destination
            // as the selected folder, the parent will lazy-load down to the destination.
            view.removeChildren(parentFolder);
        } else if (DiskResourceUtil.isDescendantOfFolder(destinationFolder, parentFolder)) {
            // The parent is under the destination, so we only need to view the destination folder's
            // contents and refresh its children.
            presenter.refreshFolder(destinationFolder);
        } else {
            // Refresh the parent folder since it has lost a child.
            presenter.refreshFolder(parentFolder);
            // Refresh the destination folder since it has gained a child.
            presenter.refreshFolder(destinationFolder);
        }

        // View the destination folder's contents.
        presenter.setSelectedFolderById(destinationFolder);
    }

    private void diskResourcesMovedFromGrid(Set<DiskResource> resourcesToMove, Folder selectedFolder,
            Folder destinationFolder) {
        if (DiskResourceUtil.containsFolder(resourcesToMove)) {
            // Refresh the destination folder, since it has gained a child.
            if (DiskResourceUtil.isDescendantOfFolder(destinationFolder, selectedFolder)) {
                view.removeChildren(destinationFolder);
            } else {
                // Refresh the selected folder since it has lost a child. This will also reload the
                // selected folder's contents in the grid.
                presenter.refreshFolder(selectedFolder);
                // Refresh the destination folder since it has gained a child.
                presenter.refreshFolder(destinationFolder);
                return;
            }
        }

        // Refresh the selected folder's contents.
        presenter.setSelectedFolderById(selectedFolder);
    }

    @Override
    public void onRename(DiskResource originalDr, DiskResource newDr) {
        view.updateDiskResource(originalDr, newDr);
    }
    
    @Override
    public void onFolderCreated(Folder parentFolder, Folder newFolder) {
        view.addFolder(parentFolder, newFolder);
    }
    
    @Override
    public void onFileUploaded(FileUploadedEvent event) {
        presenter.setSelectedFolderById(event.getUploadDestFolderFolder());
    }

}