package org.iplantc.core.uidiskresource.client.presenters.callbacks;

import java.util.Collection;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.views.IsMaskable;
import org.iplantc.core.uidiskresource.client.events.FilesDeletedEvent;
import org.iplantc.core.uidiskresource.client.models.autobeans.File;

public class FileDeleteCallback extends DiskResourceDeleteCallback<File> {

    private final Collection<File> files;

    public FileDeleteCallback(final Collection<File> files, final IsMaskable maskable) {
        super(files, maskable);
        this.files = files;
    }


    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        // Fire event that files were deleted
        EventBus.getInstance().fireEvent(new FilesDeletedEvent(files));
    }

}
