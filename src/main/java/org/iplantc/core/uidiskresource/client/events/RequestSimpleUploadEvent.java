package org.iplantc.core.uidiskresource.client.events;

import org.iplantc.core.uidiskresource.client.events.RequestSimpleUploadEvent.RequestSimpleUploadEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RequestSimpleUploadEvent extends GwtEvent<RequestSimpleUploadEventHandler> {
    public interface RequestSimpleUploadEventHandler extends EventHandler {

        void onRequestSimpleUpload(RequestSimpleUploadEvent event);

    }

    public static final GwtEvent.Type<RequestSimpleUploadEventHandler> TYPE = new GwtEvent.Type<RequestSimpleUploadEventHandler>();

    public RequestSimpleUploadEvent(Object source) {
        setSource(source);
    }

    @Override
    public GwtEvent.Type<RequestSimpleUploadEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RequestSimpleUploadEventHandler handler) {
        handler.onRequestSimpleUpload(this);
    }

}
