package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.DiskResourceErrorCode;
import org.iplantc.core.uidiskresource.client.services.errors.ErrorDiskResourceDelete;

import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorDiskResourceDeleteCategory {

    public static String generateErrorMsg(AutoBean<ErrorDiskResourceDelete> instance) {
        return ErrorDiskResourceCategory.getErrorMessage(DiskResourceErrorCode.valueOf(instance.as().getErrorCode()), null);
    }
}