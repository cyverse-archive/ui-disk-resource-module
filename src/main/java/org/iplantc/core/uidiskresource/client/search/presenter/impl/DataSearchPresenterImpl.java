package org.iplantc.core.uidiskresource.client.search.presenter.impl;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.sencha.gxt.data.shared.TreeStore;
import org.iplantc.core.uicommons.client.info.ErrorAnnouncementConfig;
import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;
import org.iplantc.core.uicommons.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataSearchPresenterImpl implements DataSearchPresenter {

    DiskResourceView view;
    private final List<DiskResourceQueryTemplate> queryTemplates = Lists.newArrayList();
    private final SearchServiceFacade searchService;
    private final IplantAnnouncer announcer;
    private DiskResourceQueryTemplate activeQuery = null;

    @Inject
    public DataSearchPresenterImpl(final SearchServiceFacade searchService, final IplantAnnouncer announcer) {
        this.searchService = searchService;
        this.announcer = announcer;
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

        String currId = queryTemplate.getId();
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
        searchService.saveQueryTemplates(toBeSaved, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig("Failed to save query template."));
            }

            @Override
            public void onSuccess(String result) {
                // Only add query template to list of query templates if save was successful
                getQueryTemplates().add(queryTemplate);
                // Call our method to perform search with saved template
                doSubmitDiskResourceQuery(new SubmitDiskResourceQueryEvent(queryTemplate));
            }
        });

    }

    List<DiskResourceQueryTemplate> getQueryTemplates(){
        return queryTemplates;
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
        // Performing a search has the effect of setting the given query as the current active query.
        updateDataNavigationWindow(getQueryTemplates(), view.getTreeStore());
        searchService.submitSearchFromQueryTemplate(event.getQueryTemplate(), new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(SafeHtmlUtils.fromString("Submission of search failed"), true));
            }

            @Override
            public void onSuccess(String result) {

                // Set active query
                activeQuery = event.getQueryTemplate();
                // TODO CORE-4876: Ensure that the current folder is selected?
            }

        });

    }



    @Override
    public DiskResourceView getView() {
        return view;
    }

    @Override
    public DiskResourceQueryTemplate getActiveQuery() {
        return activeQuery;
    }

    @Override
    public void searchInit(final DiskResourceView view) {
        this.view = view;
        view.getToolbar().addSaveDiskResourceQueryTemplateEventHandler(this);
        view.getToolbar().addSubmitDiskResourceQueryEventHandler(this);


        // Retrieve any saved searches.
        searchService.getSavedQueryTemplates(new AsyncCallback<List<DiskResourceQueryTemplate>>() {

            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(SafeHtmlUtils.fromString("Failed to retrieve saved filters"), true));
            }

            @Override
            public void onSuccess(List<DiskResourceQueryTemplate> result) {
                // Save result
                queryTemplates.clear();
                queryTemplates.addAll(result);

                // Update navigation window
                updateDataNavigationWindow(queryTemplates, view.getTreeStore());
            }
        });

    }

    /**
     * Ensures that the navigation window shows the given templates.
     * These show up in the navigation window as "magic folders".
     * <p/>
     * This method ensures that the only the given list of queryTemplates will be displayed in the
     * navigation pane.
     *
     * Only objects which are instances of {@link DiskResourceQueryTemplate} will be operated on.
     *
     *
     * @param queryTemplates
     * @param treeStore
     */
    private void updateDataNavigationWindow(List<DiskResourceQueryTemplate> queryTemplates, TreeStore<Folder> treeStore) {
        // Clean prev query template roots
        HashSet<Folder> rootItemsSet = Sets.newHashSet(treeStore.getRootItems());
        // Create set of current root items which only contains query templates
        Set<Folder> filteredRootItems = Sets.filter(rootItemsSet, new Predicate<Folder>() {

            @Override
            public boolean apply(Folder input) {
                return input instanceof DiskResourceQueryTemplate;
            }
        });

        HashSet<String> curRootIdSet = Sets.newHashSet(DiskResourceUtil.asStringIdList(filteredRootItems));
        HashSet<String> qtIdSet = Sets.newHashSet(DiskResourceUtil.asStringIdList(queryTemplates));

        // Get set of items which need to be added to the tree store
        SetView<String> qtMinusCurr = Sets.difference(qtIdSet, curRootIdSet);

        // Get set of items which need to be removed from the tree store
        SetView<String> currMinusQt = Sets.intersection(curRootIdSet, qtIdSet);

        // Remove searchItems from store
        for (Folder rootItem : treeStore.getRootItems()) {
            if (currMinusQt.contains(rootItem.getId())) {
                treeStore.remove(rootItem);
            }
        }
        for (DiskResourceQueryTemplate qt : queryTemplates) {
            if (qtMinusCurr.contains(qt.getId())) {
                treeStore.add(qt);
            }
            // Re-add the items which were removed
            if(currMinusQt.contains(qt.getId())){
                treeStore.add(qt);
            }
        }

    }

}
