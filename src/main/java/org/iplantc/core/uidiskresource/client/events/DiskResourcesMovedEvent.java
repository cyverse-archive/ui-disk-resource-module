package org.iplantc.core.uidiskresource.client.events;

import java.util.Set;

import org.iplantc.core.uidiskresource.client.events.DiskResourcesMovedEvent.DiskResourcesMovedEventHandler;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.Folder;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class DiskResourcesMovedEvent extends GwtEvent<DiskResourcesMovedEventHandler> {
    
    public interface DiskResourcesMovedEventHandler extends EventHandler{

        void onDiskResourcesMoved(DiskResourcesMovedEvent event);
        
    }

    public static final GwtEvent.Type<DiskResourcesMovedEventHandler> TYPE = new GwtEvent.Type<DiskResourcesMovedEventHandler>();
    private final Folder destFolder;
    private final Set<DiskResource> resourcesToMove;


    public DiskResourcesMovedEvent(final Folder destFolder, final Set<DiskResource> resourcesToMove) {
        this.destFolder = destFolder;
        this.resourcesToMove = resourcesToMove;
    }

    @Override
    public GwtEvent.Type<DiskResourcesMovedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DiskResourcesMovedEventHandler handler) {
        handler.onDiskResourcesMoved(this);
    }
    
    public Folder getDestinationFolder(){
        return destFolder;
    }
    
    public Set<DiskResource> getResourcesToMove(){
        return resourcesToMove;
    }

}
