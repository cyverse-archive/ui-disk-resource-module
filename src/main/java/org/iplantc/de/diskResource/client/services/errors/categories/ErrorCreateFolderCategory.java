package org.iplantc.de.diskResource.client.services.errors.categories;

import org.iplantc.de.commons.client.util.DiskResourceUtil;
import org.iplantc.de.diskResource.client.services.errors.ErrorCreateFolder;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorCreateFolderCategory {

    public static SafeHtml generateErrorMsg(AutoBean<ErrorCreateFolder> instance) {
        ErrorCreateFolder error = instance.as();

        return ErrorDiskResourceCategory.getErrorMessage(
                ErrorDiskResourceCategory.getDiskResourceErrorCode(error.getErrorCode()),
                DiskResourceUtil.parseNameFromPath(error.getPath()));
    }
}