package org.iplantc.core.uidiskresource.client.views.dialogs;

import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.models.CommonModelAutoBeanFactory;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.core.uidiskresource.client.gin.DiskResourceInjector;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class SaveAsDialog extends IPlantDialog {

    private final DiskResourceView.Presenter presenter;
    private final TextField selectedFolderField = new TextField();
    private final TextField fileNameField = new TextField();
    private Folder selectedFolder = null;

    public SaveAsDialog() {
        // Get a reference to the OK button, and disable it by default.
        TextButton okButton = null;
        Widget w = getButtonBar().getItemByItemId(PredefinedButton.OK.name());
        if ((w != null) && (w instanceof TextButton)) {
            okButton = (TextButton)w;
            okButton.setEnabled(false);
        }

        selectedFolderField.setReadOnly(true);
        fileNameField.setAllowBlank(false);
        fileNameField.setAutoValidate(true);

        initDialog();
        
        addKeyHandlers(okButton);

        presenter = DiskResourceInjector.INSTANCE.getDiskResourceViewPresenter();

        final FieldLabel fl1 = new FieldLabel(selectedFolderField, I18N.DISPLAY.selectedFolder());
        final FieldLabel fl2 = new FieldLabel(fileNameField, I18N.DISPLAY.fileName());
        fileNameField
                .addValueChangeHandler(new FileNameValueChangeHandler(okButton, selectedFolderField));

        VerticalLayoutContainer vlc = buildLayout(fl1, fl2);

        initPresenter(okButton, vlc);

        setDefaultSelectedFolder();

    }

    private void setDefaultSelectedFolder() {
        // if not refresh and currently nothing was selected and remember path is enabled, the go
        // back to last selected folder
        UserSettings instance = UserSettings.getInstance();
        String id = instance.getDefaultFileSelectorPath();
        boolean remember = instance.isRememberLastPath();
        if (remember && !Strings.isNullOrEmpty(id)) {
            CommonModelAutoBeanFactory factory = GWT.create(CommonModelAutoBeanFactory.class);
            HasId folderAb = AutoBeanCodex.decode(factory, HasId.class, "{\"id\": \"" + id + "\"}").as();
            presenter.setSelectedFolderById(folderAb);
        }
    }

    private void initPresenter(TextButton okButton, VerticalLayoutContainer vlc) {
        presenter.getView().setSouthWidget(vlc, 60);
        presenter.addFolderSelectionHandler(new FolderSelectionChangedHandler(selectedFolderField,
                okButton, fileNameField));

        // Tell the presenter to add the view with the north, east, and center widgets hidden.
        // presenter.go(this, false, true, true, true);
        presenter.builder().hideNorth().hideCenter().hideEast().singleSelect().go(this);
    }

    private VerticalLayoutContainer buildLayout(final FieldLabel fl1, final FieldLabel fl2) {
        VerticalLayoutContainer vlc = new VerticalLayoutContainer();
        vlc.add(fl1, new VerticalLayoutData(1, -1));
        vlc.add(fl2, new VerticalLayoutData(1, -1));
        return vlc;
    }

    private void addKeyHandlers(final TextButton okBtn) {
        fileNameField.addKeyDownHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (isVaild() && (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)) {
                    onButtonPressed(okBtn);
                }

            }
        });

        fileNameField.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                okBtn.setEnabled(isVaild());

            }
        });
    }

    private void initDialog() {
        setResizable(true);
        setSize("480", "425");
        setHeadingText(I18N.DISPLAY.saveAs());
    }

    public void cleanUp() {
        presenter.cleanUp();
    }

    @Override
    public void onHide() {
        cleanUp();
    }

    private final class FileNameValueChangeHandler implements ValueChangeHandler<String> {
        private final HasEnabled okButton;
        private final HasValue<String> selectedFolderField;

        public FileNameValueChangeHandler(final HasEnabled okButton,
                final HasValue<String> selectedFolderField) {
            this.okButton = okButton;
            this.selectedFolderField = selectedFolderField;
        }

        @Override
        public void onValueChange(ValueChangeEvent<String> event) {
            okButton.setEnabled(!Strings.isNullOrEmpty(event.getValue())
                    && !Strings.isNullOrEmpty(selectedFolderField.getValue()));
        }
    }
    
    private boolean isVaild() {
      return   !Strings.isNullOrEmpty(fileNameField.getCurrentValue())
        && !Strings.isNullOrEmpty(selectedFolderField.getValue());
    }

    private final class FolderSelectionChangedHandler implements SelectionHandler<Folder> {
        private final HasValue<String> textBox;
        private final HasEnabled okButton;
        private final HasValue<String> fileNameTextBox;

        private FolderSelectionChangedHandler(final HasValue<String> folderTextBox,
                final HasEnabled okButton, final HasValue<String> fileNameTextBox) {
            this.textBox = folderTextBox;
            this.okButton = okButton;
            this.fileNameTextBox = fileNameTextBox;
        }

        @Override
        public void onSelection(SelectionEvent<Folder> event) {
            if (event.getSelectedItem() == null) {
                // Disable the okButton
                okButton.setEnabled(false);
                return;
            }
            selectedFolder = event.getSelectedItem();
            textBox.setValue(selectedFolder.getId());
            // Enable the okButton
            okButton.setEnabled(!Strings.isNullOrEmpty(fileNameTextBox.getValue()));
        }
    }

    public String getFileName() {
        return fileNameField.getCurrentValue();
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }

}
