package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uicommons.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.services.errors.ErrorDiskResourceMove;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorDiskResourceMoveCategory {

    public static SafeHtml generateErrorMsg(AutoBean<ErrorDiskResourceMove> instance) {
        ErrorDiskResourceMove error = instance.as();

        return ErrorDiskResourceCategory.getErrorMessage(
                ErrorDiskResourceCategory.getDiskResourceErrorCode(error.getErrorCode()),
                DiskResourceUtil.asCommaSeperatedNameList(error.getPaths()));
    }
}