package org.iplantc.core.uidiskresource.client.views.dialogs;

import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.gin.DiskResourceInjector;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.File;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * An <code>IPlantDialog</code> which wraps the standard <code>DiskResourceView</code> for file selection.
 * 
 * Users of this class are responsible adding hide handlers to get the selected file.
 * 
 * @author jstroot
 * 
 */
public class FileSelectDialog extends IPlantDialog {

    private final DiskResourceView.Presenter presenter;
    private final TextField selectedFileField = new TextField();
    private String selectedFileId;

    public FileSelectDialog() {
        setResizable(true);
        setSize("640", "480");
        setHeadingText(I18N.DISPLAY.selectAFile());

        presenter = DiskResourceInjector.INSTANCE.getDiskResourceViewPresenter();

        final FieldLabel fl = new FieldLabel(selectedFileField, I18N.DISPLAY.selectedFile());

        presenter.getView().setSouthWidget(fl);
        presenter.addFileSelectChangedHandler(new FileSelectionChangedHandler(selectedFileField));

        // Tell the presenter to add the view with the north and east widgets hidden.
        presenter.go(this, false, false, true, true);

    }

    private final class FileSelectionChangedHandler implements SelectionChangedHandler<DiskResource> {
        private final TextField selectedFileField;

        private FileSelectionChangedHandler(final TextField selectedFileField) {
            this.selectedFileField = selectedFileField;
        }

        @Override
        public void onSelectionChanged(SelectionChangedEvent<DiskResource> event) {
            if ((event.getSelection() == null) || event.getSelection().isEmpty()) {
                return;
            }
            DiskResource diskResource = event.getSelection().get(0);
            if (diskResource instanceof File) {
                selectedFileField.setValue(((File)diskResource).getName());
                setSelectedFileId(((File)diskResource).getId());
            }
        }
    }
    
    public String getSelectedFileId() {
        return selectedFileId;
    }

    private void setSelectedFileId(String id) {
        this.selectedFileId = id;
    }

}
