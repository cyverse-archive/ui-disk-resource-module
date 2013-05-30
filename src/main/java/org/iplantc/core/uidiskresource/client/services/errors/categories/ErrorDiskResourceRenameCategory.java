package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.ErrorDiskResourceRename;

import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorDiskResourceRenameCategory {

    public static String generateErrorMsg(AutoBean<ErrorDiskResourceRename> instance) {
        ErrorDiskResourceRename renameErr = instance.as();

        return ErrorDiskResourceCategory.getErrorMessage(
                ErrorDiskResourceCategory.getDiskResourceErrorCode(renameErr.getErrorCode()),
                renameErr.getPath());
    }
}