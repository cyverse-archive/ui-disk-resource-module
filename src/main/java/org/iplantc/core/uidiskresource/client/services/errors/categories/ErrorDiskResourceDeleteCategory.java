package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.ErrorDiskResourceDelete;

import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorDiskResourceDeleteCategory extends ErrorDiskResourceCategory {

    public static String generateErrorMsg(AutoBean<ErrorDiskResourceDelete> instance) {
        return getErrorMessage(getDiskResourceErrorCode(instance.as().getErrorCode()), null);
    }
}