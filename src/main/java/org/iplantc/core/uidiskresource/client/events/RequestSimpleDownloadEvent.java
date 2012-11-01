package org.iplantc.core.uidiskresource.client.events;

import java.util.Set;

import org.iplantc.core.uidiskresource.client.events.RequestSimpleDownloadEvent.RequestSimpleDownloadEventHandler;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RequestSimpleDownloadEvent extends GwtEvent<RequestSimpleDownloadEventHandler> {
    public interface RequestSimpleDownloadEventHandler extends EventHandler {

        void onRequestSimpleDownload(RequestSimpleDownloadEvent event);

    }

    public static final GwtEvent.Type<RequestSimpleDownloadEventHandler> TYPE = new GwtEvent.Type<RequestSimpleDownloadEventHandler>();
    private final Set<DiskResource> requestedResources;

    public RequestSimpleDownloadEvent(Object source, final Set<DiskResource> requestedResources) {
        setSource(source);
        this.requestedResources = requestedResources;
    }

    @Override
    public GwtEvent.Type<RequestSimpleDownloadEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RequestSimpleDownloadEventHandler handler) {
        handler.onRequestSimpleDownload(this);
    }

    public Set<DiskResource> getRequestedResources() {
        return requestedResources;
    }
}
