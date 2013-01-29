package org.iplantc.core.uidiskresource.client.views.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.core.uicommons.client.widgets.IPCFileUploadField;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uidiskresource.client.services.callbacks.DuplicateDiskResourceCallback;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitEvent;
import com.sencha.gxt.widget.core.client.event.SubmitEvent.SubmitHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.TextField;

public class SimpleFileUploadDialog extends IPlantDialog {

    private static final String FORM_WIDTH = "475";
    private static final String FORM_HEIGHT = "28";
    public static final String HDN_PARENT_ID_KEY = "dest";
    public static final String HDN_USER_ID_KEY = "user";
    private static SimpleFileUploadPanelUiBinder BINDER = GWT.create(SimpleFileUploadPanelUiBinder.class);

    @UiTemplate("SimpleFileUploadPanel.ui.xml")
    interface SimpleFileUploadPanelUiBinder extends UiBinder<Widget, SimpleFileUploadDialog> {
    }

    @UiField
    FormPanel form0;
    @UiField
    FormPanel form1;
    @UiField
    FormPanel form2;
    @UiField
    FormPanel form3;
    @UiField
    FormPanel form4;

    @UiField
    IPCFileUploadField fuf0;
    @UiField
    IPCFileUploadField fuf1;
    @UiField
    IPCFileUploadField fuf2;
    @UiField
    IPCFileUploadField fuf3;
    @UiField
    IPCFileUploadField fuf4;

    @UiField
    TextButton btn0;
    @UiField
    TextButton btn1;
    @UiField
    TextButton btn2;
    @UiField
    TextButton btn3;
    @UiField
    TextButton btn4;

    @UiField
    Status status0;
    @UiField
    Status status1;
    @UiField
    Status status2;
    @UiField
    Status status3;
    @UiField
    Status status4;

    private final List<FormPanel> formList;
    private final List<IPCFileUploadField> fufList;
    private final List<TextButton> tbList;
    private final List<Status> statList;
    private final Folder uploadDest;
    private final DiskResourceServiceFacade drService;
    private final List<FormPanel> submittedForms = Lists.newArrayList();
    private final SafeUri fileUploadServlet;
    private final String userName;

    public SimpleFileUploadDialog(Folder uploadDest, DiskResourceServiceFacade drService, SafeUri fileUploadServlet, String userName) {
        this.uploadDest = uploadDest;
        this.drService = drService;
        this.fileUploadServlet = fileUploadServlet;
        this.userName = userName;
        setAutoHide(false);
        setHideOnButtonClick(false);
        // Reset the "OK" button text
        getOkButton().setText(I18N.DISPLAY.upload());
        getOkButton().setEnabled(false);
        setHeadingText(I18N.DISPLAY.upload());
        addCancelButtonSelectHandler(new HideSelectHandler(this));

        add(BINDER.createAndBindUi(this));

        formList = Lists.newArrayList(form0, form1, form2, form3, form4);
        fufList = Lists.newArrayList(fuf0, fuf1, fuf2, fuf3, fuf4);
        tbList = Lists.newArrayList(btn0, btn1, btn2, btn3, btn4);
        statList = Lists.newArrayList(status0, status1, status2, status3, status4);
    }

    @UiFactory
    FormPanel createFormPanel() {
        FormPanel form = new FormPanel();
        form.setAction(fileUploadServlet);
        form.setMethod(Method.POST);
        form.setEncoding(Encoding.MULTIPART);
        form.setSize(FORM_WIDTH, FORM_HEIGHT);
        return form;
    }

    @UiFactory
    HorizontalLayoutContainer createHLC() {
        HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
        hlc.add(new Hidden(HDN_PARENT_ID_KEY, uploadDest.getId()));
        hlc.add(new Hidden(HDN_USER_ID_KEY, userName));
        return hlc;
    }

    @UiFactory
    Status createFormStatus(){
        Status status = new Status();
        status.setWidth(15);
        return status;
    }

    @UiHandler({"btn0", "btn1", "btn2", "btn3", "btn4"})
    void onResetClicked(SelectEvent event) {
        IPCFileUploadField uField = fufList.get(tbList.indexOf(event.getSource()));
        uField.reset();
        uField.validate(true);
    }

    @UiHandler({"fuf0", "fuf1", "fuf2", "fuf3", "fuf4"})
    void onFieldChanged(ChangeEvent event) {
        getOkButton().setEnabled(FormPanelHelper.isValid(this, true) && isValidForm());
    }

    @UiHandler({"fuf0", "fuf1", "fuf2", "fuf3", "fuf4"})
    void onFieldValid(ValidEvent event) {
        getOkButton().setEnabled(FormPanelHelper.isValid(this, true) && isValidForm());
    }

    private boolean isValidForm() {
        for (IPCFileUploadField f : fufList) {
            if (!Strings.isNullOrEmpty(f.getValue()) && !f.getValue().equalsIgnoreCase(uploadDest.getId())) {
                return true;
            }
        }
        return false;

    }

    @UiHandler({"fuf0", "fuf1", "fuf2", "fuf3", "fuf4"})
    void onFieldInvalid(InvalidEvent event) {
        getOkButton().setEnabled(false);
    }

    @UiHandler({"form0", "form1", "form2", "form3", "form4"})
    void onSubmitComplete(SubmitCompleteEvent event) {
        String results = event.getResults();

        if (submittedForms.contains(event.getSource())) {
            submittedForms.remove(event.getSource());
            statList.get(formList.indexOf(event.getSource())).clearStatus("");
            hide();
        }
    }

    @UiHandler({"fuf0", "fuf1", "fuf2", "fuf3", "fuf4"})
    void onFormKeyUp(KeyUpEvent event) {
        if ((event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) || (event.getNativeKeyCode() == KeyCodes.KEY_DELETE)) {
            TextField tf = (TextField)event.getSource();
            for (IPCFileUploadField fuf : fufList) {
                String value = fuf.getValue();
                String currentValue = tf.getCurrentValue();
                if (value.equalsIgnoreCase(currentValue)) {
                    fuf.clear();
                    fuf.validate(true);
                    break;
                }
            }
        }
    }

    @Override
    protected void onOkButtonClicked() {
        doUpload();
    }

    private void doUpload() {
        FastMap<IPCFileUploadField> destResourceMap = new FastMap<IPCFileUploadField>();
        for (IPCFileUploadField field : fufList) {
            String fileName = field.getValue().replaceAll(".*[\\\\/]", "");
            field.setEnabled(!Strings.isNullOrEmpty(fileName) && !fileName.equalsIgnoreCase("null"));
            if (field.isEnabled()) {
                destResourceMap.put(fileName, field);
            } else {
                field.setEnabled(false);
            }
        }

        if (!destResourceMap.isEmpty()) {
            ArrayList<String> ids = Lists.newArrayList(destResourceMap.keySet());
            drService.diskResourcesExist(ids, new CheckDuplicatesCallback(ids, destResourceMap, statList, fufList, submittedForms, formList));
        }
    }

    private final class CheckDuplicatesCallback extends DuplicateDiskResourceCallback {
        private final FastMap<IPCFileUploadField> destResourceMap;
        private final List<Status> statList;
        private final List<IPCFileUploadField> fufList;
        private final List<FormPanel> submittedForms;
        private final List<FormPanel> formList;

        public CheckDuplicatesCallback(List<String> ids, FastMap<IPCFileUploadField> destResourceMap, List<Status> statList,
                List<IPCFileUploadField> fufList, List<FormPanel> submittedForms, List<FormPanel> formList) {
            super(ids, null);
            this.destResourceMap = destResourceMap;
            this.statList = statList;
            this.fufList = fufList;
            this.submittedForms = submittedForms;
            this.formList = formList;
        }

        @Override
        public void markDuplicates(Collection<String> duplicates) {
            if ((duplicates != null) && !duplicates.isEmpty()) {
                for (String id : duplicates) {
                    destResourceMap.get(id).markInvalid(I18N.ERROR.fileExist());
                }
            } else {
                for (IPCFileUploadField field : destResourceMap.values()) {
                    int index = fufList.indexOf(field);
                    statList.get(index).setBusy("");
                    FormPanel form = formList.get(index);
                    form.addSubmitHandler(new SubmitHandler() {

                        @Override
                        public void onSubmit(SubmitEvent event) {
                            // TODO Auto-generated method stub
                            event.getSource();

                        }
                    });
                    form.submit();
                    submittedForms.add(form);
                }
            }

        }
    }
}
