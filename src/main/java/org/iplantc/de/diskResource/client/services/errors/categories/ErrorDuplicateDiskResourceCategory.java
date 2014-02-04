package org.iplantc.de.diskResource.client.services.errors.categories;

import org.iplantc.de.commons.client.util.DiskResourceUtil;
import org.iplantc.de.diskResource.client.services.errors.ErrorDuplicateDiskResource;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorDuplicateDiskResourceCategory {

    public static SafeHtml generateErrorMsg(AutoBean<ErrorDuplicateDiskResource> instance) {
        ErrorDuplicateDiskResource error = instance.as();

        return ErrorDiskResourceCategory.getErrorMessage(
                ErrorDiskResourceCategory.getDiskResourceErrorCode(error.getErrorCode()),
                DiskResourceUtil.asCommaSeperatedNameList(error.getPaths()));
    }
}