package org.iplantc.core.uidiskresource.client.views.dialogs;

import java.util.List;

import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.gin.DiskResourceInjector;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

import com.google.common.collect.Lists;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * An <code>IPlantDialog</code> which wraps the standard <code>DiskResourceView</code> for folder
 * selection.
 * 
 * Users of this class are responsible adding hide handlers to get the selected folder.
 * 
 * @author jstroot
 * 
 */
public class FolderSelectDialog extends IPlantDialog implements TakesValue<List<String>> {

    private final DiskResourceView.Presenter presenter;
    private final TextField selectedFolderField = new TextField();

    private List<String> selectedFolderId;

    public FolderSelectDialog() {
        // Disable Ok button by default.
        getOkButton().setEnabled(false);
        
        setResizable(true);
        setSize("640", "480");
        setHeadingText(I18N.DISPLAY.selectAFolder());

        presenter = DiskResourceInjector.INSTANCE.getDiskResourceViewPresenter();

        final FieldLabel fl = new FieldLabel(selectedFolderField, I18N.DISPLAY.selectedFolder());

        presenter.getView().setSouthWidget(fl);
        presenter.addFolderSelectionHandler(new FolderSelectionChangedHandler(this, selectedFolderField, getOkButton()));

        // Tell the presenter to add the view with the north, east, and center widgets hidden.
        // presenter.go(this, false, true, true, true);
        presenter.builder().hideNorth().hideCenter().hideEast().singleSelect().go(this);

    }

    private final class FolderSelectionChangedHandler implements SelectionHandler<Folder> {
        private final HasValue<String> textBox;
        private final HasEnabled okButton;
        private final TakesValue<List<String>> dlg;

        private FolderSelectionChangedHandler(final TakesValue<List<String>> dlg, final HasValue<String> textBox, final HasEnabled okButton) {
            this.textBox = textBox;
            this.okButton = okButton;
            this.dlg = dlg;
        }

        @Override
        public void onSelection(SelectionEvent<Folder> event) {
            if (event.getSelectedItem() == null) {
                // Disable the okButton
                okButton.setEnabled(false);
                return;
            }
            Folder diskResource = event.getSelectedItem();
            dlg.setValue(Lists.newArrayList(diskResource.getId()));
            textBox.setValue(diskResource.getName());
            // Enable the okButton
            okButton.setEnabled(true);
        }
    }

    @Override
    public List<String> getValue() {
        return selectedFolderId;
    }

    @Override
    public void setValue(List<String> value) {
        this.selectedFolderId = value;
    }

}
