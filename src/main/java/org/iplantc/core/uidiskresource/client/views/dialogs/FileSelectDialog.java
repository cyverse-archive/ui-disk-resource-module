package org.iplantc.core.uidiskresource.client.views.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.gin.DiskResourceInjector;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.File;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.TakesValue;
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

    protected FileSelectDialog(boolean singleSelect) {
        setResizable(true);
        setSize("640", "480");
        setHeadingText(I18N.DISPLAY.selectAFile());

        presenter = DiskResourceInjector.INSTANCE.getDiskResourceViewPresenter();

        final FieldLabel fl = new FieldLabel(selectedFileField, I18N.DISPLAY.selectedFile());

        presenter.getView().setSouthWidget(fl);
        presenter.addFileSelectChangedHandler(new FileSelectionChangedHandler(selectedFileField));

        // Tell the presenter to add the view with the north and east widgets hidden.
        DiskResourceView.Presenter.Builder b = presenter.builder().hideNorth().hideEast().disableDiskResourceHyperlink();
        if (singleSelect) {
            b.singleSelect();
        }

        b.go(this);
    }

    public FileSelectDialog() {
        this(false);
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
            ArrayList<File> newArrayList = Lists.newArrayList(DiskResourceUtil.extractFiles(event.getSelection()));
            List<String> idList = DiskResourceUtil.asStringIdList(newArrayList);
            setValue(idList);
            selectedFileField.setValue(DiskResourceUtil.asCommaSeperatedNameList(idList));
        }
    }
    
    @Override
    public void setValue(List<String> value) {
        this.selectedFileIds = value;

    }

    @Override
    public List<String> getValue() {
        return selectedFileIds;
    }

    public static FileSelectDialog singleSelect() {
        return new FileSelectDialog(true);
    }

}
