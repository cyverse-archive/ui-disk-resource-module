package org.iplantc.core.uidiskresource.client.services.callbacks;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uidiskresource.client.services.errors.DiskResourceErrorAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.services.errors.ErrorUpdateMetadata;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class DiskResourceMetadataUpdateCallback extends DiskResourceServiceCallback {

    public DiskResourceMetadataUpdateCallback() {
        super(null);
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
