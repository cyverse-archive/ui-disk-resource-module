package org.iplantc.core.uidiskresource.client.views.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.gin.DiskResourceInjector;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.File;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView.Presenter;

import com.google.common.collect.Lists;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * An <code>IPlantDialog</code> which wraps the standard <code>DiskResourceView</code> for file
 * selection.
 * 
 * Users of this class are responsible adding hide handlers to get the selected file.
 * FIXME JDS Needs to support MultiSelect, TakesValue<List<String>>
 * 
 * @author jstroot
 * 
 */
public class FileSelectDialog extends IPlantDialog implements TakesValue<List<String>> {

    private final DiskResourceView.Presenter presenter;
    private final TextField selectedFileField = new TextField();
    private List<String> selectedFileIds;
    private TextButton okButton;

    public static FileSelectDialog singleSelect() {
        return new FileSelectDialog(true);
    }

    public FileSelectDialog() {
        this(false);
    }

    protected FileSelectDialog(boolean singleSelect) {
        // Get a reference to the OK button, and disable it by default.
        Widget w = getButtonBar().getItemByItemId(PredefinedButton.OK.name());
        if ((w != null) && (w instanceof TextButton)) {
            okButton = (TextButton)w;
            okButton.setEnabled(false);
        }

        setResizable(true);
        setSize("640", "480");
        setHeadingText(I18N.DISPLAY.selectAFile());

        presenter = DiskResourceInjector.INSTANCE.getDiskResourceViewPresenter();

        final FieldLabel fl = new FieldLabel(selectedFileField, I18N.DISPLAY.selectedFile());

        selectedFileField.addKeyUpHandler(new SelectedFileFieldKeyUpHandler(presenter, selectedFileField));

        presenter.getView().setSouthWidget(fl);
        presenter.addFileSelectChangedHandler(new FileSelectionChangedHandler(selectedFileField, okButton));

        // Tell the presenter to add the view with the north and east widgets hidden.
        DiskResourceView.Presenter.Builder b = presenter.builder().hideNorth().hideEast().disableDiskResourceHyperlink();
        if (singleSelect) {
            b.singleSelect();
        }

        b.go(this);
    }

    @Override
    public void setValue(List<String> value) {
        this.selectedFileIds = value;
    
    }

    @Override
    public List<String> getValue() {
        return selectedFileIds;
    }

    public Set<DiskResource> getDiskResources() {
        return presenter.getSelectedDiskResources();
    }

    private final class SelectedFileFieldKeyUpHandler implements KeyUpHandler {
        private final Presenter presenter;
        private final HasValue<String> hasValue;

        public SelectedFileFieldKeyUpHandler(final DiskResourceView.Presenter presenter, final HasValue<String> hasValue) {
            this.presenter = presenter;
            this.hasValue = hasValue;
        }

        @Override
        public void onKeyUp(KeyUpEvent event) {
            if ((event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) || (event.getNativeKeyCode() == KeyCodes.KEY_DELETE)) {
                presenter.deSelectDiskResources();
                hasValue.setValue(null);
            } else {
                event.preventDefault();
            }

        }
    }

    private final class FileSelectionChangedHandler implements SelectionChangedHandler<DiskResource> {
        private final TextField selectedFileField;
        private final HasEnabled hasEnabled;

        private FileSelectionChangedHandler(final TextField selectedFileField, final HasEnabled hasEnabled) {
            this.selectedFileField = selectedFileField;
            this.hasEnabled = hasEnabled;
        }

        @Override
        public void onSelectionChanged(SelectionChangedEvent<DiskResource> event) {
            if ((event.getSelection() == null) || event.getSelection().isEmpty()) {
                // Disable the okButton
                hasEnabled.setEnabled(false);
                return;
            }
            ArrayList<File> newArrayList = Lists.newArrayList(DiskResourceUtil.extractFiles(event.getSelection()));
            List<String> idList = DiskResourceUtil.asStringIdList(newArrayList);
            setValue(idList);
            selectedFileField.setValue(DiskResourceUtil.asCommaSeperatedNameList(idList));
            // Enable the okButton
            hasEnabled.setEnabled(true);
        }
    }

}
