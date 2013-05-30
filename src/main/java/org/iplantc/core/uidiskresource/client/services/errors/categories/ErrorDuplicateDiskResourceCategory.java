package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.ErrorDuplicateDiskResource;

import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorDuplicateDiskResourceCategory extends ErrorDiskResourceCategory {

    public static String generateErrorMsg(AutoBean<ErrorDuplicateDiskResource> instance) {
        return getErrorMessage(getDiskResourceErrorCode(instance.as().getErrorCode()), null);
    }
}