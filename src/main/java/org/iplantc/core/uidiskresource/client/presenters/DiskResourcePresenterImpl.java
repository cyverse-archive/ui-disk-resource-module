package org.iplantc.core.uidiskresource.client.presenters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.ErrorDialog3;
import org.iplantc.core.uidiskresource.client.DiskResourceDisplayStrings;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.events.DiskResourceRenamedEvent;
import org.iplantc.core.uidiskresource.client.events.DiskResourceRenamedEvent.DiskResourceRenamedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DiskResourceSelectedEvent;
import org.iplantc.core.uidiskresource.client.events.DiskResourceSelectedEvent.DiskResourceSelectedEventHandler;
import org.iplantc.core.uidiskresource.client.events.DiskResourcesDeletedEvent;
import org.iplantc.core.uidiskresource.client.events.DiskResourcesDeletedEvent.DiskResourcesDeletedEventHandler;
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
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceMetadata;
import org.iplantc.core.uidiskresource.client.models.autobeans.File;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.presenters.callbacks.CreateFolderCallback;
import org.iplantc.core.uidiskresource.client.presenters.callbacks.DiskResourceDeleteCallback;
import org.iplantc.core.uidiskresource.client.presenters.callbacks.DiskResourceMetadataUpdateCallback;
import org.iplantc.core.uidiskresource.client.presenters.callbacks.RenameDiskResourceCallback;
import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.metadata.DiskResourceMetadataDialog;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbarImpl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * 
 * @author jstroot
 * 
 */
public class DiskResourcePresenterImpl implements DiskResourceView.Presenter,
        DiskResourceViewToolbarImpl.Presenter, HasHandlerRegistrationMgmt {

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

    @Inject
    public DiskResourcePresenterImpl(final DiskResourceView view, final DiskResourceView.Proxy proxy,
            final DiskResourceServiceFacade diskResourceService,
            final DiskResourceDisplayStrings display, final DiskResourceAutoBeanFactory factory) {
        this.view = view;
        this.proxy = proxy;
        this.diskResourceService = diskResourceService;
        this.DISPLAY = display;
        this.drFactory = factory;

        initHandlers();
        initDragAndDrop();
        treeLoader = new TreeLoader<Folder>(this.proxy) {
            @Override
            public boolean hasChildren(Folder parent) {
                return parent.hasSubDirs();
            }
        };

        // Add selection handlers which will control the visibility of the toolbar buttons
        addFolderSelectionHandler(new ToolbarButtonVisibilityFolderSelectionHandler(
                view.getToolbar()));
        addFileSelectChangedHandler(new ToolbarButtonVisibilityDiskResourceSelectionChangedHandler(
                view.getToolbar()));

        treeLoader.addLoadHandler(new ChildTreeStoreBinding<Folder>(this.view.getTreeStore()));
        this.view.setTreeLoader(treeLoader);
        this.view.setPresenter(this);
        this.proxy.setPresenter(this);
    }

    private void initDragAndDrop() {

    }

    private void initHandlers() {
        EventBus eventBus = EventBus.getInstance();

        DiskResourcesDeletedEventHandlerImpl diskResourcesDeletedHandler = new DiskResourcesDeletedEventHandlerImpl(view);
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
        eventBus.addHandler(DiskResourceSelectedEvent.TYPE, new DiskResourceSelectedEventHandler() {

            @Override
            public void onSelect(DiskResourceSelectedEvent event) {
                if (event.getSelectedItem() instanceof Folder) {
                    view.setSelectedFolder((Folder)event.getSelectedItem());
                } else if (event.getSelectedItem() instanceof File) {
                    EventBus.getInstance().fireEvent(
                            new ShowFilePreviewEvent((File)event.getSelectedItem(), this));
                }
            }
        });
    }

    @Override
    public DiskResourceView getView() {
        return view;
    }

    @Override
    public void go(HasOneWidget container) {
        go(container, false, false, false, false);
    }

    @Override
    public void go(HasOneWidget container, boolean hideWestWidget, boolean hideCenterWidget,
            boolean hideEastWidget, boolean hideNorthWidget) {
        view.setWestWidgetHidden(hideWestWidget);
        view.setCenterWidgetHidden(hideCenterWidget);
        view.setEastWidgetHidden(hideEastWidget);
        view.setNorthWidgetHidden(hideNorthWidget);
        container.setWidget(view);
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
        diskResourceService.createFolder(parentFolder, newFolderName,
                new CreateFolderCallback(parentFolder, view, newFolderName));
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
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doShare");
        ErrorDialog3 dlg = new ErrorDialog3(
                "Sample Err msg",
                "Sample Err Msg description asdffasdfasdfasdfasdfasdfasfasdfasfasdfasdfasdfasdfasdfasdfasdfasdfasfasdfasdfasdf\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nsadfasdfasdfasdfasdfasdf");
        dlg.show();
    }

    @Override
    public void requestDelete() {
        final ConfirmMessageBox mb = new ConfirmMessageBox(I18N.DISPLAY.deleteFilesTitle(),
                I18N.DISPLAY.deleteFilesMsg());
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
        if (!getSelectedDiskResources().isEmpty() && isDeletable(getSelectedDiskResources())) {
            view.mask(DISPLAY.loadingMask());

            HashSet<DiskResource> drSet = Sets.newHashSet(getSelectedDiskResources());
            diskResourceService.deleteDiskResources(drSet, new DiskResourceDeleteCallback(drSet, view));

        } else if ((getSelectedFolder() != null) && isDeletable(getSelectedFolder())) {
            view.mask(DISPLAY.loadingMask());
            HashSet<DiskResource> drSet = Sets.newHashSet((DiskResource)getSelectedFolder());
            diskResourceService.deleteDiskResources(drSet, new DiskResourceDeleteCallback(drSet, view));
        }
    }

    private boolean isDeletable(DiskResource resource) {
        return resource.getPermissions().isOwner();
    }

    private boolean isDeletable(Iterable<DiskResource> resources) {
        // Use predicate to determine if user is owner of all disk resources
        boolean isDeletable = true;
        for (DiskResource dr : resources) {
            if (!dr.getPermissions().isOwner()) {
                isDeletable = false;
                break;
            }
        }
        return isDeletable;
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

}
