package org.iplantc.de.diskResource.client.services.callbacks;

import org.iplantc.de.diskResource.client.services.errors.DiskResourceErrorAutoBeanFactory;
import org.iplantc.de.diskResource.client.services.errors.ErrorUpdateMetadata;
import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.info.SuccessAnnouncementConfig;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class DiskResourceMetadataUpdateCallback extends DiskResourceServiceCallback<String> {

    public DiskResourceMetadataUpdateCallback() {
        super(null);
    }
    
    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
        IplantAnnouncer.getInstance().schedule(new SuccessAnnouncementConfig(I18N.DISPLAY.metadataSuccess()));
    }

    @Override
    public void onFailure(Throwable caught) {
        unmaskCaller();
        DiskResourceErrorAutoBeanFactory factory = GWT.create(DiskResourceErrorAutoBeanFactory.class);
        AutoBean<ErrorUpdateMetadata> errorBean = AutoBeanCodex.decode(factory,
                ErrorUpdateMetadata.class, caught.getMessage());

        ErrorHandler.post(errorBean.as(), caught);

    }

    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.metadataUpdateFailed();
    }

}
