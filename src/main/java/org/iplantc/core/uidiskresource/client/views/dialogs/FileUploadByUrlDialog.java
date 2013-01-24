package org.iplantc.core.uidiskresource.client.views.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.validators.UrlValidator;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IsHideable;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uidiskresource.client.services.callbacks.DuplicateDiskResourceCallback;

import com.google.common.collect.Lists;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;

public class FileUploadByUrlDialog extends IPlantDialog {
    private static final String FIELD_HEIGHT = "50";
    private static final String FIELD_WIDTH = "475";
    private static final int MAX_UPLOADS = 5;
    private static final String URL_FIELD = "url";
    private static final String HDN_PARENT_ID_KEY = "parentfolderid";
    private static final String HDN_USER_ID_KEY = "user";

    public FileUploadByUrlDialog(Folder uploadDest, DiskResourceServiceFacade drService, SafeUri servletActionUrl, String userName) {
        setAutoHide(false);
        setHideOnButtonClick(false);
        // Reset the "OK" button text.
        getOkButton().setText(I18N.DISPLAY.upload());
        getOkButton().setEnabled(false);
        setHeadingText(I18N.DISPLAY.upload());

        Status formStatus = new Status();
        FormPanel form = initForm(servletActionUrl);
        FlowLayoutContainer flc = new FlowLayoutContainer();

        flc.add(new Hidden(HDN_PARENT_ID_KEY, uploadDest.getId()));
        flc.add(new Hidden(HDN_USER_ID_KEY, userName));
        flc.add(new HTML(I18N.DISPLAY.fileUploadFolder(uploadDest.getId())));
        flc.add(new HTML(I18N.DISPLAY.urlPrompt()));
        for (int i = 0; i < MAX_UPLOADS; i++) {
            flc.add(buildUrlField());
        }
        form.add(flc);
        add(form);

        addCancleButtonSelectHandler(new CancelButtonSelectHandler(this));
        addOkButtonSelectHandler(new OkButtonSelectHandler(formStatus, uploadDest, getOkButton(), this, drService, form));
    }

    private FormPanel initForm(SafeUri actionUrl) {
        FormPanel ret = new FormPanel();
        ret.setAction(actionUrl);
        ret.setMethod(Method.POST);
        ret.setEncoding(Encoding.MULTIPART);
        ret.addSubmitCompleteHandler(new FormSubmitHandler());

        return ret;
    }

    private TextArea buildUrlField() {
        TextArea urlField = new TextArea();
        urlField.setName(URL_FIELD);
        urlField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
        urlField.addValidator(new UrlValidator());
        urlField.setAutoValidate(true);
        ValidationHandler handler = new ValidationHandler(getOkButton(), this);
        urlField.addInvalidHandler(handler);
        urlField.addValidHandler(handler);
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
            okButton.setEnabled(FormPanelHelper.isValid(dlg, true)
                    && textAreasHaveText(dlg));
        }

        private boolean textAreasHaveText(HasWidgets container) {
            Iterator<Widget> it = container.iterator();
            while (it.hasNext()) {
                Widget w = it.next();

                if ((w instanceof ValueBaseField) 
                        && (((ValueBaseField<?>)w).getCurrentValue() instanceof String)) {
                    @SuppressWarnings("unchecked")
                    ValueBaseField<String> vbf = (ValueBaseField<String>)w;
                    if(!vbf.getCurrentValue().isEmpty()){
                        return true;
                    }
                }

                if (w instanceof HasWidgets) {
                    return textAreasHaveText((HasWidgets)w);
                }
            }
            return false;
        }
    }

    private final class OkButtonSelectHandler implements SelectHandler {
        private final Status formStatus;
        private final HasId uploadDest;
        private final HasEnabled okButton;
        private final HasWidgets dlg;
        private final DiskResourceServiceFacade drService;
        private final FormPanel form;

        public OkButtonSelectHandler(Status formStatus, HasId uploadDest, 
                HasEnabled okButton, HasWidgets dlg, 
                DiskResourceServiceFacade drService, FormPanel form) {
            this.formStatus = formStatus;
            this.uploadDest = uploadDest;
            this.okButton = okButton;
            this.dlg = dlg;
            this.drService = drService;
            this.form = form;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onSelect(SelectEvent event) {
            formStatus.setBusy(I18N.DISPLAY.fileUploadFolder(uploadDest.getId()));
            formStatus.show();
            
            okButton.setEnabled(false);

            // Get the
            final FastMap<Field<String>> destResourceMap = new FastMap<Field<String>>();

            for (IsField<?> field : FormPanelHelper.getFields(dlg)) {
                if (field.getValue() instanceof String) {
                    Field<String> stringField = (Field<String>)field;
                    String url = stringField.getValue().trim();
                    if (!url.isEmpty()) {
                        stringField.setValue(url);
                        destResourceMap.put(buildResourceId(url), stringField);
                    } else {

                        stringField.setEnabled(false);
                    }

                } else {
                    ((Field<?>)field).setEnabled(false);
                }
            }

            if (!destResourceMap.isEmpty()) {
                ArrayList<String> ids = Lists.newArrayList(destResourceMap.keySet());
                drService.diskResourcesExist(ids, new CheckDuplicatesCallback(ids, destResourceMap, formStatus, form));
            }

        }

        private String buildResourceId(String filename) {
            return uploadDest.getId() + "/" + filename; //$NON-NLS-1$
        }
    }

    private final class CheckDuplicatesCallback extends DuplicateDiskResourceCallback {

        private final FastMap<Field<String>> destResourceMap;
        private final Status formStatus;
        private final FormPanel form;

        public CheckDuplicatesCallback(List<String> ids, FastMap<Field<String>> destResourceMap, Status formStatus, FormPanel form) {
            super(ids, null);
            this.destResourceMap = destResourceMap;
            this.formStatus = formStatus;
            this.form = form;
        }

        @Override
        public void markDuplicates(Collection<String> duplicates) {
            if ((duplicates != null) && !duplicates.isEmpty()) {
                for (String id : duplicates) {
                    destResourceMap.get(id).markInvalid(I18N.ERROR.fileExist());
                }
                formStatus.clearStatus(formStatus.getText());
                return;
            } else {
                form.submit();
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

    private final class FormSubmitHandler implements SubmitCompleteHandler {

        @Override
        public void onSubmitComplete(SubmitCompleteEvent event) {
            String results = event.getResults();

        }
    }
}
