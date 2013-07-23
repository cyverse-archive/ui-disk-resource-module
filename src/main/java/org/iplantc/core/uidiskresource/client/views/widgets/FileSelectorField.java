package org.iplantc.core.uidiskresource.client.views.widgets;

import java.util.List;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.events.UserSettingsUpdatedEvent;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.core.uicommons.client.models.diskresources.File;
import org.iplantc.core.uicommons.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.views.dialogs.FileSelectDialog;

import com.google.common.collect.Lists;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.TakesValue;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

public class FileSelectorField extends AbstractDiskResourceSelector<File> {

    UserSettings userSettings = UserSettings.getInstance();

    @Override
    protected void onBrowseSelected() {
        List<HasId> selected = null;

        HasId value = getValue();
        if (value != null) {
            selected = Lists.newArrayList();
            selected.add(value);
        }
        FileSelectDialog fileSD = null;
        if (selected != null && selected.size() > 0) {
            fileSD = FileSelectDialog.singleSelect(selected);
        } else {
            if (userSettings.isRememberLastPath()) {
                String id = userSettings.getLastPathId();
                if (id != null) {
                    fileSD = FileSelectDialog.selectParentFolderById(id);
                } else {
                    fileSD = FileSelectDialog.singleSelect(null);
                }
            } else {
                fileSD = FileSelectDialog.singleSelect(null);
            }
        }
        fileSD.addHideHandler(new FileDialogHideHandler(fileSD));
        fileSD.show();
    }

    private class FileDialogHideHandler implements HideHandler {
        private final TakesValue<List<File>> takesValue;

        public FileDialogHideHandler(TakesValue<List<File>> dlg) {
            this.takesValue = dlg;
        }

        @Override
        public void onHide(HideEvent event) {
            if ((takesValue.getValue() == null) || takesValue.getValue().isEmpty())
                return;

            // This class is single select, so only grab first element
            File selectedResource = takesValue.getValue().get(0);
            setSelectedResource(selectedResource);
            // cache the last used path
            if (userSettings.isRememberLastPath()) {
                userSettings.setLastPathId(DiskResourceUtil.parseParent(selectedResource.getId()));
                UserSettingsUpdatedEvent usue = new UserSettingsUpdatedEvent();
                EventBus.getInstance().fireEvent(usue);
            }
            ValueChangeEvent.fire(FileSelectorField.this, selectedResource);
        }
    }
}
