package org.iplantc.core.uidiskresource.client.views.dialogs;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.validators.NameValidator3;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantPromptDialog;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class RenameFileDialog extends IPlantPromptDialog {

    public RenameFileDialog(final File file, final DiskResourceViewToolbar.Presenter presenter) {
        super(I18N.DISPLAY.fileName(), -1, file.getName(), new NameValidator3());
        setHeadingText(I18N.DISPLAY.rename());

        addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.doRename(file, getFieldText());
            }
        });
    }
}
