package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.ErrorGetManifest;

import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorGetManifestCategory extends ErrorDiskResourceCategory {

    public static String generateErrorMsg(AutoBean<ErrorGetManifest> instance) {
        ErrorGetManifest error = instance.as();
        return getErrorMessage(getDiskResourceErrorCode(error.getErrorCode()), error.getPath());
    }

}