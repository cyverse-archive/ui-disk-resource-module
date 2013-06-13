package org.iplantc.core.uidiskresource.client.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.views.IsMaskable;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.DiskResourceInfo;
import org.iplantc.core.uidiskresource.client.models.DiskResourceMetadata;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.presenters.proxy.SelectFolderByIdLoadHandler;
import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uidiskresource.client.services.callbacks.DiskResourceMetadataUpdateCallback;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbarImpl;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;

/**
 * @author jstroot
 * 
 */
public interface DiskResourceView extends IsWidget, IsMaskable, IsDiskResourceRoot {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter,
 DiskResourceViewToolbarImpl.Presenter, HasHandlerRegistrationMgmt {
        interface Builder extends org.iplantc.core.uicommons.client.presenter.Presenter {
            Builder hideNorth();

            Builder hideWest();

            Builder hideCenter();

            Builder hideEast();

            Builder singleSelect();

            Builder disableDiskResourceHyperlink();
        }

        void go(HasOneWidget container, HasId folderToSelect, List<HasId> diskResourcesToSelect);

        Builder builder();

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

        void addFileSelectChangedHandler(SelectionChangedHandler<DiskResource> selectionChangedHandler);

        void addFolderSelectionHandler(SelectionHandler<Folder> selectionHandler);

        /**
         * Selects the folder with the given Id by adding a {@link SelectFolderByIdLoadHandler} to the
         * view's corresponding {@link TreeLoader}, then triggering a remote load.
         * 
         * @param folderId
         */
        void setSelectedFolderById(HasId folderToSelect);

        /**
         * Sets the selected disk resource with the given ids.
         * 
         * @param diskResourceIdList
         */
//        void setSelectedDiskResourcesById(Set<String> diskResourceIdList);

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

        void doMoveDiskResources(Folder targetFolder, Set<DiskResource> resources);

        /**
         * A convenience method for looking up drop target folders for View components
         * @param widget
         * @param el
         * @return
         */
        Folder getDropTargetFolder(IsWidget widget, Element el);
        
        /**
         * Determines if the given widget is this view's <code>Tree</code> object.
         * 
         * @param widget
         * @return
         */
        boolean isViewTree(IsWidget widget);
        
        /**
         * Determines if the given widget is this view's <code>Grid</code> object.
         * 
         * @param widget
         * @return
         */
        boolean isViewGrid(IsWidget widget);

        boolean canDragDataToTargetFolder(Folder targetFolder, Collection<DiskResource> dropData);

        void deSelectDiskResources();

        void loadSearchHistory();

        void loadUserTrashPath();

        void addToSearchHistory(String searchTerm);

        void removeFromSearchHistory(String searchTerm);

        void saveSearchHistory();

        
        String getCurrentSearchTerm();
        
        void setCurrentSearchTerm(String searchTerm);

        void maskView();

        void unMaskView();

        void handleSearchEvent(DiskResource resource);

        void unMaskView(boolean clearRegisteredHandlers);

        void setSelectedDiskResourcesById(List<HasId> selectedDiskResources);
        
        void OnInfoTypeClick(String id, String infoType);
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
        void setPresenter(Presenter presenter);

    }

    void setPresenter(Presenter presenter);

    void setTreeLoader(TreeLoader<Folder> treeLoader);

    Folder getSelectedFolder();

    Set<DiskResource> getSelectedDiskResources();

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

    void setSouthWidget(IsWidget fl, double size);

    void addDiskResourceSelectChangedHandler(SelectionChangedHandler<DiskResource> selectionChangedHandler);

    void addFolderSelectionHandler(SelectionHandler<Folder> selectionHandler);

    /**
     * Selects the given Folder.
     * This method will also ensure that the Data listing widget is shown.
     * 
     * @param folder
     */
    void setSelectedFolder(Folder folder);

    void setSelectedDiskResources(List<HasId> diskResourcesToSelect);

    void addFolder(Folder parent, Folder newChild);

    Folder getFolderById(String folderId);

    void expandFolder(Folder folder);

    void deSelectDiskResources();

    void refreshFolder(Folder folder);

    DiskResourceViewToolbar getToolbar();

    /**
     * Removes the given <code>DiskResource</code>s from all of the view's stores.
     * 
     * @param resources
     */
    <D extends DiskResource> void removeDiskResources(Collection<D> resources);

    void updateDiskResource(DiskResource originalDr, DiskResource newDr);

    /**
     * Determines if the given widget is this view's <code>Tree</code> object.
     * 
     * @param widget
     * @return
     */
    boolean isViewTree(IsWidget widget);

    /**
     * Determines if the given widget is this view's <code>Grid</code> object.
     * 
     * @param widget
     * @return
     */
    boolean isViewGrid(IsWidget widget);

    TreeNode<Folder> findTreeNode(Element el);

    Element findGridRow(Element el);

    int findRowIndex(Element targetRow);

    ListStore<DiskResource> getListStore();

    void setSingleSelect();

    void disableDiskResourceHyperlink();

    void showDataListingWidget();

    void showSearchResultWidget(IsWidget w);

    void updateDetails(String path, DiskResourceInfo info);

    void resetDetailsPanel();

    void renderSearchHistory(List<String> history);

    void deSelectNavigationFolder();

    boolean isCenterHidden();

    void unmaskDetailsPanel();

    void maskDetailsPanel();

}
