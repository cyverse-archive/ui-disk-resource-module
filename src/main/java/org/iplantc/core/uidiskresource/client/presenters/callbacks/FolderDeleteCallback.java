package org.iplantc.core.uidiskresource.client.presenters.callbacks;

import java.util.Collection;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.views.IsMaskable;
import org.iplantc.core.uidiskresource.client.events.FoldersDeletedEvent;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;

public class FolderDeleteCallback extends DiskResourceDeleteCallback<Folder> {

    private final Collection<Folder> folders;

    public FolderDeleteCallback(final Collection<Folder> folders, final IsMaskable maskable) {
        super(folders, maskable);
        this.folders = folders;
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        // Fire event that folders were deleted.
        EventBus.getInstance().fireEvent(new FoldersDeletedEvent(folders));
    }

}
