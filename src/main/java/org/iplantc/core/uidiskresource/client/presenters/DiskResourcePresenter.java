package org.iplantc.core.uidiskresource.client.presenters;

import java.util.List;

import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.presenters.proxy.FolderRpcProxy;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.TreeLoader;

public class DiskResourcePresenter implements DiskResourceView.Presenter,
        DiskResourceViewToolbar.Presenter {

    private final DiskResourceView view;
    private FolderRpcProxy proxy;

    public DiskResourcePresenter(final DiskResourceView view) {
        this.view = view;
        proxy = new FolderRpcProxy();
        TreeLoader<Folder> treeLoader = new TreeLoader<Folder>(proxy) {
            @Override
            public boolean hasChildren(Folder parent) {
                return parent.hasSubDirs();
            }
        };


        treeLoader.addLoadHandler(new ChildTreeStoreBinding<Folder>(view.getTreeStore()));
        view.setTreeLoader(treeLoader);
        view.setPresenter(this);
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);

        // proxy.load(null, new AsyncCallback<List<Folder>>() {
        //
        // @Override
        // public void onSuccess(List<Folder> result) {
        // view.setRootFolders(result);
        // }
        //
        // @Override
        // public void onFailure(Throwable caught) {}
        // });
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
        // FIXME JDS This need to be IMPLEMENTED
        if (view.isLoaded(folder)) {
            GWT.log("Folder is loaded");
        } else {
            GWT.log("Folder is NOT LOADED");

        }

        if ((folder.getFiles() != null) || (folder.getFolders() != null)) {
            GWT.log("We have stuff");
        } else {
            GWT.log("We got NOTHING");

        }
    }

    @Override
    public void onDiskResourceSelected(List<DiskResource> selection) {
        // TODO Auto-generated method stub

    }

}
