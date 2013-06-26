package org.iplantc.core.uidiskresource.client.views.dialogs;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.validators.NameValidator3;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantPromptDialog;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;

import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

public class CreateFolderDialog extends IPlantPromptDialog {

    public CreateFolderDialog(final Folder parentFolder,
            final DiskResourceViewToolbar.Presenter presenter) {
        super(I18N.DISPLAY.folderName(), -1, "", new NameValidator3());

        setHeadingText(I18N.DISPLAY.newFolder());
        initDestPathLabel(parentFolder.getId());

        addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.doCreateNewFolder(parentFolder, getFieldText());
            }
        });

    }

    private void initDestPathLabel(String destPath) {
        HTML htmlDestText = new HTML(Format.ellipse(I18N.DISPLAY.createIn(destPath), 50));
        insert(htmlDestText, 0);
        new ToolTip(htmlDestText, new ToolTipConfig(destPath));
    }
}
