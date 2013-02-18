package org.iplantc.core.uidiskresource.client.presenters.handlers;

import java.util.Collection;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uidiskresource.client.events.DiskResourceSelectedEvent;
import org.iplantc.core.uidiskresource.client.events.DiskResourcesMovedEvent;
import org.iplantc.core.uidiskresource.client.events.FileUploadedEvent;
import org.iplantc.core.uidiskresource.client.events.ShowFilePreviewEvent;
import org.iplantc.core.uidiskresource.client.events.DiskResourceRenamedEvent.DiskResourceRenamedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DiskResourceSelectedEvent.DiskResourceSelectedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DiskResourcesDeletedEvent.DiskResourcesDeletedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DiskResourcesMovedEvent.DiskResourcesMovedEventHandler;
import org.iplantc.core.uidiskresource.client.events.FileUploadedEvent.FileUploadedEventHandler;
import org.iplantc.core.uidiskresource.client.events.FolderCreatedEvent.FolderCreatedEventHandler;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

public final class DiskResourcesEventHandler implements DiskResourcesDeletedEventHandler, DiskResourceSelectedEventHandler, DiskResourcesMovedEventHandler, DiskResourceRenamedEventHandler, FolderCreatedEventHandler, FileUploadedEventHandler {
    private final DiskResourceView.Presenter presenter;
    private final DiskResourceView view;

    public DiskResourcesEventHandler(DiskResourceView.Presenter presenter) {
        this.presenter = presenter;
        this.view = presenter.getView();
    }

    @Override
    public void onDiskResourcesDeleted(Collection<DiskResource> resources, Folder parentFolder) {
        view.refreshFolder(parentFolder);
    }

    @Override
    public void onSelect(DiskResourceSelectedEvent event) {
        if (event.getSelectedItem() instanceof Folder) {
            view.setSelectedFolder((Folder)event.getSelectedItem());
        } else if (event.getSelectedItem() instanceof File) {
            EventBus.getInstance().fireEvent(
                    new ShowFilePreviewEvent((File)event.getSelectedItem(), this));
        }
    }
    
    @Override
    public void onDiskResourcesMoved(DiskResourcesMovedEvent event) {
        // Determine which folder is the ancestor, then refresh it
        if (DiskResourceUtil.isDescendantOfFolder(event.getDestinationFolder(), presenter.getSelectedFolder())) {
            view.refreshFolder(event.getDestinationFolder());
        } else {
            view.refreshFolder(presenter.getSelectedFolder());
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
        view.refreshFolder(event.getUploadDestFolderFolder());
    }

}