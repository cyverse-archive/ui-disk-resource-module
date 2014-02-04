package org.iplantc.de.diskResource.client.services.errors.categories;

import org.iplantc.de.commons.client.util.DiskResourceUtil;
import org.iplantc.de.diskResource.client.services.errors.ErrorUpdateMetadata;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorUpdateMetadataCategory {

    public static SafeHtml generateErrorMsg(AutoBean<ErrorUpdateMetadata> instance) {
        ErrorUpdateMetadata error = instance.as();

        return ErrorDiskResourceCategory.getErrorMessage(
                ErrorDiskResourceCategory.getDiskResourceErrorCode(error.getErrorCode()),
                DiskResourceUtil.parseNameFromPath(error.getPath()));
    }
}