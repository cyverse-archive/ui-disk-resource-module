package org.iplantc.de.diskResource.client.services.errors.categories;

import org.iplantc.de.commons.client.util.DiskResourceUtil;
import org.iplantc.de.diskResource.client.services.errors.ErrorDiskResourceDelete;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorDiskResourceDeleteCategory {

    public static SafeHtml generateErrorMsg(AutoBean<ErrorDiskResourceDelete> instance) {
        ErrorDiskResourceDelete error = instance.as();
        if(error.getLimit() > 0) {
            return ErrorDiskResourceCategory.getThresholdErrorMessage(
                    ErrorDiskResourceCategory.getDiskResourceErrorCode(error.getErrorCode()),error.getLimit());
        } else {
        return ErrorDiskResourceCategory.getErrorMessage(
                ErrorDiskResourceCategory.getDiskResourceErrorCode(error.getErrorCode()),
                DiskResourceUtil.asCommaSeperatedNameList(error.getPaths()));
        }
    }
}