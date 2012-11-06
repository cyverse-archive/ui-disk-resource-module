package org.iplantc.core.uidiskresource.client.events;

import java.util.Collection;

import org.iplantc.core.uidiskresource.client.events.DiskResourcesDeletedEvent.DiskResourcesDeletedEventHandler;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class DiskResourcesDeletedEvent extends GwtEvent<DiskResourcesDeletedEventHandler> {

    public interface DiskResourcesDeletedEventHandler extends EventHandler {


        void onDiskResourcesDeleted(Collection<DiskResource> resources);
    }

    public static final GwtEvent.Type<DiskResourcesDeletedEventHandler> TYPE = new GwtEvent.Type<DiskResourcesDeletedEventHandler>();
    private final Collection<DiskResource> resources;

    public DiskResourcesDeletedEvent(Collection<DiskResource> resources) {
        this.resources = resources;
    }

    @Override
    public GwtEvent.Type<DiskResourcesDeletedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DiskResourcesDeletedEventHandler handler) {
        handler.onDiskResourcesDeleted(resources);
    }
}
