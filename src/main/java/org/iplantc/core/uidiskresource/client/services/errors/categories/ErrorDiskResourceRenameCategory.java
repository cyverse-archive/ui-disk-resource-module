package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.DiskResourceErrorCode;
import org.iplantc.core.uidiskresource.client.services.errors.ErrorDiskResourceRename;

import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorDiskResourceRenameCategory extends ErrorDiskResourceCategory {

    public static String generateErrorMsg(AutoBean<ErrorDiskResourceRename> instance) {
        ErrorDiskResourceRename renameErr = instance.as();

        String path = renameErr.getPath();
        DiskResourceErrorCode errCode = getDiskResourceErrorCode(renameErr.getErrorCode());

        return getErrorMessage(errCode, path);
    }
}