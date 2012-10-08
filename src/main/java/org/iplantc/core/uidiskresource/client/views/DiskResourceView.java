package org.iplantc.core.uidiskresource.client.views;

import java.util.List;

import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.TreeLoader;

public interface DiskResourceView extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {

        Folder getSelectedFolder();

        List<DiskResource> getSelectedDiskResources();

        void onFolderSelected(Folder folder);

        void onDiskResourceSelected(List<DiskResource> selection);

    }

    void setPresenter(Presenter presenter);

    void setTreeLoader(TreeLoader<Folder> treeLoader);

    Folder getSelectedFolder();

    List<DiskResource> getSelectedDiskResources();

    void setRootFolders(List<Folder> rootFolders);

    TreeStore<Folder> getTreeStore();

    boolean isLoaded(Folder folder);
}
