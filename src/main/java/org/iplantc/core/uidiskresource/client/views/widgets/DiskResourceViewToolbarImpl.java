package org.iplantc.core.uidiskresource.client.views.widgets;

import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.File;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.presenters.CreateFolderDialog;
import org.iplantc.core.uidiskresource.client.presenters.RenameFileDialog;
import org.iplantc.core.uidiskresource.client.presenters.RenameFolderDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class DiskResourceViewToolbarImpl implements DiskResourceViewToolbar {

    @UiTemplate("DiskResourceViewToolbar.ui.xml")
    interface DiskResourceViewToolbarUiBinder extends UiBinder<Widget, DiskResourceViewToolbarImpl> {
    }

    private static DiskResourceViewToolbarUiBinder BINDER = GWT
            .create(DiskResourceViewToolbarUiBinder.class);

    private DiskResourceViewToolbar.Presenter presenter;
    private final Widget widget;

    @UiField
    TextButton uploads;
    
    @UiField
    MenuItem bulkUploadButton;

    @UiField
    MenuItem simpleUploadButton;

    @UiField
    MenuItem importButton;

    @UiField
    TextButton newFolderButton;

    @UiField
    TextButton refreshButton;

    @UiField
    TextButton downloads;
    
    @UiField
    MenuItem simpleDownloadButton;

    @UiField
    MenuItem bulkDownloadButton;

    @UiField
    TextButton renameButton;

    @UiField
    TextButton deleteButton;

    @UiField
    TextButton shareButton;

    @UiField
    TextField searchField;

    @UiField
    MenuItem emptyTrash;

    @UiField
    MenuItem restore;

    public DiskResourceViewToolbarImpl() {
        widget = BINDER.createAndBindUi(this);

        searchField.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                String val = searchField.getCurrentValue();
                if(val!=null && !val.isEmpty()&& val.length()>2) {
                    presenter.doSearch(val);
                }

            }
        });
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
        CreateFolderDialog dlg = new CreateFolderDialog(presenter.getSelectedFolder(), presenter);
        dlg.show();
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
        if (!presenter.getSelectedDiskResources().isEmpty()
                && (presenter.getSelectedDiskResources().size() == 1)) {
            DiskResource dr = presenter.getSelectedDiskResources().iterator().next();
            if (dr instanceof File) {
                RenameFileDialog dlg = new RenameFileDialog((File)dr, presenter);
                dlg.show();

            } else {
                RenameFolderDialog dlg = new RenameFolderDialog((Folder)dr, presenter);
                dlg.show();

            }
        } else if (presenter.getSelectedFolder() != null) {
            RenameFolderDialog dlg = new RenameFolderDialog(presenter.getSelectedFolder(), presenter);
            dlg.show();
        }
    }

    @UiHandler("deleteButton")
    void onDeleteClicked(SelectEvent event) {
        presenter.requestDelete();
    }

    @UiHandler("shareButton")
    void onShareClicked(SelectEvent event) {
        presenter.doShare();
    }


    @UiHandler("emptyTrash")
    void onEmptyTrashClicked(SelectionEvent<Item> event) {
        final ConfirmMessageBox cmb = new ConfirmMessageBox(I18N.DISPLAY.emptyTrash(),
                I18N.DISPLAY.emptyTrashWarning());
        cmb.addHideHandler(new HideHandler() {
            public void onHide(HideEvent event) {
                if (cmb.getHideButton() == cmb.getButtonById(PredefinedButton.YES.name())) {
                    presenter.emptyTrash();
                }
            }
        });

        cmb.setWidth(300);
        cmb.show();
    }

    @UiHandler("restore")
    void onRestoreClicked(SelectionEvent<Item> event) {
        presenter.restore();
    }

    @Override
    public void setUploadsEnabled(boolean enabled) {
        uploads.setEnabled(enabled);
    }

    @Override
    public void setBulkUploadEnabled(boolean enabled) {
        bulkUploadButton.setEnabled(enabled);
    }

    @Override
    public void setSimpleUploadEnabled(boolean enabled) {
        simpleUploadButton.setEnabled(enabled);
    }

    @Override
    public void setImportButtonEnabled(boolean enabled) {
        importButton.setEnabled(enabled);
    }

    @Override
    public void setNewFolderButtonEnabled(boolean enabled) {
        newFolderButton.setEnabled(enabled);
    }

    @Override
    public void setRefreshButtonEnabled(boolean enabled) {
        refreshButton.setEnabled(enabled);
    }

    @Override
    public void setDownloadsEnabled(boolean enabled) {
        downloads.setEnabled(enabled);
    }

    @Override
    public void setSimpleDowloadButtonEnabled(boolean enabled) {
        simpleDownloadButton.setEnabled(enabled);
    }

    @Override
    public void setBulkDownloadButtonEnabled(boolean enabled) {
        bulkDownloadButton.setEnabled(enabled);
    }

    @Override
    public void setRenameButtonEnabled(boolean enabled) {
        renameButton.setEnabled(enabled);
    }

    @Override
    public void setDeleteButtonEnabled(boolean enabled) {
        deleteButton.setEnabled(enabled);
    }

    @Override
    public void setShareButtonEnabled(boolean enabled) {
        shareButton.setEnabled(enabled);
    }



    @Override
    public void setSearchTerm(String searchTerm) {
        searchField.setValue(searchTerm, true);

    }

    @Override
    public void clearSearchTerm() {
        searchField.clear();
    }

    @Override
    public void setRestoreMenuItemEnabled(boolean enabled) {
        restore.setEnabled(enabled);
    }
}
