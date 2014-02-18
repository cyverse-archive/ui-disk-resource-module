package org.iplantc.de.diskResource.client.services.errors.categories;

import org.iplantc.core.uicommons.client.util.DiskResourceUtil;
import org.iplantc.de.diskResource.client.services.errors.ErrorDiskResourceRename;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorDiskResourceRenameCategory {

    public static SafeHtml generateErrorMsg(AutoBean<ErrorDiskResourceRename> instance) {
        ErrorDiskResourceRename error = instance.as();

        if(error.getLimit() > 0) {
            return ErrorDiskResourceCategory.getThresholdErrorMessage(
                    ErrorDiskResourceCategory.getDiskResourceErrorCode(error.getErrorCode()),error.getLimit());
        } else {
        return ErrorDiskResourceCategory.getErrorMessage(
                ErrorDiskResourceCategory.getDiskResourceErrorCode(error.getErrorCode()),
                DiskResourceUtil.parseNameFromPath(error.getPath()));
        }
    }
}