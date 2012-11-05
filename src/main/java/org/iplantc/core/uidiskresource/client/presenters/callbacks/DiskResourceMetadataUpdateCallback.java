package org.iplantc.core.uidiskresource.client.presenters.callbacks;

import org.iplantc.core.uidiskresource.client.I18N;

import com.google.gwt.json.client.JSONObject;

public class DiskResourceMetadataUpdateCallback extends DiskResourceServiceCallback {

    public DiskResourceMetadataUpdateCallback() {
        super(null);
    }

    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.metadataUpdateFailed();
    }

    @Override
    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
        return null;
    }

}
