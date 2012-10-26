package org.iplantc.core.uidiskresource.client.presenters;

import org.iplantc.core.uicommons.client.validators.NameValidator3;
import org.iplantc.core.uicommons.client.views.IsMaskable;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantPromptDialog;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.models.autobeans.File;
import org.iplantc.core.uidiskresource.client.presenters.callbacks.RenameDiskResourceCallback;
import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class RenameFileDialog extends IPlantPromptDialog {

    public RenameFileDialog(final File file, final IsMaskable maskable,
            final DiskResourceServiceFacade diskResourceService) {
        super(I18N.DISPLAY.fileName(), -1, file.getName(), new NameValidator3());
        setHeadingText(I18N.DISPLAY.rename());

        addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                maskable.mask(I18N.DISPLAY.loadingMask());
                diskResourceService.renameDiskResource(file, getFieldText(),
                        new RenameDiskResourceCallback(file, maskable));
            }
        });
    }
}
