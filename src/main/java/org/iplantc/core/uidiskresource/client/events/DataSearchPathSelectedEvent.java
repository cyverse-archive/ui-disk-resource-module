/**
 * 
 */
package org.iplantc.core.uidiskresource.client.events;

import org.iplantc.core.uidiskresource.client.events.DataSearchPathSelectedEvent.DataSearchPathSelectedEventHandler;
import org.iplantc.core.uidiskresource.client.models.DiskResource;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author sriram
 *
 */
public class DataSearchPathSelectedEvent extends GwtEvent<DataSearchPathSelectedEventHandler> {

    public interface DataSearchPathSelectedEventHandler extends EventHandler {
        void onPathSelected(DataSearchPathSelectedEvent event);
    }

    public static final GwtEvent.Type<DataSearchPathSelectedEventHandler> TYPE = new GwtEvent.Type<DataSearchPathSelectedEventHandler>();

    private DiskResource resource;

    public DataSearchPathSelectedEvent(DiskResource dr) {
        setResource(dr);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<DataSearchPathSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DataSearchPathSelectedEventHandler handler) {
        handler.onPathSelected(this);
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
