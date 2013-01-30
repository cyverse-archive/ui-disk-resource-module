package org.iplantc.core.uidiskresource.client.events;

import org.iplantc.core.uidiskresource.client.events.FolderCreatedEvent.FolderCreatedEventHandler;
import org.iplantc.core.uidiskresource.client.models.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FolderCreatedEvent extends GwtEvent<FolderCreatedEventHandler> {

    public interface FolderCreatedEventHandler extends EventHandler {

        void onFolderCreated(Folder parentFolder, Folder newFolder);
    }

    public static final GwtEvent.Type<FolderCreatedEventHandler> TYPE = new GwtEvent.Type<FolderCreatedEventHandler>();
    private final Folder parentFolder;
    private final Folder newFolder;

    public FolderCreatedEvent(final Folder parentFolder, Folder newFolder) {
        this.parentFolder = parentFolder;
        this.newFolder = newFolder;
    }

    @Override
    public GwtEvent.Type<FolderCreatedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FolderCreatedEventHandler handler) {
        handler.onFolderCreated(parentFolder, newFolder);
    }

}
