package org.iplantc.core.uidiskresource.client.events;

import java.util.Collection;

import org.iplantc.core.uidiskresource.client.events.FoldersDeletedEvent.FoldersDeletedEventHandler;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FoldersDeletedEvent extends GwtEvent<FoldersDeletedEventHandler> {
    public interface FoldersDeletedEventHandler extends EventHandler {

        void onFoldersDeleted(Collection<Folder> folders);
    }

    public static final GwtEvent.Type<FoldersDeletedEventHandler> TYPE = new GwtEvent.Type<FoldersDeletedEvent.FoldersDeletedEventHandler>();
    private final Collection<Folder> folders;

    public FoldersDeletedEvent(Collection<Folder> folders) {
        this.folders = folders;
    }

    @Override
    public GwtEvent.Type<FoldersDeletedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FoldersDeletedEventHandler handler) {
        handler.onFoldersDeleted(folders);
    }
}
