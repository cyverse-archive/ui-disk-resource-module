package org.iplantc.core.uidiskresource.client.events;

import java.util.Set;

import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uidiskresource.client.events.DiskResourcesMovedEvent.DiskResourcesMovedEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class DiskResourcesMovedEvent extends GwtEvent<DiskResourcesMovedEventHandler> {
    
    public interface DiskResourcesMovedEventHandler extends EventHandler{

        void onDiskResourcesMoved(DiskResourcesMovedEvent event);
        
    }

    public static final GwtEvent.Type<DiskResourcesMovedEventHandler> TYPE = new GwtEvent.Type<DiskResourcesMovedEventHandler>();
    private final Folder destFolder;
    private final Set<DiskResource> resourcesToMove;
    private final Folder srcFolder;
    private final boolean moveContents;


    public DiskResourcesMovedEvent(final Folder srcFolder, final Folder destFolder, final Set<DiskResource> resourcesToMove, final boolean moveContents) {
        this.destFolder = destFolder;
        this.resourcesToMove = resourcesToMove;
        this.srcFolder = srcFolder;
        this.moveContents = moveContents;
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

    /**
     * @return the moveContents
     */
    public boolean isMoveContents() {
        return moveContents;
    }

    /**
     * @return the srcFolder
     */
    public Folder getSrcFolder() {
        return srcFolder;
    }

}
