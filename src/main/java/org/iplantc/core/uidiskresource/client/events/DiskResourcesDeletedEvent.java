package org.iplantc.core.uidiskresource.client.events;

import java.util.Collection;

import org.iplantc.core.uidiskresource.client.events.DiskResourcesDeletedEvent.FilesDeletedEventHandler;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class DiskResourcesDeletedEvent extends GwtEvent<FilesDeletedEventHandler> {

    public interface FilesDeletedEventHandler extends EventHandler {


        void onFilesDeleted(Collection<DiskResource> resources);
    }

    public static final GwtEvent.Type<DiskResourcesDeletedEvent.FilesDeletedEventHandler> TYPE = new GwtEvent.Type<DiskResourcesDeletedEvent.FilesDeletedEventHandler>();
    private final Collection<DiskResource> resources;

    public DiskResourcesDeletedEvent(Collection<DiskResource> resources) {
        this.resources = resources;
    }

    @Override
    public GwtEvent.Type<DiskResourcesDeletedEvent.FilesDeletedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DiskResourcesDeletedEvent.FilesDeletedEventHandler handler) {
        handler.onFilesDeleted(resources);
    }
}
