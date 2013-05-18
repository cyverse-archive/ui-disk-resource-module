package org.iplantc.core.uidiskresource.client.presenters.handlers;

import org.iplantc.core.uidiskresource.client.events.DataSearchHistorySelectedEvent;
import org.iplantc.core.uidiskresource.client.events.DataSearchHistorySelectedEvent.DataSearchHistorySelectedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DataSearchNameSelectedEvent;
import org.iplantc.core.uidiskresource.client.events.DataSearchNameSelectedEvent.DataSearchNameSelectedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DataSearchPathSelectedEvent;
import org.iplantc.core.uidiskresource.client.events.DataSearchPathSelectedEvent.DataSearchPathSelectedEventHandler;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

public final class DataSearchHandler implements DataSearchNameSelectedEventHandler, DataSearchPathSelectedEventHandler, DataSearchHistorySelectedEventHandler {
    private final DiskResourceView.Presenter presenter;

    public DataSearchHandler(DiskResourceView.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onNameSelected(DataSearchNameSelectedEvent event) {
        presenter.handleSearchEvent(event.getResource());
    }
    
    @Override
    public void onPathSelected(DataSearchPathSelectedEvent event) {
        presenter.handleSearchEvent(event.getResource());
    }
    
    @Override
    public void onSelection(DataSearchHistorySelectedEvent event) {
        String searchHistoryTerm = event.getSearchHistoryTerm();
        presenter.getView().getToolbar().setSearchTerm(searchHistoryTerm);
        presenter.doSearch(searchHistoryTerm);
    }
}