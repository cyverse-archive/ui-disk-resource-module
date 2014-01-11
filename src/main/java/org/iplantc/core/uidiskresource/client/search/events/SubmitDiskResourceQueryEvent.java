package org.iplantc.core.uidiskresource.client.search.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler;

public class SubmitDiskResourceQueryEvent extends GwtEvent<SubmitDiskResourceQueryEventHandler> {

    public interface SubmitDiskResourceQueryEventHandler extends EventHandler {
        void doSubmitDiskResourceQuery(SubmitDiskResourceQueryEvent event);
    }

    public static interface HasSubmitDiskResourceQueryEventHandlers {
        HandlerRegistration addSubmitDiskResourceQueryEventHandler(SubmitDiskResourceQueryEventHandler handler);
    }

    public static final GwtEvent.Type<SubmitDiskResourceQueryEventHandler> TYPE = new GwtEvent.Type<SubmitDiskResourceQueryEventHandler>();
    private final DiskResourceQueryTemplate queryTemplate;

    public SubmitDiskResourceQueryEvent(DiskResourceQueryTemplate queryTemplate) {
        this.queryTemplate = queryTemplate;
    }

    @Override
    public GwtEvent.Type<SubmitDiskResourceQueryEventHandler> getAssociatedType() {
        return TYPE;
    }

    public DiskResourceQueryTemplate getQueryTemplate() {
        return queryTemplate;
    }

    @Override
    protected void dispatch(SubmitDiskResourceQueryEventHandler handler) {
        handler.doSubmitDiskResourceQuery(this);
    }

}
