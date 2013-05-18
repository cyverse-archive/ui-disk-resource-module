package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.DiskResourceErrorCode;
import org.iplantc.core.uidiskresource.client.services.errors.ErrorDiskResourceRename;

import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorDiskResourceRenameCategory {

    public static String generateErrorMsg(AutoBean<ErrorDiskResourceRename> instance) {
        return ErrorDiskResourceCategory.getErrorMessage(DiskResourceErrorCode.valueOf(instance.as().getErrorCode()), instance.as().getPath());
    }
}