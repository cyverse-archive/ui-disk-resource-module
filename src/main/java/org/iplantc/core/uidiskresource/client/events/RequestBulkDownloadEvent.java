package org.iplantc.core.uidiskresource.client.events;

import org.iplantc.core.uidiskresource.client.events.RequestBulkDownloadEvent.RequestBulkDownloadEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RequestBulkDownloadEvent extends GwtEvent<RequestBulkDownloadEventHandler> {
    public interface RequestBulkDownloadEventHandler extends EventHandler {

        void onRequestBulkDownload(RequestBulkDownloadEvent event);
    }

    public static final GwtEvent.Type<RequestBulkDownloadEventHandler> TYPE = new GwtEvent.Type<RequestBulkDownloadEventHandler>();

    public RequestBulkDownloadEvent(Object source) {
        setSource(source);
    }

    @Override
    public GwtEvent.Type<RequestBulkDownloadEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RequestBulkDownloadEventHandler handler) {
        handler.onRequestBulkDownload(this);
    }

}
