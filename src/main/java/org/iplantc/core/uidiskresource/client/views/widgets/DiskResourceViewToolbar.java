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
}
