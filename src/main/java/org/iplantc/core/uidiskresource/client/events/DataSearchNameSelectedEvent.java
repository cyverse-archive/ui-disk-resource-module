/**
 * 
 */
package org.iplantc.core.uidiskresource.client.events;

import org.iplantc.core.uidiskresource.client.events.DataSearchNameSelectedEvent.DataSearchNameSelectedEventHandler;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author sriram
 *
 */
public class DataSearchNameSelectedEvent extends GwtEvent<DataSearchNameSelectedEventHandler> {
    
    
    public interface DataSearchNameSelectedEventHandler extends EventHandler {
        void onNameSelected(DataSearchNameSelectedEvent event);
    }

    public static final GwtEvent.Type<DataSearchNameSelectedEventHandler> TYPE = new GwtEvent.Type<DataSearchNameSelectedEventHandler>();

    private DiskResource resource;

    public DataSearchNameSelectedEvent(DiskResource dr) {
        setResource(dr);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<DataSearchNameSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DataSearchNameSelectedEventHandler handler) {
        handler.onNameSelected(this);

    }

    /**
     * @return the resource
     */
    public DiskResource getResource() {
        return resource;
    }

    /**
     * @param resource the resource to set
     */
    public void setResource(DiskResource resource) {
        this.resource = resource;
    }


}
