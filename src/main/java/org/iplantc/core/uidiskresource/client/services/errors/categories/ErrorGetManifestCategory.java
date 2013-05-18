package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.DiskResourceErrorCode;
import org.iplantc.core.uidiskresource.client.services.errors.ErrorGetManifest;

import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorGetManifestCategory {

    public static String generateErrorMsg(AutoBean<ErrorGetManifest> instance) {
        return ErrorDiskResourceCategory.getErrorMessage(DiskResourceErrorCode.valueOf(instance.as().getErrorCode()), instance.as().getPath());
    }

}