package org.iplantc.core.uidiskresource.client.views.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.menu.Item;

public class DiskResourceViewToolbarImpl implements DiskResourceViewToolbar {

    private static DiskResourceViewToolbarUiBinder uiBinder = GWT
            .create(DiskResourceViewToolbarUiBinder.class);

    @UiTemplate("DiskResourceViewToolbar.ui.xml")
    interface DiskResourceViewToolbarUiBinder extends UiBinder<Widget, DiskResourceViewToolbarImpl> {
    }

    private DiskResourceViewToolbar.Presenter presenter;
    private final Widget widget;

    public DiskResourceViewToolbarImpl() {
        widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(DiskResourceViewToolbar.Presenter presenter) {
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
