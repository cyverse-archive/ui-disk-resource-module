package org.iplantc.core.uidiskresource.client.views.dialogs;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.validators.UrlValidator;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IsHideable;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextArea;

public class FileUploadByUrlDialog extends IPlantDialog {
    private static final int MAX_UPLOADS = 5;
    private static final String FIELD_WIDTH = "475";
    private final Status formStatus = new Status();
    private final Folder uploadDest;

    public FileUploadByUrlDialog(Folder uploadDest) {
        this.uploadDest = uploadDest;
        setAutoHide(false);
        setHideOnButtonClick(false);
        addCancleButtonSelectHandler(new CancelButtonSelectHandler(this));
        addOkButtonSelectHandler(new OkButtonSelectHandler(formStatus, uploadDest, getOkButton(), this));
        // Reset the "OK" button text.
        getOkButton().setText(I18N.DISPLAY.upload());

        VerticalLayoutContainer vlc = new VerticalLayoutContainer();
        for (int i = 0; i < MAX_UPLOADS; i++) {
            vlc.add(buildUrlField());
        }
        add(vlc);
    }

    private TextArea buildUrlField() {
        TextArea urlField = new TextArea();
        urlField.setWidth(FIELD_WIDTH);
        urlField.addValidator(new UrlValidator());
        urlField.setAutoValidate(true);
        urlField.addInvalidHandler(new ValidationHandler(getOkButton(), this));
        urlField.addValidHandler(new ValidationHandler(getOkButton(), this));
        return urlField;
    }

    private final class ValidationHandler implements InvalidHandler, ValidHandler {
        private final HasEnabled okButton;
        private final HasWidgets dlg;

        public ValidationHandler(HasEnabled okButton, HasWidgets dlg) {
            this.okButton = okButton;
            this.dlg = dlg;
        }

        @Override
        public void onInvalid(InvalidEvent event) {
            okButton.setEnabled(false);
        }

        @Override
        public void onValid(ValidEvent event) {
            okButton.setEnabled(FormPanelHelper.isValid(dlg, true));
        }
    }

    private final class OkButtonSelectHandler implements SelectHandler {
        private final Status formStatus;
        private final HasId uploadDest;
        private final HasEnabled okButton;
        private final HasWidgets dlg;

        public OkButtonSelectHandler(Status formStatus, HasId uploadDest, HasEnabled okButton, HasWidgets dlg) {
            this.formStatus = formStatus;
            this.uploadDest = uploadDest;
            this.okButton = okButton;
            this.dlg = dlg;
        }

        @Override
        public void onSelect(SelectEvent event) {
            formStatus.setBusy(I18N.DISPLAY.fileUploadFolder(uploadDest.getId()));
            formStatus.show();
            
            okButton.setEnabled(false);

            // Get the
            final FastMap<IsField<String>> destResourceMap = new FastMap<IsField<String>>();

            for (IsField<?> field : FormPanelHelper.getFields(dlg)) {

            }

        }
    }

    private final class CancelButtonSelectHandler implements SelectHandler {
        private final IsHideable dlg;
    
        public CancelButtonSelectHandler(IsHideable dlg) {
            this.dlg = dlg;
        }
    
        @Override
        public void onSelect(SelectEvent event) {
            dlg.hide();
        }
    }
}
