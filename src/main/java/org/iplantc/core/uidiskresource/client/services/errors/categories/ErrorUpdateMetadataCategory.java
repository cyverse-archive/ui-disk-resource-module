package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.ErrorUpdateMetadata;

import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorUpdateMetadataCategory extends ErrorDiskResourceCategory {

    public static String generateErrorMsg(AutoBean<ErrorUpdateMetadata> instance) {
        return getErrorMessage(getDiskResourceErrorCode(instance.as().getErrorCode()), null);
    }
}