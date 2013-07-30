package org.iplantc.core.uidiskresource.client.views.widgets;

import java.util.Set;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.events.UserSettingsUpdatedEvent;
import org.iplantc.core.uicommons.client.models.CommonModelUtils;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.views.dialogs.FolderSelectDialog;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.TakesValue;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.StatusProxy;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

public class FolderSelectorField extends AbstractDiskResourceSelector<Folder> {

    UserSettings userSettings = UserSettings.getInstance();

    public FolderSelectorField() {
        setEmptyText(I18N.DISPLAY.selectAFolder());
    }

    @Override
    protected void onBrowseSelected() {
        HasId value = getValue();
        FolderSelectDialog folderSD = null;
        if (value != null) {
            folderSD = new FolderSelectDialog(value);
        } else {
            if (userSettings.isRememberLastPath()) {
                String id = userSettings.getLastPathId();
                if (id != null) {
                    folderSD = new FolderSelectDialog(CommonModelUtils.createHasIdFromString(id));
                } else {
                    folderSD = new FolderSelectDialog(null);
                }
            } else {
                folderSD = new FolderSelectDialog(null);
            }
        }
        folderSD.addHideHandler(new FolderDialogHideHandler(folderSD));
        folderSD.show();
    }

    @Override
    public void setValue(HasId value) {
        super.setValue(value);
    }

    private class FolderDialogHideHandler implements HideHandler {
        private final TakesValue<Folder> takesValue;

        public FolderDialogHideHandler(TakesValue<Folder> dlg) {
            this.takesValue = dlg;
        }

        @Override
        public void onHide(HideEvent event) {
            Folder value = takesValue.getValue();
            if (value == null)
                return;

            setSelectedResource(value);
            // cache the last used path
            if (userSettings.isRememberLastPath()) {
                userSettings.setLastPathId(value.getId());
                UserSettingsUpdatedEvent usue = new UserSettingsUpdatedEvent();
                EventBus.getInstance().fireEvent(usue);
            }
            ValueChangeEvent.fire(FolderSelectorField.this, value);
        }
    }

    @Override
    protected boolean validateDropStatus(Set<DiskResource> dropData, StatusProxy status) {
        // Only allow 1 folder to be dropped in this field.
        if (dropData == null || dropData.size() != 1 || !(DiskResourceUtil.containsFolder(dropData))) {
            status.setStatus(false);
            return false;
        }

        // Reset status message
        status.setStatus(true);
        status.update(I18N.DISPLAY.dataDragDropStatusText(dropData.size()));

        return true;
    }

    @Override
    public void onDrop(DndDropEvent event) {
        Set<DiskResource> dropData = getDropData(event.getData());

        if (validateDropStatus(dropData, event.getStatusProxy())) {
            Folder selectedFolder = (Folder)dropData.iterator().next();
            setSelectedResource(selectedFolder);
            ValueChangeEvent.fire(this, selectedFolder);
        }
    }
}
