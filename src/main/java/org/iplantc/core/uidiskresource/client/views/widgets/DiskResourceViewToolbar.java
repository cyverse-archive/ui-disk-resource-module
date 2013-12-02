package org.iplantc.core.uidiskresource.client.views.widgets;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;

import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler;

import java.util.List;
import java.util.Set;

public interface DiskResourceViewToolbar extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter, SaveDiskResourceQueryEventHandler, SubmitDiskResourceQueryEventHandler {

        void doBulkUpload();

        void doSimpleUpload();

        void doImport();

        /**
         * Reloads the given folder in the view's navigation tree, and if it's the currently selected
         * folder then the data grid is refreshed as well.
         * 
         * @param folder The folder to reload from the service.
         */
        void doRefresh(Folder folder);

        /**
         * Reloads the folder with the given ID in the view's navigation tree, and if it's the currently
         * selected folder then the data grid is refreshed with the given selected resources.
         * 
         * @param selectedResources
         * @param folderId
         */
        void refreshFolder(String folderId, List<DiskResource> selectedResources);

        void doSimpleDownload();

        void doBulkDownload();

        void doShare();

        void requestDelete();

        void doDelete();

        void doMetadata();

        void doDataQuota();

        Set<DiskResource> getSelectedDiskResources();

        Folder getSelectedFolder();

        void doRename(DiskResource dr, String newName);

        void doCreateNewFolder(Folder parentFolder, String folderName);

        void doSearch(String val);
        
        void emptyTrash();

        void restore();

        void doDataLinks();

        void onMove();
        
        void onNewFile();

    }

   

    void setPresenter(Presenter presenter);
    
    void setUploadsEnabled(boolean enabled);

    void setBulkUploadEnabled(boolean enabled);

    void setSimpleUploadEnabled(boolean enabled);

    void setImportButtonEnabled(boolean enabled);

    void setNewFolderButtonEnabled(boolean enabled);

    void setRefreshButtonEnabled(boolean enabled);

    void setDownloadsEnabled(boolean enabled);
    
    void setSimpleDowloadButtonEnabled(boolean enabled);

    void setBulkDownloadButtonEnabled(boolean enabled);

    void setRenameButtonEnabled(boolean enabled);

    void setDeleteButtonEnabled(boolean enabled);

    void setShareButtonEnabled(boolean enabled);

    void setShareMenuItemEnabled(boolean enabled);

    void setDataLinkMenuItemEnabled(boolean enabled);

    void setSearchTerm(String searchTerm);

    void clearSearchTerm();

    void setRestoreMenuItemEnabled(boolean b);

    void setMetaDatMenuItemEnabled(boolean canEditMetadata);

    void setEditEnabled(boolean canEdit);

    void setMoveButtonEnabled(boolean enabled);

    void setNewFileButtonEnabled(boolean enabled);

    void setNewButtonEnabled(boolean enabled);
}
