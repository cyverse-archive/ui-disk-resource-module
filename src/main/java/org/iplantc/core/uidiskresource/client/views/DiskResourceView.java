package org.iplantc.core.uidiskresource.client.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.iplantc.core.uicommons.client.views.IsMaskable;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceMetadata;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.presenters.callbacks.DiskResourceMetadataUpdateCallback;
import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbarImpl;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * @author jstroot
 * 
 */
public interface DiskResourceView extends IsWidget, IsMaskable {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter,
            DiskResourceViewToolbarImpl.Presenter {

        void go(HasOneWidget container, boolean hideWestWidget, boolean hideCenterWidget,
                boolean hideEastWidget, boolean hideNorthWidget);

        Folder getSelectedFolder();

        Set<DiskResource> getSelectedDiskResources();

        /**
         * Method called by the view when a folder is selected.
         * Whenever this method is called with a non-null and non-empty list, the presenter will have the
         * view de-select all disk resources
         * in the center panel.
         * 
         * @param folders
         */
        void onFolderSelected(Folder folders);

        void onDiskResourceSelected(Set<DiskResource> selection);

        /**
         * Called by the {@link DiskResourceView.Proxy} when a folder is successfully loaded. If the
         * loaded folder is the currently selected folder, the presenter will set its view's
         * <code>DiskResource</code> collection with a call to
         * {@link DiskResourceView#setDiskResources(ArrayList)}.
         * 
         * @param loadedFolder
         * @param folderChildren
         */
        void onFolderLoad(Folder loadedFolder, Set<DiskResource> folderChildren);

        void addFileSelectChangedHandler(SelectionChangedHandler<DiskResource> selectionChangedHandler);

        void addFolderSelectionHandler(SelectionHandler<Folder> selectionHandler);

        /**
         * Selects the folder with the given Id by adding a {@link SelectFolderByIdLoadHandler} to the
         * view's corresponding {@link TreeLoader}.
         * This method is typically called before the
         * {@link org.iplantc.core.uicommons.client.presenter.Presenter#go(HasOneWidget)} method is
         * called.
         * The intent is to have the
         * 
         * @param folderId
         */
        void setSelectedFolderById(String folderId);

        /**
         * Sets the selected disk resource with the given ids.
         * 
         * @param diskResourceIdList
         */
        void setSelectedDiskResourcesById(Set<String> diskResourceIdList);

        DiskResourceView getView();

        /**
         * Retrieves a collection of metadata for the given resource.
         * 
         * @param resource
         * @param callback
         * @return a collection of the given resource's metadata.
         */
        void getDiskResourceMetadata(DiskResource resource, AsyncCallback<String> callback);

        void setDiskResourceMetaData(DiskResource resource, Set<DiskResourceMetadata> metadataToAdd,
                Set<DiskResourceMetadata> metadataToDelete,
                DiskResourceMetadataUpdateCallback diskResourceMetadataUpdateCallback);

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

    Set<DiskResource> getSelectedDiskResources();

    void setRootFolders(Set<Folder> rootFolders);

    TreeStore<Folder> getTreeStore();

    boolean isLoaded(Folder folder);

    void setDiskResources(Set<DiskResource> folderChildren);
    
    void onFolderSelected(Folder folder);

    void onDiskResourceSelected(Set<DiskResource> selection);

    void setWestWidgetHidden(boolean hideWestWidget);

    void setCenterWidgetHidden(boolean hideCenterWidget);

    void setEastWidgetHidden(boolean hideEastWidget);

    void setNorthWidgetHidden(boolean hideNorthWidget);

    void setSouthWidget(IsWidget fl);

    void addDiskResourceSelectChangedHandler(SelectionChangedHandler<DiskResource> selectionChangedHandler);

    void addFolderSelectionHandler(SelectionHandler<Folder> selectionHandler);

    void setSelectedFolder(Folder folder);

    void addFolder(Folder parent, Folder newChild);

    Folder getFolderById(String folderId);

    void expandFolder(Folder folder);

    void deSelectDiskResources();

    /**
     * @return a list of root folders from the view's tree store.
     */
    Set<Folder> getRootFolders();

    /**
     * Clears all children from the tree store and reloads all root folders.
     */
    void refreshAll();

    void refreshFolder(Folder folder);

    DiskResourceViewToolbar getToolbar();

    /**
     * Removes the given <code>DiskResource</code>s from all of the view's stores.
     * 
     * @param resources
     */
    <D extends DiskResource> void removeDiskResources(Collection<D> resources);

    void updateDiskResource(DiskResource originalDr, DiskResource newDr);

}
