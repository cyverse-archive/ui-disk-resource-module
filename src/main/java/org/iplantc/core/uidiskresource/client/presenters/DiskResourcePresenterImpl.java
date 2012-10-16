package org.iplantc.core.uidiskresource.client.presenters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.dialogs.FileSelectDialog;
import org.iplantc.core.uidiskresource.client.views.dialogs.FolderSelectDialog;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * @author jstroot
 * 
 */
public class DiskResourcePresenterImpl implements DiskResourceView.Presenter,
        DiskResourceViewToolbar.Presenter, HasHandlerRegistrationMgmt {

    private final DiskResourceView view;
    private final DiskResourceView.Proxy proxy;
    private final TreeLoader<Folder> treeLoader;
    private final HashMap<EventHandler, HandlerRegistration> registeredHandlers = new HashMap<EventHandler, HandlerRegistration>();

    public DiskResourcePresenterImpl(final DiskResourceView view, final DiskResourceView.Proxy proxy) {
        this.view = view;
        this.proxy = proxy;
        treeLoader = new TreeLoader<Folder>(this.proxy) {
            @Override
            public boolean hasChildren(Folder parent) {
                return parent.hasSubDirs();
            }
        };

        treeLoader.addLoadHandler(new ChildTreeStoreBinding<Folder>(this.view.getTreeStore()));
        this.view.setTreeLoader(treeLoader);
        this.view.setPresenter(this);
        this.proxy.setPresenter(this);
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
    public List<DiskResource> getSelectedDiskResources() {
        return view.getSelectedDiskResources();
    }

    @Override
    public void onFolderSelected(Folder folder) {
        proxy.load(folder);
    }

    @Override
    public void onDiskResourceSelected(List<DiskResource> selection) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFolderLoad(Folder loadedFolder, ArrayList<DiskResource> folderChildren) {
        if (loadedFolder == getSelectedFolder()) {
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
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doRefresh");
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
    public void doDelete() {
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doDelete");
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
    public void addFolderSelectChangedHandler(SelectionChangedHandler<Folder> selectionChangedHandler) {
        view.addFolderSelectChangedHandler(selectionChangedHandler);
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
    public void setSelectedDiskResourcesById(List<String> diskResourceIdList) {
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
