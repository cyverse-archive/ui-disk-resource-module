package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.diskResource.client.views.widgets.DiskResourceViewToolbar;
import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.de.commons.client.models.diskresources.Folder;
import org.iplantc.de.commons.client.validators.DiskResourceNameValidator;
import org.iplantc.de.commons.client.views.gxt3.dialogs.IPlantPromptDialog;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class RenameFolderDialog extends IPlantPromptDialog {

    public RenameFolderDialog(final Folder folder, final DiskResourceViewToolbar.Presenter presenter) {
        super(I18N.DISPLAY.folderName(), -1, folder.getName(), new DiskResourceNameValidator());
        setHeadingText(I18N.DISPLAY.rename());
        addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.doRename(folder, getFieldText());
            }
        });
    }
}