package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.DiskResourceErrorCode;
import org.iplantc.core.uidiskresource.client.services.errors.ErrorUpdateMetadata;

import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorUpdateMetadataCategory {

    public static String generateErrorMsg(AutoBean<ErrorUpdateMetadata> instance) {
        return ErrorDiskResourceCategory.getErrorMessage(DiskResourceErrorCode.valueOf(instance.as().getErrorCode()), null);
    }
}