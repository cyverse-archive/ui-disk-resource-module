package org.iplantc.core.uidiskresource.client.views.dialogs;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.validators.NameValidator3;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantPromptDialog;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class CreateFolderDialog extends IPlantPromptDialog {

    public CreateFolderDialog(final Folder parentFolder,
            final DiskResourceViewToolbar.Presenter presenter) {
        super(I18N.DISPLAY.folderName(), -1, "", new NameValidator3());
        setHeadingText(I18N.DISPLAY.newFolder());

        addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.doCreateNewFolder(parentFolder, getFieldText());
            }
        });

    }

}
