package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.ErrorCreateFolder;

import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorCreateFolderCategory extends ErrorDiskResourceCategory {

    public static String generateErrorMsg(AutoBean<ErrorCreateFolder> instance) {
        return getErrorMessage(getDiskResourceErrorCode(instance.as().getErrorCode()), null);
    }
}