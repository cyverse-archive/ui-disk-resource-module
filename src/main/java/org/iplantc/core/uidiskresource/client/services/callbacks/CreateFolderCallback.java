package org.iplantc.core.uidiskresource.client.services.callbacks;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceAutoBeanFactory;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.views.IsMaskable;
import org.iplantc.core.uidiskresource.client.events.FolderCreatedEvent;
import org.iplantc.core.uidiskresource.client.services.errors.DiskResourceErrorAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.services.errors.ErrorCreateFolder;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

public class CreateFolderCallback extends DiskResourceServiceCallback<String> {

    private final Folder parentFolder;
    private final String newName;

    public CreateFolderCallback(final Folder parentFolder, final IsMaskable maskable,
            final String newName) {
        super(maskable);
        this.newName = newName;
        this.parentFolder = parentFolder;
    }

    @Override
    public void onSuccess(String result) {
        unmaskCaller();

        DiskResourceAutoBeanFactory factory = GWT.create(DiskResourceAutoBeanFactory.class);
        Splittable splittable = StringQuoter.split(result);
        AutoBean<Folder> bean = AutoBeanCodex.decode(factory, Folder.class, result);

        // Set the new folder name since the create folder service call result does not contain the name
        // of the new folder
        bean.as().setName(newName);

        // Use the service call result to set the ID of the new folder. Otherwise, calls to getId() on
        // this new folder instance will return null.
        bean.as().setId(splittable.get("path").asString());

        EventBus.getInstance().fireEvent(new FolderCreatedEvent(parentFolder, bean.as()));
    }

    @Override
    public void onFailure(Throwable caught) {
        unmaskCaller();
        DiskResourceErrorAutoBeanFactory factory = GWT.create(DiskResourceErrorAutoBeanFactory.class);
        AutoBean<ErrorCreateFolder> errorBean = AutoBeanCodex.decode(factory, ErrorCreateFolder.class,
                caught.getMessage());

        ErrorHandler.post(errorBean.as(), caught);
    }

    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.createFolderFailed();
    }

}
