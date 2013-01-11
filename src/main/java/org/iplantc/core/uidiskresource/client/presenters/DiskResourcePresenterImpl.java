package org.iplantc.core.uidiskresource.client.presenters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.core.uidiskresource.client.DiskResourceDisplayStrings;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.events.DataSearchHistorySelectedEvent;
import org.iplantc.core.uidiskresource.client.events.DataSearchNameSelectedEvent;
import org.iplantc.core.uidiskresource.client.events.DataSearchNameSelectedEvent.DataSearchNameSelectedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DataSearchPathSelectedEvent;
import org.iplantc.core.uidiskresource.client.events.DataSearchPathSelectedEvent.DataSearchPathSelectedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DiskResourceRenamedEvent;
import org.iplantc.core.uidiskresource.client.events.DiskResourceRenamedEvent.DiskResourceRenamedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DiskResourceSelectedEvent;
import org.iplantc.core.uidiskresource.client.events.DiskResourceSelectedEvent.DiskResourceSelectedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DiskResourcesDeletedEvent;
import org.iplantc.core.uidiskresource.client.events.DiskResourcesDeletedEvent.DiskResourcesDeletedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DiskResourcesMovedEvent;
import org.iplantc.core.uidiskresource.client.events.DiskResourcesMovedEvent.DiskResourcesMovedEventHandler;
import org.iplantc.core.uidiskresource.client.events.FolderCreatedEvent;
import org.iplantc.core.uidiskresource.client.events.FolderCreatedEvent.FolderCreatedEventHandler;
import org.iplantc.core.uidiskresource.client.events.RequestBulkDownloadEvent;
import org.iplantc.core.uidiskresource.client.events.RequestBulkUploadEvent;
import org.iplantc.core.uidiskresource.client.events.RequestImportFromUrlEvent;
import org.iplantc.core.uidiskresource.client.events.RequestSimpleDownloadEvent;
import org.iplantc.core.uidiskresource.client.events.RequestSimpleUploadEvent;
import org.iplantc.core.uidiskresource.client.events.ShowFilePreviewEvent;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceInfo;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceMetadata;
import org.iplantc.core.uidiskresource.client.models.autobeans.File;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.search.models.DataSearch;
import org.iplantc.core.uidiskresource.client.search.models.DataSearchAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.search.models.DataSearchResult;
import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uidiskresource.client.services.callbacks.CreateFolderCallback;
import org.iplantc.core.uidiskresource.client.services.callbacks.DiskResourceDeleteCallback;
import org.iplantc.core.uidiskresource.client.services.callbacks.DiskResourceMetadataUpdateCallback;
import org.iplantc.core.uidiskresource.client.services.callbacks.DiskResourceMoveCallback;
import org.iplantc.core.uidiskresource.client.services.callbacks.RenameDiskResourceCallback;
import org.iplantc.core.uidiskresource.client.sharing.views.DataSharingDialog;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.views.DiskResourceSearchView;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.metadata.DiskResourceMetadataDialog;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbarImpl;
import org.iplantc.core.uidiskresource.client.events.DataSearchHistorySelectedEvent.DataSearchHistorySelectedEventHandler;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;

/**
 * 
 * @author jstroot
 * 
 */
public class DiskResourcePresenterImpl implements DiskResourceView.Presenter,
        DiskResourceViewToolbarImpl.Presenter, HasHandlerRegistrationMgmt {

    private final class DetailsCallbackImpl implements AsyncCallback<String> {
        private final String path;

        private DetailsCallbackImpl(String path) {
            this.path = path;
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(I18N.ERROR.retrieveStatFailed(), caught);

        }

        @Override
        public void onSuccess(String result) {
            JSONObject json = JsonUtil.getObject(result);
            JSONObject pathsObj = JsonUtil.getObject(json, "paths");
            JSONObject details = JsonUtil.getObject(pathsObj, path);
            AutoBean<DiskResourceInfo> bean = AutoBeanCodex.decode(drFactory, DiskResourceInfo.class,
                    details.toString());
            view.updateDetails(path, bean.as());
        }
    }

    private final class DiskResourceMovedEventHandlerImpl implements DiskResourcesMovedEventHandler {
        @Override
        public void onDiskResourcesMoved(DiskResourcesMovedEvent event) {
            // Determine which folder is the ancestor, then refresh it
            if (DiskResourceUtil.isDescendantOfFolder(event.getDestinationFolder(), getSelectedFolder())) {
                view.refreshFolder(event.getDestinationFolder());
            } else {
                view.refreshFolder(getSelectedFolder());
            }
        }
    }

    private final class DiskResourceSelectedEventHandlerImpl implements DiskResourceSelectedEventHandler {
        @Override
        public void onSelect(DiskResourceSelectedEvent event) {
            if (event.getSelectedItem() instanceof Folder) {
                view.setSelectedFolder((Folder)event.getSelectedItem());
            } else if (event.getSelectedItem() instanceof File) {
                EventBus.getInstance().fireEvent(
                        new ShowFilePreviewEvent((File)event.getSelectedItem(), this));
            }
        }
    }

    private final class DiskResourcesDeletedEventHandlerImpl implements DiskResourcesDeletedEventHandler {
        private final DiskResourceView view;

        public DiskResourcesDeletedEventHandlerImpl(final DiskResourceView view) {
            this.view = view;
        }

        @Override
        public void onDiskResourcesDeleted(Collection<DiskResource> resources) {
            view.removeDiskResources(resources);
        }

    }

    private final DiskResourceView view;
    private final DiskResourceView.Proxy proxy;
    private final TreeLoader<Folder> treeLoader;
    private final HashMap<EventHandler, HandlerRegistration> registeredHandlers = new HashMap<EventHandler, HandlerRegistration>();
    private DiskResourceServiceFacade diskResourceService;
    private final DiskResourceDisplayStrings DISPLAY;
    private final DiskResourceAutoBeanFactory drFactory;
    private final Builder builder;
    private final DataSearchAutoBeanFactory dataSearchFactory;
    private List<String> searchHistory;
    private String currentSearchTerm;

    @Inject
    public DiskResourcePresenterImpl(final DiskResourceView view, final DiskResourceView.Proxy proxy,
            final DiskResourceServiceFacade diskResourceService,
            final DiskResourceDisplayStrings display, final DiskResourceAutoBeanFactory factory,
            final DataSearchAutoBeanFactory dataSearchFactory) {
        this.view = view;
        this.proxy = proxy;
        this.diskResourceService = diskResourceService;
        this.DISPLAY = display;
        this.drFactory = factory;
        this.dataSearchFactory = dataSearchFactory;

        builder = new MyBuilder(this);

        initHandlers();
        initDragAndDrop();
        treeLoader = new TreeLoader<Folder>(this.proxy) {
            @Override
            public boolean hasChildren(Folder parent) {
                return parent.hasSubDirs();
            }
        };

        // Add selection handlers which will control the visibility of the toolbar buttons
        addFileSelectChangedHandler(new ToolbarButtonVisibilitySelectionHandler<DiskResource>(
                view.getToolbar(), view));
        addFolderSelectionHandler(new ToolbarButtonVisibilitySelectionHandler<Folder>(view.getToolbar(), view));

        treeLoader.addLoadHandler(new ChildTreeStoreBinding<Folder>(this.view.getTreeStore()));
        this.view.setTreeLoader(treeLoader);
        this.view.setPresenter(this);
        this.proxy.setPresenter(this);
        searchHistory = new ArrayList<String>();
        loadSearchHistory();
        loadUserTrashPath();
    }

    private void initDragAndDrop() {

    }

    @Override
    public void loadSearchHistory() {
        diskResourceService.getDataSearchHistory(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JsonUtil.getObject(result);
                if (obj != null) {
                    JSONArray arr = JsonUtil.getArray(obj, "data-search");
                    if (arr != null) {
                        for (int i = 0; i < arr.size(); i++) {
                            searchHistory.add((JsonUtil.trim(arr.get(i).isString().toString())));
                        }
                    }
                }
                view.renderSearchHistory(searchHistory);
            }

        });

    }

    @Override
    public void loadUserTrashPath() {
        final String userName = UserInfo.getInstance().getUsername();
        diskResourceService.getUserTrashPath(userName, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                // best guess of user name. this is horrible
                UserInfo.getInstance().setTrashPath("/iplant/trash/home/rods/" + userName);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JsonUtil.getObject(result);
                UserInfo.getInstance().setTrashPath(JsonUtil.getString(obj, "trash"));
            }
        });
    }

    private void initHandlers() {
        EventBus eventBus = EventBus.getInstance();

        DiskResourcesDeletedEventHandlerImpl diskResourcesDeletedHandler = new DiskResourcesDeletedEventHandlerImpl(
                view);
        eventBus.addHandler(DiskResourcesDeletedEvent.TYPE, diskResourcesDeletedHandler);
        eventBus.addHandler(FolderCreatedEvent.TYPE, new FolderCreatedEventHandler() {

            @Override
            public void onFolderCreated(Folder parentFolder, Folder newFolder) {
                view.addFolder(parentFolder, newFolder);
            }
        });
        eventBus.addHandler(DiskResourceRenamedEvent.TYPE, new DiskResourceRenamedEventHandler() {

            @Override
            public void onRename(DiskResource originalDr, DiskResource newDr) {
                view.updateDiskResource(originalDr, newDr);

            }
        });
        eventBus.addHandler(DiskResourceSelectedEvent.TYPE, new DiskResourceSelectedEventHandlerImpl());
        eventBus.addHandler(DiskResourcesMovedEvent.TYPE, new DiskResourceMovedEventHandlerImpl());

        eventBus.addHandler(DataSearchNameSelectedEvent.TYPE, new DataSearchNameSelectedEventHandler() {

            @Override
            public void onNameSelected(DataSearchNameSelectedEvent event) {
                handleSearchEvent(event.getResource());
            }

        });

        eventBus.addHandler(DataSearchPathSelectedEvent.TYPE, new DataSearchPathSelectedEventHandler() {

            @Override
            public void onPathSelected(DataSearchPathSelectedEvent event) {
                handleSearchEventByPath(event.getResource().getPath());
            }

        });
        eventBus.addHandler(DataSearchHistorySelectedEvent.TYPE,
                new DataSearchHistorySelectedEventHandler() {

                    @Override
                    public void onSelection(DataSearchHistorySelectedEvent event) {
                        String searchHistoryTerm = event.getSearchHistoryTerm();
                        view.getToolbar().setSearchTerm(searchHistoryTerm);
                        doSearch(searchHistoryTerm);

                    }

                });

    }

    private void handleSearchEvent(DiskResource resource) {
        if (resource instanceof Folder) {
            Folder f = (Folder)resource;
            view.setSelectedFolder(f);
            onFolderSelected(f);
        } else {
            EventBus.getInstance().fireEvent(new ShowFilePreviewEvent((File)resource, this));
        }
        addToSearchHistory(getCurrentSearchTerm());
    }

    private void handleSearchEventByPath(String path) {
        setSelectedFolderById(path);
        addToSearchHistory(getCurrentSearchTerm());
    }

    @Override
    public DiskResourceView getView() {
        return view;
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);
        // JDS May need to call doRefresh here.
    }

    @Override
    public Folder getSelectedFolder() {
        return view.getSelectedFolder();
    }

    @Override
    public Set<DiskResource> getSelectedDiskResources() {
        return view.getSelectedDiskResources();
    }

    @Override
    public void onFolderSelected(Folder folder) {
        view.showDataListingWidget();
        view.deSelectDiskResources();
        if (view.isLoaded(folder)) {
            Set<DiskResource> children = Sets.newHashSet();
            if (folder.getFolders() != null) {
                children.addAll(folder.getFolders());
            }
            if (folder.getFiles() != null) {
                children.addAll(folder.getFiles());
            }
            view.setDiskResources(children);
        } else {
            treeLoader.load(folder);
        }
    }

    @Override
    public void onDiskResourceSelected(Set<DiskResource> selection) {
        if (selection != null && selection.size() == 1) {
            Iterator<DiskResource> it = selection.iterator();
            getDetails(it.next());
        } else {
            view.resetDetailsPanel();
        }

    }

    private void getDetails(DiskResource resource) {
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        final String path = resource.getId();
        arr.set(0, new JSONString(path));
        obj.put("paths", arr);
        diskResourceService.getStat(obj.toString(), new DetailsCallbackImpl(path));

    }

    @Override
    public void onFolderLoad(Folder loadedFolder, Set<DiskResource> folderChildren) {
        if ((getSelectedFolder() != null) && getSelectedFolder().equals(loadedFolder)) {
            view.setDiskResources(folderChildren);
        }
    }

    @Override
    public void doBulkUpload() {
        EventBus.getInstance().fireEvent(new RequestBulkUploadEvent(this, getSelectedFolder()));
    }

    @Override
    public void doSimpleUpload() {
        EventBus.getInstance().fireEvent(new RequestSimpleUploadEvent(this, getSelectedFolder()));
    }

    @Override
    public void doImport() {
        EventBus.getInstance().fireEvent(new RequestImportFromUrlEvent(this, getSelectedFolder()));
    }

    @Override
    public void doCreateNewFolder(final Folder parentFolder, final String newFolderName) {
        view.mask(DISPLAY.loadingMask());
        diskResourceService.createFolder(parentFolder, newFolderName, new CreateFolderCallback(
                parentFolder, view, newFolderName));
    }

    @Override
    public void doRefresh() {
        view.refreshFolder(getSelectedFolder());
    }

    @Override
    public void doSimpleDownload() {
        EventBus.getInstance().fireEvent(
                new RequestSimpleDownloadEvent(this, getSelectedDiskResources()));
    }

    @Override
    public void doBulkDownload() {
        EventBus.getInstance().fireEvent(new RequestBulkDownloadEvent(this, getSelectedDiskResources()));
    }

    @Override
    public void doRename(final DiskResource dr, final String newName) {
        view.mask(DISPLAY.loadingMask());
        diskResourceService.renameDiskResource(dr, newName, new RenameDiskResourceCallback(dr, view,
                drFactory));
    }

    @Override
    public void doShare() {
        DataSharingDialog dlg = new DataSharingDialog(getSelectedDiskResources());
        dlg.show();
    }

    @Override
    public void requestDelete() {
        // TODO JDS Need to give feedback to user if there are selections in both the nav and the main.
        final ConfirmMessageBox mb = new ConfirmMessageBox(DISPLAY.deleteFilesTitle(),
                DISPLAY.deleteFilesMsg());
        mb.addHideHandler(new HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                if (mb.getHideButton() == mb.getButtonById(PredefinedButton.YES.name())) {
                    doDelete();
                }
            }
        });
        mb.show();
    }

    @Override
    public void doDelete() {
        if (!getSelectedDiskResources().isEmpty()
                && DiskResourceUtil.isOwner(getSelectedDiskResources())) {
            view.mask(DISPLAY.loadingMask());

            HashSet<DiskResource> drSet = Sets.newHashSet(getSelectedDiskResources());
            diskResourceService.deleteDiskResources(drSet, new DiskResourceDeleteCallback(drSet, view));

        } else if ((getSelectedFolder() != null) && DiskResourceUtil.isOwner(getSelectedFolder())) {
            view.mask(DISPLAY.loadingMask());
            HashSet<DiskResource> drSet = Sets.newHashSet((DiskResource)getSelectedFolder());
            diskResourceService.deleteDiskResources(drSet, new DiskResourceDeleteCallback(drSet, view));
        }
    }

    @Override
    public void doMetadata() {
        if (getSelectedDiskResources().size() == 1) {
            DiskResourceMetadataDialog dlg = new DiskResourceMetadataDialog(getSelectedDiskResources()
                    .iterator().next(), this);
            dlg.show();
        }

    }

    @Override
    public void doDataQuota() {
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doDataQuota");

    }

    @Override
    public void addFileSelectChangedHandler(SelectionChangedHandler<DiskResource> selectionChangedHandler) {
        view.addDiskResourceSelectChangedHandler(selectionChangedHandler);
    }

    @Override
    public void addFolderSelectionHandler(SelectionHandler<Folder> selectionHandler) {
        view.addFolderSelectionHandler(selectionHandler);
    }

    @Override
    public void setSelectedFolderById(final String folderId) {
        if (Strings.isNullOrEmpty(folderId)) {
            return;
        }
        // Create and add the SelectFolderByIdLoadHandler to the treeLoader.
        SelectFolderByIdLoadHandler handler = new SelectFolderByIdLoadHandler(folderId, this, view);
        HandlerRegistration reg = treeLoader.addLoadHandler(handler);
        addEventHandlerRegistration(handler, reg);

        ArrayList<HandlerRegistration> regList = Lists.newArrayList();
        regList.add(reg);
        doRefresh();
    }

    @Override
    public void setSelectedDiskResourcesById(Set<String> diskResourceIdList) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterHandler(EventHandler handler) {
        if (registeredHandlers.containsKey(handler)) {
            registeredHandlers.remove(handler).removeHandler();
        }
    }

    @Override
    public void addEventHandlerRegistration(EventHandler handler, HandlerRegistration reg) {
        registeredHandlers.put(handler, reg);
    }

    @Override
    public void getDiskResourceMetadata(DiskResource resource, AsyncCallback<String> callback) {
        diskResourceService.getDiskResourceMetaData(resource, callback);
    }

    @Override
    public void setDiskResourceMetaData(DiskResource resource, Set<DiskResourceMetadata> metadataToAdd,
            Set<DiskResourceMetadata> metadataToDelete,
            DiskResourceMetadataUpdateCallback diskResourceMetadataUpdateCallback) {
        diskResourceService.setDiskResourceMetaData(resource, metadataToAdd, metadataToDelete,
                diskResourceMetadataUpdateCallback);
    }

    @Override
    public boolean canDragDataToTargetFolder(final Folder targetFolder,
            final Collection<DiskResource> dropData) {
        // Assuming that ownership is of no concern.
        for (DiskResource dr : dropData) {
            // if the resource is a direct child of target folder
            if (DiskResourceUtil.isChildOfFolder(targetFolder, dr)) {
                return false;
            }
            if (dr instanceof Folder) {

                // cannot drag an ancestor (parent, grandparent, etc) onto a child and/or descendant
                if (DiskResourceUtil.isDescendantOfFolder(targetFolder, (Folder)dr)) {
                    return false;
                }

            } else if (dr instanceof File) {

            }
        }
        return true;
    }

    @Override
    public void doMoveDiskResources(Folder targetFolder, Set<DiskResource> resources) {
        diskResourceService.moveDiskResources(resources, targetFolder, new DiskResourceMoveCallback(
                view, targetFolder, resources));
    }

    @Override
    public Folder getDropTargetFolder(IsWidget target, Element eventTargetElement) {
        Folder ret = null;
        if (view.isViewTree(target) && (view.findTreeNode(eventTargetElement) != null)) {
            TreeNode<Folder> targetTreeNode = view.findTreeNode(eventTargetElement);
            ret = targetTreeNode.getModel();
        } else if (view.isViewGrid(target)) {
            Element targetRow = view.findGridRow(target.asWidget().getElement()).cast();

            if (targetRow != null) {
                int dropIndex = view.findRowIndex(targetRow);
                // TODO JDS Do some type checking here. got errors last time for casting file to folder
                DiskResource selDiskResource = view.getListStore().get(dropIndex);
                ret = (selDiskResource instanceof Folder) ? (Folder)selDiskResource : null;
            }
        }
        return ret;
    }

    @Override
    public boolean isViewGrid(IsWidget widget) {
        return view.isViewGrid(widget);
    }

    @Override
    public boolean isViewTree(IsWidget widget) {
        return view.isViewTree(widget);
    }

    @Override
    public Builder builder() {
        return builder;
    }

    private class MyBuilder implements Builder {

        private final DiskResourceView.Presenter presenter;

        public MyBuilder(DiskResourceView.Presenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public void go(HasOneWidget container) {
            presenter.go(container);
        }

        @Override
        public Builder hideNorth() {
            presenter.getView().setNorthWidgetHidden(true);
            return this;
        }

        @Override
        public Builder hideWest() {
            presenter.getView().setWestWidgetHidden(true);
            return this;
        }

        @Override
        public Builder hideCenter() {
            presenter.getView().setCenterWidgetHidden(true);
            return this;
        }

        @Override
        public Builder hideEast() {
            presenter.getView().setEastWidgetHidden(true);
            return this;
        }

        @Override
        public Builder singleSelect() {
            presenter.getView().setSingleSelect();
            return this;
        }

        @Override
        public Builder disableDiskResourceHyperlink() {
            presenter.getView().disableDiskResourceHyperlink();
            return this;
        }

    }

    @Override
    public void doSearch(final String val) {
        diskResourceService.search(val, 50, null, new AsyncCallback<String>() {

            private DiskResourceSearchView searchView;

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);

            }

            @Override
            public void onSuccess(String result) {
                AutoBean<DataSearchResult> bean = AutoBeanCodex.decode(dataSearchFactory,
                        DataSearchResult.class, result);
                List<DiskResource> resources = new ArrayList<DiskResource>();
                for (DataSearch ds : bean.as().getSearchResults()) {
                    if (ds.getType().equalsIgnoreCase("file")) {
                        File f = buildDummyFile(ds);
                        resources.add(f);
                    } else {
                        Folder fo = buildDummyFolder(ds);
                        resources.add(fo);
                    }

                }

                if (searchView == null) {
                    searchView = new DiskResourceSearchView();
                }
                view.deSelectNavigationFolder();
                searchView.loadResults(resources);
                view.showSearchResultWidget(searchView.asWidget());
                setCurrentSearchTerm(val);
            }

            private Folder buildDummyFolder(DataSearch ds) {
                AutoBean<Folder> folder = AutoBeanCodex.decode(drFactory, Folder.class, "{}");
                Folder fo = folder.as();
                fo.setId(ds.getId());
                fo.setName(ds.getName());
                fo.setPath(ds.getId());
                return fo;
            }

            private File buildDummyFile(DataSearch ds) {
                AutoBean<File> file = AutoBeanCodex.decode(drFactory, File.class, "{}");
                File f = file.as();
                f.setId(ds.getId());
                f.setName(ds.getName());
                f.setPath(DiskResourceUtil.parseParent(ds.getId()));
                return f;
            }
        });

    }

    @Override
    public void deSelectDiskResources() {
        view.deSelectDiskResources();
    }

    @Override
    public void addToSearchHistory(String searchTerm) {
        if (!searchHistory.contains(searchTerm)) {
            searchHistory.add(searchTerm);
        }

        saveSearchHistory();
        view.renderSearchHistory(searchHistory);
    }

    @Override
    public void removeFromSearchHistory(String searchTerm) {
        if (searchHistory.contains(searchTerm)) {
            searchHistory.remove(searchTerm);
        }
        saveSearchHistory();
        view.renderSearchHistory(searchHistory);
    }

    private JSONObject getSearchHistoryAsJson() {
        JSONObject obj = new JSONObject();
        if (searchHistory.size() > 0) {
            obj.put("data-search", JsonUtil.buildArrayFromStrings(searchHistory));
        } else {
            obj.put("data-search", new JSONArray());
        }
        return obj;
    }

    @Override
    public void saveSearchHistory() {
        JSONObject obj = getSearchHistoryAsJson();
        diskResourceService.saveDataSearchHistory(obj.toString(), new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.searchHistoryError(), caught);

            }

            @Override
            public void onSuccess(String result) {
                // do nothing

            }
        });
    }

    @Override
    public String getCurrentSearchTerm() {
        return currentSearchTerm;
    }

    @Override
    public void setCurrentSearchTerm(String searchTerm) {
        this.currentSearchTerm = searchTerm;

    }

    @Override
    public void emptyTrash() {
        diskResourceService.emptyTrash(UserInfo.getInstance().getUsername(),
                new AsyncCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        Folder f = view.getFolderById(UserInfo.getInstance().getTrashPath());
                        if (f != null) {
                            view.refreshFolder(f);
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);
                    }
                });

    }

    @Override
    public void restore() {
        Iterator<DiskResource> it = getSelectedDiskResources().iterator();
        JSONObject obj = new JSONObject();
        JSONArray pathArr = new JSONArray();
        int i = 0;
        while (it.hasNext()) {
            DiskResource r = it.next();
            pathArr.set(i++, new JSONString(r.getId()));
        }
        obj.put("paths", pathArr);

        diskResourceService.restoreDiskResource(obj, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);

            }

            @Override
            public void onSuccess(String result) {
                view.removeDiskResources(getSelectedDiskResources());
            }
        });
    }

}
