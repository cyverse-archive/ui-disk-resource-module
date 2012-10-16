package org.iplantc.core.uidiskresource.client.views.dialogs;

import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceModelKeyProvider;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.presenters.DiskResourcePresenterImpl;
import org.iplantc.core.uidiskresource.client.presenters.proxy.FolderRpcProxy;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.DiskResourceViewImpl;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * An <code>IPlantDialog</code> which wraps the standard <code>DiskResourceView</code> for folder
 * selection.
 * 
 * Users of this class are responsible adding hide handlers to get the selected folder.
 * 
 * @author jstroot
 * 
 */
public class FolderSelectDialog extends IPlantDialog {

    private final DiskResourceView.Presenter presenter;
    private final TextField selectedFileField = new TextField();
    private String selectedFolderId;

    public FolderSelectDialog() {
        setResizable(true);
        setSize("640", "480");
        setHeadingText(I18N.DISPLAY.selectAFolder());

        final TreeStore<Folder> treeStore = new TreeStore<Folder>(new DiskResourceModelKeyProvider());
        DiskResourceView view = new DiskResourceViewImpl(treeStore);
        DiskResourceView.Proxy proxy = new FolderRpcProxy();
        presenter = new DiskResourcePresenterImpl(view, proxy);

        // TODO JDS Need to add a south widget with label and text field.
        final FieldLabel fl = new FieldLabel(selectedFileField, I18N.DISPLAY.selectedFolder());

        view.setSouthWidget(fl);
        presenter.addFolderSelectChangedHandler(new FileSelectionChangedHandler(selectedFileField));

        // Tell the presenter to add the view with the north, east, and center widgets hidden.
        presenter.go(this, false, true, true, true);

    }

    private final class FileSelectionChangedHandler implements SelectionChangedHandler<Folder> {
        private final TextField selectedFileField;

        private FileSelectionChangedHandler(final TextField selectedFileField) {
            this.selectedFileField = selectedFileField;
        }

        @Override
        public void onSelectionChanged(SelectionChangedEvent<Folder> event) {
            if ((event.getSelection() == null) || event.getSelection().isEmpty()) {
                return;
            }
            Folder diskResource = event.getSelection().get(0);
            selectedFileField.setValue(diskResource.getName());
            setSelectedFolderId(diskResource.getId());
        }
    }

    public String getSelectedFolderId() {
        return selectedFolderId;
    }

    private void setSelectedFolderId(String id) {
        this.selectedFolderId = id;
    }

}
