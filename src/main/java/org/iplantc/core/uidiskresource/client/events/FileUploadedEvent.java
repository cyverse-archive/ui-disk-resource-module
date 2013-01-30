package org.iplantc.core.uidiskresource.client.events;

import org.iplantc.core.uidiskresource.client.events.FileUploadedEvent.FileUploadedEventHandler;
import org.iplantc.core.uidiskresource.client.models.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FileUploadedEvent extends GwtEvent<FileUploadedEventHandler> {

    public interface FileUploadedEventHandler extends EventHandler {
        void onFileUploaded(FileUploadedEvent event);
    }

    public static final GwtEvent.Type<FileUploadedEventHandler> TYPE = new GwtEvent.Type<FileUploadedEventHandler>();
    private final Folder uploadDest;

    public FileUploadedEvent(Folder uploadDest) {
        this.uploadDest = uploadDest;
    }

    @Override
    public GwtEvent.Type<FileUploadedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FileUploadedEventHandler handler) {
        handler.onFileUploaded(this);
    }

    public Folder getUploadDestFolderFolder() {
        return uploadDest;
    }

}
