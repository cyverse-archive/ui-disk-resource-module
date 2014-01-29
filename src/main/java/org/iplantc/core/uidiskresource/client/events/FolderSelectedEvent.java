package org.iplantc.core.uidiskresource.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uidiskresource.client.events.FolderSelectedEvent.FolderSelectedEventHandler;

public class FolderSelectedEvent extends GwtEvent<FolderSelectedEventHandler> {

    public interface FolderSelectedEventHandler extends EventHandler {
        void onFolderSelected(FolderSelectedEvent event);
    }

    public static interface HasFolderSelectedEventHandlers {
        HandlerRegistration addFolderSelectedEventHandler(FolderSelectedEventHandler handler);
    }

    private final Folder selectedFolder;

    public FolderSelectedEvent(Folder selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }

    public static final GwtEvent.Type<FolderSelectedEventHandler> TYPE = new GwtEvent.Type<FolderSelectedEventHandler>();

    @Override
    public GwtEvent.Type<FolderSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FolderSelectedEventHandler handler) {
        handler.onFolderSelected(this);
    }
}
