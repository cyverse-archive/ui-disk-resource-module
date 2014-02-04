package org.iplantc.de.diskResource.client.services.callbacks;

import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.events.EventBus;
import org.iplantc.de.commons.client.models.diskresources.DiskResource;
import org.iplantc.de.commons.client.views.IsMaskable;
import org.iplantc.de.diskResource.client.events.DiskResourceRenamedEvent;
import org.iplantc.de.diskResource.client.services.errors.DiskResourceErrorAutoBeanFactory;
import org.iplantc.de.diskResource.client.services.errors.ErrorDiskResourceRename;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class RenameDiskResourceCallback extends DiskResourceServiceCallback<DiskResource> {

    private final DiskResource dr;

    public RenameDiskResourceCallback(DiskResource dr, IsMaskable maskable) {
        super(maskable);
        this.dr = dr;
    }

    @Override
    public void onSuccess(DiskResource result) {
        unmaskCaller();
        EventBus.getInstance().fireEvent(new DiskResourceRenamedEvent(dr, result));
    }

    @Override
    public void onFailure(Throwable caught) {
        unmaskCaller();
        DiskResourceErrorAutoBeanFactory factory = GWT.create(DiskResourceErrorAutoBeanFactory.class);
        String errMessage = caught.getMessage();
        if (JsonUtils.safeToEval(errMessage)) {
            AutoBean<ErrorDiskResourceRename> errorBean = AutoBeanCodex.decode(factory,
                    ErrorDiskResourceRename.class, errMessage);

            ErrorHandler.post(errorBean.as(), caught);
        } else {
            ErrorHandler.post(caught);
        }


    }

    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.renameFailed();
    }

}
