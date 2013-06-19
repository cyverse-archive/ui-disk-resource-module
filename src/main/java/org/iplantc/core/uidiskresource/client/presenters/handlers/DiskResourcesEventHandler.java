package org.iplantc.core.uidiskresource.client.presenters.handlers;

import java.util.Collection;
import java.util.Set;

import org.iplantc.core.uicommons.client.events.EventBus;
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
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
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
            // If the selected folder happens to be one of the moved items, then view the destination.
            String parentFolderId = DiskResourceUtil.parseParent(selectedFolder.getId());
            Folder parentFolder = view.getFolderById(parentFolderId);

            presenter.refreshFolder(destinationFolder);
            if (!DiskResourceUtil.isDescendantOfFolder(destinationFolder, parentFolder)) {
                presenter.refreshFolder(parentFolder);
            }

            presenter.setSelectedFolderById(destinationFolder);
        } else {
            if (DiskResourceUtil.containsFolder(resourcesToMove)) {
                // Refresh the destination since it has new children.
                presenter.refreshFolder(destinationFolder);
                if (!DiskResourceUtil.isDescendantOfFolder(destinationFolder, selectedFolder)) {
                    // Refresh the selected Folder since it lost children.
                    presenter.refreshFolder(selectedFolder);
                }
            }

            presenter.setSelectedFolderById(selectedFolder);
        }
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