package org.iplantc.core.uidiskresource.client.search.presenter.impl;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;

import org.iplantc.core.uicommons.client.info.ErrorAnnouncementConfig;
import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;
import org.iplantc.core.uidiskresource.client.events.FolderSelectedEvent;
import org.iplantc.core.uidiskresource.client.events.FolderSelectedEvent.FolderSelectedEventHandler;
import org.iplantc.core.uidiskresource.client.events.FolderSelectedEvent.HasFolderSelectedEventHandlers;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter;
import org.iplantc.core.uidiskresource.client.search.views.DiskResourceSearchField;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

import java.util.Collections;
import java.util.List;

public class DataSearchPresenterImpl implements DataSearchPresenter {

    public final class InitShowHandler implements ShowHandler {
        private final TreeStore<Folder> handlerTreeStore;
        private HandlerRegistration hr;

        public InitShowHandler(TreeStore<Folder> treeStore) {
            this.handlerTreeStore = treeStore;
        }

        @Override
        public void onShow(ShowEvent event) {
            updateDataNavigationWindow(queryTemplates, handlerTreeStore);
            if (hr != null) {
                hr.removeHandler();
            }
        }

        public void setHandlerRegistration(HandlerRegistration hr) {
            this.hr = hr;
        }
    }

    TreeStore<Folder> treeStore;
    DiskResourceSearchField view;
    private DiskResourceQueryTemplate activeQuery = null;
    private final IplantAnnouncer announcer;
    private List<DiskResourceQueryTemplate> cleanCopyQueryTemplates;
    private HandlerManager handlerManager;
    private final List<DiskResourceQueryTemplate> queryTemplates = Lists.newArrayList();
    private final SearchServiceFacade searchService;

    @Inject
    public DataSearchPresenterImpl(final SearchServiceFacade searchService, final IplantAnnouncer announcer) {
        this.searchService = searchService;
        this.announcer = announcer;
    }

    @Override
    public HandlerRegistration addFolderSelectedEventHandler(FolderSelectedEventHandler handler) {
        return ensureHandlers().addHandler(FolderSelectedEvent.TYPE, handler);
    }

    /**
     * This handler is responsible for saving or updating the {@link DiskResourceQueryTemplate} contained
     * in the given {@link SaveDiskResourceQueryEvent}.
     * <p/>
     * This method will ensure that renaming of queries will not result in replacing the original query.
     * This method will apply a unique id to the given query template if it doesn't have one.
     * <p/>
     * After the query has been successfully saved, a search with the given querytemplate will be
     * performed.
     */
    @Override
    public void doSaveDiskResourceQueryTemplate(SaveDiskResourceQueryEvent event) {
        // Assume that once the filter is saved, a search should be performed.
        // Get query template
        final DiskResourceQueryTemplate queryTemplate = event.getQueryTemplate();

        String currId = Strings.nullToEmpty(queryTemplate.getId());
        if (Strings.isNullOrEmpty(currId)) {
            queryTemplate.setId(searchService.getUniqueId());
        }
        // If the given query template is already in the list, remove it.
        for (DiskResourceQueryTemplate hasId : ImmutableList.copyOf(getQueryTemplates())) {
            String inListId = hasId.getId();
            if (currId.equalsIgnoreCase(inListId)) {
                getQueryTemplates().remove(hasId);
                break;
            }
        }

        final ImmutableList<DiskResourceQueryTemplate> toBeSaved = ImmutableList.copyOf(Iterables.concat(queryTemplates, Collections.singletonList(queryTemplate)));
        // Call service to save template
        searchService.saveQueryTemplates(toBeSaved, new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig("Failed to save query template."));
            }

            // @Override
            // public void onSuccess(List<DiskResourceQueryTemplate> result) {
            @Override
            public void onSuccess(Boolean result) {
                // Clear list of saved query templates and re-add result.
                getQueryTemplates().clear();
                getQueryTemplates().addAll(toBeSaved);

                // Create immutable copy of saved templates
                setCleanCopyQueryTemplates(searchService.createFrozenList(toBeSaved));

                // Call our method to perform search with saved template
                doSubmitDiskResourceQuery(new SubmitDiskResourceQueryEvent(queryTemplate));
            }
        });

    }

    /**
     * This handler is responsible for submitting a search with the {@link DiskResourceQueryTemplate}
     * contained in the given {@link SubmitDiskResourceQueryEvent}.
     * <p/>
     * Additionally, this method also ensures that this presenter's query template collection is also maintained in the
     * {@link DiskResourceView#getTreeStore()}, and is responsible for setting the current "active query".
     */
    @Override
    public void doSubmitDiskResourceQuery(final SubmitDiskResourceQueryEvent event) {
        DiskResourceQueryTemplate toSubmit = event.getQueryTemplate();

        List<DiskResourceQueryTemplate> toUpdate;
        // If we don't have the query in our collection
        final boolean isNewQuery = Strings.isNullOrEmpty(toSubmit.getId());
        if (isNewQuery) {
            // Give the new query an id.
            toSubmit.setId(searchService.getUniqueId());
            toUpdate = Lists.newArrayList(getQueryTemplates());
            toUpdate.add(toSubmit);
        } else {
            toUpdate = Lists.newArrayList();
            // If it is an existing query, determine if it is dirty. If so, set dirty flag
            if (templateHasChanged(toSubmit, getCleanCopyQueryTemplates())) {
                toSubmit.setDirty(true);
                // Replace existing object in current template list
                for (DiskResourceQueryTemplate qt : getQueryTemplates()) {
                    if (qt.getId().equalsIgnoreCase(toSubmit.getId())) {
                        toUpdate.add(toSubmit);
                    } else {
                        toUpdate.add(qt);
                    }
                }
                getQueryTemplates().clear();
                getQueryTemplates().addAll(toUpdate);
            } else {
                toUpdate = Lists.newArrayList(getQueryTemplates());
            }
        }

        // Performing a search has the effect of setting the given query as the current active query.
        updateDataNavigationWindow(toUpdate, treeStore);

        activeQuery = toSubmit;
        fireEvent(new FolderSelectedEvent(activeQuery));
    }

    @Override
    public DiskResourceQueryTemplate getActiveQuery() {
        return activeQuery;
    }

    @Override
    public void onFolderSelected(FolderSelectedEvent event) {
        if (event.getSelectedFolder() instanceof DiskResourceQueryTemplate) {
            view.edit((DiskResourceQueryTemplate)event.getSelectedFolder());
        } else {
            // Clear search form
            view.clearSearch();
        }
    }

    @Override
    public void searchInit(final HasFolderSelectedEventHandlers hasFolderSelectedHandlers, final FolderSelectedEventHandler folderSelectedHandler,
            final TreeStore<Folder> treeStore,
            final DiskResourceSearchField view) {
        hasFolderSelectedHandlers.addFolderSelectedEventHandler(this);
        // Add handler which will listen to our FolderSelectedEvents
        addFolderSelectedEventHandler(folderSelectedHandler);
        this.treeStore = treeStore;
        this.view = view;
        view.addSaveDiskResourceQueryEventHandler(this);
        view.addSubmitDiskResourceQueryEventHandler(this);

        // Retrieve any saved searches.
        searchService.getSavedQueryTemplates(new AsyncCallback<List<DiskResourceQueryTemplate>>() {

            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(SafeHtmlUtils.fromString("Failed to retrieve saved filters"), true));
            }

            @Override
            public void onSuccess(List<DiskResourceQueryTemplate> result) {
                // Save result
                getQueryTemplates().clear();
                for (DiskResourceQueryTemplate qt : result) {
                    qt.setId(searchService.getUniqueId());
                    getQueryTemplates().add(qt);
                }
                setCleanCopyQueryTemplates(searchService.createFrozenList(getQueryTemplates()));

                // Update navigation window
                updateDataNavigationWindow(queryTemplates, treeStore);
            }
        });

    }

    boolean areTemplatesEqual(DiskResourceQueryTemplate lhs, DiskResourceQueryTemplate rhs) {
        Splittable lhsSplittable = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(lhs));
        Splittable rhsSplittable = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(rhs));

        String payloadLHS = lhsSplittable.getPayload();
        String payloadRHS = rhsSplittable.getPayload();
        return payloadLHS.equals(payloadRHS);
    }

    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }

    List<DiskResourceQueryTemplate> getCleanCopyQueryTemplates() {
        return cleanCopyQueryTemplates;
    }

    HandlerManager getHandlerManager() {
        return handlerManager;
    }


    List<DiskResourceQueryTemplate> getQueryTemplates() {
        return queryTemplates;
    }

    void setCleanCopyQueryTemplates(List<DiskResourceQueryTemplate> cleanCopyQueryTemplates) {
        this.cleanCopyQueryTemplates = cleanCopyQueryTemplates;
    }

    /**
     * Ensures that the navigation window shows the given templates.
     * These show up in the navigation window as "magic folders".
     * <p/>
     * This method ensures that the only the given list of queryTemplates will be displayed in the
     * navigation pane.
     * 
     * 
     * Only objects which are instances of {@link DiskResourceQueryTemplate} will be operated on. Items
     * which can't be found in the tree store will be added, and items which are already in the store and
     * are marked as dirty will be updated.
     * 
     * @param queryTemplates
     * @param treeStore
     */
    void updateDataNavigationWindow(final List<DiskResourceQueryTemplate> queryTemplates, final TreeStore<Folder> treeStore) {
        for (DiskResourceQueryTemplate qt : queryTemplates) {
            // If the item already exists in the store and the template is dirty, update it
            if (treeStore.findModelWithKey(qt.getId()) != null) {
                if (qt.isDirty()) {
                    treeStore.update(qt);
                }
            } else {
                treeStore.add(qt);
            }
        }
    }

    private boolean templateHasChanged(DiskResourceQueryTemplate template, List<DiskResourceQueryTemplate> controlList) {
        for (DiskResourceQueryTemplate qt : controlList) {
            if (qt.getId().equalsIgnoreCase(template.getId()) && !areTemplatesEqual(qt, template)) {
                // Given template has been changed
                return true;
            }
        }
        return false;
    }

    @Override
    public void loadSavedQueries(List<DiskResourceQueryTemplate> savedQueries) {
        // Save result
        getQueryTemplates().clear();
        for (DiskResourceQueryTemplate qt : savedQueries) {
            qt.setId(searchService.getUniqueId());
            getQueryTemplates().add(qt);
        }
        setCleanCopyQueryTemplates(searchService.createFrozenList(getQueryTemplates()));

        // Update navigation window
        updateDataNavigationWindow(queryTemplates, treeStore);
    }

}
