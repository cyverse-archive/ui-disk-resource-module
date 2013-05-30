package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.ErrorDuplicateDiskResource;

import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorDuplicateDiskResourceCategory {

    public static String generateErrorMsg(AutoBean<ErrorDuplicateDiskResource> instance) {
        return ErrorDiskResourceCategory.getErrorMessage(
                ErrorDiskResourceCategory.getDiskResourceErrorCode(instance.as().getErrorCode()), null);
    }
}