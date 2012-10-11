package org.iplantc.core.uidiskresource.client.presenters;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.info.Info;

/**
 * TODO JDS Implement recursive load of previously selected folder.
 * 
 * @author jstroot
 * 
 */
public class DiskResourcePresenter implements DiskResourceView.Presenter,
        DiskResourceViewToolbar.Presenter {

    private final DiskResourceView view;
    private final DiskResourceView.Proxy proxy;

    public DiskResourcePresenter(final DiskResourceView view, final DiskResourceViewToolbar toolbar,
            final DiskResourceView.Proxy proxy) {
        this.view = view;
        this.proxy = proxy;
        TreeLoader<Folder> treeLoader = new TreeLoader<Folder>(this.proxy) {
            @Override
            public boolean hasChildren(Folder parent) {
                return parent.hasSubDirs();
            }
        };


        treeLoader.addLoadHandler(new ChildTreeStoreBinding<Folder>(this.view.getTreeStore()));
        this.view.setTreeLoader(treeLoader);
        this.view.setNorthWidget(toolbar);
        this.view.setPresenter(this);
        this.proxy.setPresenter(this);
        toolbar.setPresenter(this);
    }

    @Override
    public void go(HasOneWidget container) {
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
    }

    @Override
    public void doShare() {
        // TODO Auto-generated method stub
        Info.display("You clicked something!", "doShare");
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
}
