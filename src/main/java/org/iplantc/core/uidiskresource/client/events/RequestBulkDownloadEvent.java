package org.iplantc.core.uidiskresource.client.events;

import java.util.Set;

import org.iplantc.core.uidiskresource.client.events.RequestBulkDownloadEvent.RequestBulkDownloadEventHandler;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RequestBulkDownloadEvent extends GwtEvent<RequestBulkDownloadEventHandler> {
    public interface RequestBulkDownloadEventHandler extends EventHandler {

        void onRequestBulkDownload(RequestBulkDownloadEvent event);
    }

    public static final GwtEvent.Type<RequestBulkDownloadEventHandler> TYPE = new GwtEvent.Type<RequestBulkDownloadEventHandler>();
    private final Set<DiskResource> requestedResources;
    private final Folder currentFolder;

    public RequestBulkDownloadEvent(Object source, final Set<DiskResource> requestedResources, final Folder currentFolder) {
        setSource(source);
        this.requestedResources = requestedResources;
        this.currentFolder = currentFolder;
    }

    @Override
    public GwtEvent.Type<RequestBulkDownloadEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RequestBulkDownloadEventHandler handler) {
        handler.onRequestBulkDownload(this);
    }

    public Set<DiskResource> getRequestedResources() {
        return requestedResources;
    }

    public Folder getCurrentFolder() {
        return currentFolder;
    }

}
