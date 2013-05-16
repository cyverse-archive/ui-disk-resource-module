package org.iplantc.core.uidiskresource.client.presenters.proxy;

import java.util.List;
import java.util.Set;

import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

import com.google.common.collect.Sets;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.LoadEvent;

/**
 * A load handler which is responsible for updating the <code>DiskResourceView</code>'s <code>TreeStore</code> and
 * <code>ListStore</code> in response to remote loads made by the {@link FolderRpcProxy}.
 * 
 * @author jstroot
 * 
 */
public final class DiskResourceViewLoadHandler extends ChildTreeStoreBinding<Folder> {
    private final DiskResourceView.Presenter presenter;

    public DiskResourceViewLoadHandler(TreeStore<Folder> treeStore, DiskResourceView.Presenter presenter) {
        super(treeStore);
        this.presenter = presenter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ChildTreeStoreBinding#onLoad(LoadEvent)
     * 
     * This method updates the TreeStore via the super call. The remainder of the method determines if
     * the center view should update. The ListStore should only be updated if the load result parent is the currently
     * selected Folder. At other times (e.g. when a user clicks the expand arrow in the navigation view)
     * when the center panel ListStore should not be updated.
     */
    @Override
    public void onLoad(LoadEvent<Folder, List<Folder>> event) {
        super.onLoad(event);

        Folder parent = event.getLoadConfig();
        if ((parent != null) && (presenter.getSelectedFolder() == parent) && !presenter.getView().isCenterHidden()) {
            Set<DiskResource> parentFolderChildren = Sets.newHashSet();
            if (parent.getFolders() != null) {
                parentFolderChildren.addAll(parent.getFolders());
            }
        }
    }
}