package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.DiskResourceErrorCode;
import org.iplantc.core.uidiskresource.client.services.errors.ErrorCreateFolder;

import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorCreateFolderCategory {

    public static String generateErrorMsg(AutoBean<ErrorCreateFolder> instance) {
        return ErrorDiskResourceCategory.getErrorMessage(DiskResourceErrorCode.valueOf(instance.as().getErrorCode()), null);
    }
}