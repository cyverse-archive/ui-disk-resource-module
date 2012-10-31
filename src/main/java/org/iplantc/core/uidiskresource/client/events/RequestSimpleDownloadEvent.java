package org.iplantc.core.uidiskresource.client.events;

import org.iplantc.core.uidiskresource.client.events.RequestSimpleDownloadEvent.RequestSimpleDownloadEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RequestSimpleDownloadEvent extends GwtEvent<RequestSimpleDownloadEventHandler> {
    public interface RequestSimpleDownloadEventHandler extends EventHandler {

        void onRequestSimpleDownload(RequestSimpleDownloadEvent event);

    }

    public static final GwtEvent.Type<RequestSimpleDownloadEventHandler> TYPE = new GwtEvent.Type<RequestSimpleDownloadEventHandler>();

    public RequestSimpleDownloadEvent(Object source) {
        setSource(source);
    }

    @Override
    public GwtEvent.Type<RequestSimpleDownloadEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RequestSimpleDownloadEventHandler handler) {
        handler.onRequestSimpleDownload(this);
    }
}
