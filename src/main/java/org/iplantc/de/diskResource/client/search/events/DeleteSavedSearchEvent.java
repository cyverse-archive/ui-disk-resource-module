package org.iplantc.de.diskResource.client.search.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.diskResource.client.search.events.DeleteSavedSearchEvent.DeleteSavedSearchEventHandler;

public class DeleteSavedSearchEvent extends GwtEvent<DeleteSavedSearchEventHandler> {

    public interface DeleteSavedSearchEventHandler extends EventHandler {
        void onDeleteSavedSearch(DeleteSavedSearchEvent deleteSavedSearchEvent);
    }

    public static interface HasDeleteSavedSearchEventHandlers {
        HandlerRegistration addDeleteSavedSearchEventHandler(DeleteSavedSearchEventHandler handler);
    }

    public static final GwtEvent.Type<DeleteSavedSearchEventHandler> TYPE = new GwtEvent.Type<DeleteSavedSearchEventHandler>();
    private final DiskResourceQueryTemplate savedSearch;

    public DeleteSavedSearchEvent(DiskResourceQueryTemplate savedSearch) {
        this.savedSearch = savedSearch;
    }

    @Override
    public GwtEvent.Type<DeleteSavedSearchEventHandler> getAssociatedType() {
        return TYPE;
    }

    public DiskResourceQueryTemplate getSavedSearch() {
        return savedSearch;
    }

    @Override
    protected void dispatch(DeleteSavedSearchEventHandler handler) {
        handler.onDeleteSavedSearch(this);
    }
}
