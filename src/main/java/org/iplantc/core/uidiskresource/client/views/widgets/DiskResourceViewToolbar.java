package org.iplantc.core.uidiskresource.client.views.widgets;

import java.util.Set;

import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.Folder;

import com.google.gwt.user.client.ui.IsWidget;

public interface DiskResourceViewToolbar extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {

        void doBulkUpload();

        void doSimpleUpload();

        void doImport();

        /**
         * Reloads the view's current selected folders.
         */
        void doRefresh();

        /**
         * Reloads the given folder in the view's navigation tree, and also the data grid if it's the
         * currently selected folder.
         * 
         * @param folder The folder to reload from the service.
         */
        void refreshFolder(Folder folder);

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
}
