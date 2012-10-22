package org.iplantc.core.uidiskresource.client.presenters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uidiskresource.client.DiskResourceDisplayStrings;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.events.FilesDeletedEvent;
import org.iplantc.core.uidiskresource.client.events.FilesDeletedEvent.FilesDeletedEventHandler;
import org.iplantc.core.uidiskresource.client.events.FoldersDeletedEvent;
import org.iplantc.core.uidiskresource.client.events.FoldersDeletedEvent.FoldersDeletedEventHandler;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.File;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.presenters.callbacks.DiskResourceDeleteCallback;
import org.iplantc.core.uidiskresource.client.presenters.callbacks.FileDeleteCallback;
import org.iplantc.core.uidiskresource.client.presenters.callbacks.FolderDeleteCallback;
import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.dialogs.FileSelectDialog;
import org.iplantc.core.uidiskresource.client.views.dialogs.FolderSelectDialog;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbarImpl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * Selection rules
 * 
 * -- Multi select folders/files
 * -- If file(s) are selected, folder will remain selected
 * -- Selecting a folder deselects anything in center panel.
 * -- Operations for the toolbar are based off of what was last selected.
 * 
 * 
 * 
 * @author jstroot
 * 
 */
public class DiskResourcePresenterImpl implements DiskResourceView.Presenter,
        DiskResourceViewToolbarImpl.Presenter, HasHandlerRegistrationMgmt {

    private final class DiskResourcesDeletedEventHandlerImpl implements FilesDeletedEventHandler, FoldersDeletedEventHandler {
        private final DiskResourceView view;

        public DiskResourcesDeletedEventHandlerImpl(final DiskResourceView view) {
            this.view = view;
        }

        @Override
        public void onFilesDeleted(final Collection<File> files) {
            view.removeDiskResources(files);
        }
        
        @Override
        public void onFoldersDeleted(final Collection<Folder> folders) {
            view.removeDiskResources(folders);
        }
    }

    private final DiskResourceView view;
    private final DiskResourceView.Proxy proxy;
    private final TreeLoader<Folder> treeLoader;
    private final HashMap<EventHandler, HandlerRegistration> registeredHandlers = new HashMap<EventHandler, HandlerRegistration>();
    private DiskResourceServiceFacade diskResourceService;
    private final DiskResourceDisplayStrings DISPLAY;

    public DiskResourcePresenterImpl(final DiskResourceView view, final DiskResourceView.Proxy proxy,
            final DiskResourceServiceFacade diskResourceService, final DiskResourceDisplayStrings display) {
        this.view = view;
        this.proxy = proxy;
        this.diskResourceService = diskResourceService;
        this.DISPLAY = display;

        initHandlers();
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

    private void initHandlers() {
        EventBus eventBus = EventBus.getInstance();

        DiskResourcesDeletedEventHandlerImpl diskResourcesDeletedHandler = new DiskResourcesDeletedEventHandlerImpl(view);
        eventBus.addHandler(FoldersDeletedEvent.TYPE, diskResourcesDeletedHandler);
        eventBus.addHandler(FilesDeletedEvent.TYPE, diskResourcesDeletedHandler);
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
        // TODO JDS Determine if one or many was selected



    }

    @Override
    public void onFolderLoad(Folder loadedFolder, Set<DiskResource> folderChildren) {
        if ((getSelectedFolder() != null) && getSelectedFolder().equals(loadedFolder)) {
            view.setDiskResources(folderChildren);
        }
    }

    @Override
    public void doBulkUpload() {
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doBulkUpload");
    }

    @Override
    public void doSimpleUpload() {
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doSimpleUpload");
    }

    @Override
    public void doImport() {
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doImport");
    }

    @Override
    public void doCreateNewFolder() {
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doCreateNewFolder");
    }

    @Override
    public void doRefresh() {
        // view.refreshAll();
        view.refreshFolder(getSelectedFolder());
    }

    @Override
    public void doSimpleDownload() {
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doSimpleDownload");
    }

    @Override
    public void doBulkDownload() {
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doBulkDownload");
    }

    @Override
    public void doRename() {
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doRename");
        FileSelectDialog dialog = new FileSelectDialog();
        dialog.show();
    }

    @Override
    public void doShare() {
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doShare");
        FolderSelectDialog dialog = new FolderSelectDialog();
        dialog.show();
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
            // Create lists for each type of disk resource
            Set<Folder> folders = Sets.newHashSet();
            List<File> files = Lists.newArrayList();
            for (DiskResource dr : getSelectedDiskResources()) {
                if (dr instanceof File) {
                    files.add((File)dr);
                } else if (dr instanceof Folder) {
                    folders.add((Folder)dr);
                }
            }

            // If either of the sublists contains elements, call the appropriate service method.
            if (!folders.isEmpty()) {
                // diskResourceService.deleteFolders(folders, new FolderDeleteCallback(folders, view));
                diskResourceService.deleteDiskResources(folders, new DiskResourceDeleteCallback<Folder>(
                        folders, view));
            }
            if (!files.isEmpty()) {
                diskResourceService.deleteFiles(files, new FileDeleteCallback(files, view));
            }
        } else if ((getSelectedFolder() != null) && isDeletable(getSelectedFolder())) {
            view.mask(DISPLAY.loadingMask());
            diskResourceService.deleteFolders(Lists.newArrayList(getSelectedFolder()),
                    new FolderDeleteCallback(Lists.newArrayList(getSelectedFolder()), view));
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
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doMetadata");
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

}
