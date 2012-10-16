package org.iplantc.core.uidiskresource.client.views.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.menu.Item;

public class DiskResourceViewToolbar implements IsWidget {

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


    interface DiskResourceViewToolbarUiBinder extends UiBinder<Widget, DiskResourceViewToolbar> {
    }

    private static DiskResourceViewToolbarUiBinder BINDER = GWT
            .create(DiskResourceViewToolbarUiBinder.class);

    private Presenter presenter;
    private final Widget widget;

    public DiskResourceViewToolbar() {
        widget = BINDER.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("bulkUploadButton")
    void onBulkUploadClicked(SelectionEvent<Item> event) {
        presenter.doBulkUpload();
    }

    @UiHandler("simpleUploadButton")
    void onSimpleUploadClicked(SelectionEvent<Item> event) {
        presenter.doSimpleUpload();
    }

    @UiHandler("importButton")
    void onImportClicked(SelectionEvent<Item> event) {
        presenter.doImport();
    }

    @UiHandler("newFolderButton")
    void onNewFolderClicked(SelectEvent event) {
        presenter.doCreateNewFolder();
    }

    @UiHandler("refreshButton")
    void onRefreshClicked(SelectEvent event) {
        presenter.doRefresh();
    }

    @UiHandler("simpleDownloadButton")
    void onSimpleDownloadClicked(SelectionEvent<Item> event) {
        presenter.doSimpleDownload();
    }

    @UiHandler("bulkDownloadButton")
    void onBulkDownloadClicked(SelectionEvent<Item> event) {
        presenter.doBulkDownload();
    }

    @UiHandler("renameButton")
    void onRenameClicked(SelectEvent event) {
        presenter.doRename();
    }

    @UiHandler("deleteButton")
    void onDeleteClicked(SelectEvent event) {
        presenter.doDelete();
    }

    @UiHandler("shareButton")
    void onShareClicked(SelectEvent event) {
        presenter.doShare();
    }

    @UiHandler("metadataButton")
    void onMetadataClicked(SelectEvent event) {
        presenter.doMetadata();
    }

    @UiHandler("dataQuotaButton")
    void onDataQuotaClicked(SelectEvent event) {
        presenter.doDataQuota();
    }
}
