package org.iplantc.core.uidiskresource.client.events;

import java.util.Collection;

import org.iplantc.core.uidiskresource.client.events.FilesDeletedEvent.FilesDeletedEventHandler;
import org.iplantc.core.uidiskresource.client.models.autobeans.File;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FilesDeletedEvent extends GwtEvent<FilesDeletedEventHandler> {

    public interface FilesDeletedEventHandler extends EventHandler {


        void onFilesDeleted(Collection<File> files);
    }

    public static final GwtEvent.Type<FilesDeletedEvent.FilesDeletedEventHandler> TYPE = new GwtEvent.Type<FilesDeletedEvent.FilesDeletedEventHandler>();
    private final Collection<File> files;

    public FilesDeletedEvent(Collection<File> files) {
        this.files = files;
    }

    @Override
    public GwtEvent.Type<FilesDeletedEvent.FilesDeletedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FilesDeletedEvent.FilesDeletedEventHandler handler) {
        handler.onFilesDeleted(files);
    }
}
