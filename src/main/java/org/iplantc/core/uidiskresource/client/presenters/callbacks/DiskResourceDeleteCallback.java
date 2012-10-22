package org.iplantc.core.uidiskresource.client.presenters.callbacks;

import java.util.Collection;

import org.iplantc.core.uicommons.client.views.IsMaskable;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;

import com.google.gwt.json.client.JSONObject;

public class DiskResourceDeleteCallback<T extends DiskResource> extends DiskResourceServiceCallback {

    public DiskResourceDeleteCallback(Collection<T> diskResources, IsMaskable maskedCaller) {
        super(maskedCaller);
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);
    }

    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.deleteFileFailed();
    }

    @Override
    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
        // TODO Auto-generated method stub
        return null;
    }

}
