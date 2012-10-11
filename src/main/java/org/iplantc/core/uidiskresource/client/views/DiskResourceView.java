package org.iplantc.core.uidiskresource.client.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.TreeLoader;

/**
 * @author jstroot
 * 
 */
public interface DiskResourceView extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {

        Folder getSelectedFolder();

        List<DiskResource> getSelectedDiskResources();

        /**
         * 
         * @param folder
         */
        void onFolderSelected(Folder folder);

        void onDiskResourceSelected(List<DiskResource> selection);

        /**
         * Called by the {@link DiskResourceView.Proxy} when a folder is successfully loaded. If the
         * loaded folder is the currently selected folder, the presenter will set its view's
         * <code>DiskResource</code> collection with a call to
         * {@link DiskResourceView#setDiskResources(ArrayList)}.
         * 
         * @param loadedFolder
         * @param folderChildren
         */
        void onFolderLoad(Folder loadedFolder, ArrayList<DiskResource> folderChildren);
    }

    /**
     * A dataproxy used by the <code>Presenter</code> to fetch <code>DiskResource</code> data from the
     * {@link DiskResourceServiceFacade}.
     * When the proxy completes a load of a non-root folder, it is expected to call the
     * {@link DiskResourceView.Presenter#onFolderLoad(Folder, ArrayList)} method with the
     * <code>Folder</code> and <code>File</code> contents of the loaded folder.
     * 
     * @author jstroot
     * 
     */
    public interface Proxy extends DataProxy<Folder, List<Folder>> {
        void load(Folder folder);

        void setPresenter(Presenter presenter);
    }

    void setPresenter(Presenter presenter);

    void setTreeLoader(TreeLoader<Folder> treeLoader);

    Folder getSelectedFolder();

    List<DiskResource> getSelectedDiskResources();

    void setRootFolders(List<Folder> rootFolders);

    TreeStore<Folder> getTreeStore();

    boolean isLoaded(Folder folder);

    void setDiskResources(ArrayList<DiskResource> folderChildren);
    
    void onFolderSelected(Folder folder);

    void onDiskResourceSelected(List<DiskResource> selection);

    void setNorthWidget(IsWidget widget);
}
