package org.iplantc.core.uidiskresource.client.presenters.callbacks;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.views.IsMaskable;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.events.FolderCreatedEvent;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.models.autobeans.errors.CreateFolderError;
import org.iplantc.core.uidiskresource.client.models.autobeans.errors.DiskResourceErrorAutoBeanFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class CreateFolderCallback extends DiskResourceServiceCallback {

    private final Folder parentFolder;

    public CreateFolderCallback(final Folder parentFolder, IsMaskable maskable) {
        super(maskable);
        this.parentFolder = parentFolder;
    }

    @Override
    public void onSuccess(String result) {
        unmaskCaller();

        DiskResourceAutoBeanFactory factory = GWT.create(DiskResourceAutoBeanFactory.class);
        AutoBean<Folder> bean = AutoBeanCodex.decode(factory, Folder.class, result);
        EventBus.getInstance().fireEvent(new FolderCreatedEvent(parentFolder, bean.as()));
    }

    @Override
    public void onFailure(Throwable caught) {
        unmaskCaller();
        DiskResourceErrorAutoBeanFactory factory = GWT.create(DiskResourceErrorAutoBeanFactory.class);
        AutoBean<CreateFolderError> errorBean = AutoBeanCodex.decode(factory, CreateFolderError.class,
                caught.getMessage());

        ErrorHandler.post(errorBean.as(), caught);
    }

    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.createFolderFailed();
    }

    @Override
    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
        // TODO Auto-generated method stub
        return null;
    }

}
