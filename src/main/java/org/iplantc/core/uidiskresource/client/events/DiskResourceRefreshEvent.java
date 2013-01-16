package org.iplantc.core.uidiskresource.client.events;

import java.util.List;

import org.iplantc.core.uidiskresource.client.events.DiskResourceRefreshEvent.DiskResourceRefreshEventHandler;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class DiskResourceRefreshEvent extends GwtEvent<DiskResourceRefreshEventHandler> {

    public interface DiskResourceRefreshEventHandler extends EventHandler {
        void onRefresh(DiskResourceRefreshEvent mdre);
    }

    public static final GwtEvent.Type<DiskResourceRefreshEventHandler> TYPE = new GwtEvent.Type<DiskResourceRefreshEventHandler>();

    private final String currentFolderId;
    private String tag;
    private List<DiskResource> resources;

    public DiskResourceRefreshEvent(String tag, String currentFolderId, List<DiskResource> resources) {
        this.currentFolderId = currentFolderId;
        this.setTag(tag);
        this.setResources(resources);
    }

    @Override
    protected void dispatch(DiskResourceRefreshEventHandler arg0) {
        arg0.onRefresh(this);

    }

    @Override
    public GwtEvent.Type<DiskResourceRefreshEventHandler> getAssociatedType() {
        return TYPE;
    }

    public String getCurrentFolderId() {
        return currentFolderId;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param resources the resources to set
     */
    public void setResources(List<DiskResource> resources) {
        this.resources = resources;
    }

    /**
     * @return the resources
     */
    public List<DiskResource> getResources() {
        return resources;
    }

}
