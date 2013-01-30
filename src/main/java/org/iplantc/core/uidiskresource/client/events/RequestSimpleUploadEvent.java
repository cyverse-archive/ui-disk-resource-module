package org.iplantc.core.uidiskresource.client.events;

import org.iplantc.core.uidiskresource.client.events.RequestSimpleUploadEvent.RequestSimpleUploadEventHandler;
import org.iplantc.core.uidiskresource.client.models.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RequestSimpleUploadEvent extends GwtEvent<RequestSimpleUploadEventHandler> {
    public interface RequestSimpleUploadEventHandler extends EventHandler {

        void onRequestSimpleUpload(RequestSimpleUploadEvent event);

    }

    public static final GwtEvent.Type<RequestSimpleUploadEventHandler> TYPE = new GwtEvent.Type<RequestSimpleUploadEventHandler>();
    private final Folder destinationFolder;

    public RequestSimpleUploadEvent(Object source, final Folder destinationFolder) {
        setSource(source);
        this.destinationFolder = destinationFolder;
    }

    @Override
    public GwtEvent.Type<RequestSimpleUploadEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RequestSimpleUploadEventHandler handler) {
        handler.onRequestSimpleUpload(this);
    }

    public Folder getDestinationFolder() {
        return destinationFolder;
    }

}
