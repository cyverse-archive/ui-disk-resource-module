package org.iplantc.core.uidiskresource.client.events;

import org.iplantc.core.uidiskresource.client.events.RequestImportFromUrlEvent.RequestImportFromUrlEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RequestImportFromUrlEvent extends GwtEvent<RequestImportFromUrlEventHandler> {
    public interface RequestImportFromUrlEventHandler extends EventHandler {

        void onRequestUploadFromUrl(RequestImportFromUrlEvent event);

    }

    public static final GwtEvent.Type<RequestImportFromUrlEventHandler> TYPE = new GwtEvent.Type<RequestImportFromUrlEventHandler>();

    public RequestImportFromUrlEvent(Object source) {
        setSource(source);
    }

    @Override
    public GwtEvent.Type<RequestImportFromUrlEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RequestImportFromUrlEventHandler handler) {
        handler.onRequestUploadFromUrl(this);
    }
}
