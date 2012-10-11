package org.iplantc.core.uidiskresource.client.views.widgets;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;

public interface DiskResourceViewToolbar extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {

        void doBulkUpload();

        void doSimpleUpload();

        void doImport();

        void doCreateNewFolder();

        void doRefresh();

        void doSimpleDownload();

        void doBulkDownload();

        void doRename();

        void doShare();

        void doDelete();

        void doMetadata();

        void doDataQuota();

    }

    interface Resources extends ClientBundle {
        @Source("icons/drive_disk.png")
        ImageResource uploadButtonIcon();

        @Source("icons/folder_add.gif")
        ImageResource newFolderButtonIcon();

        @Source("icons/refreshicon.png")
        ImageResource refreshButtonIcon();

        @Source("icons/file_download.gif")
        ImageResource downloadButtonIcon();

        @Source("icons/folder_rename.gif")
        ImageResource renameButtonIcon();

        @Source("icons/file_delete.gif")
        ImageResource deleteButtonIcon();

        @Source("icons/group_key.png")
        ImageResource shareButtonIcon();

        @Source("icons/comments.png")
        ImageResource metadataButtonIcon();

        @Source("icons/comments.png")
        ImageResource dataQuotaButtonIcon();

    }

    void setPresenter(Presenter presenter);
}
