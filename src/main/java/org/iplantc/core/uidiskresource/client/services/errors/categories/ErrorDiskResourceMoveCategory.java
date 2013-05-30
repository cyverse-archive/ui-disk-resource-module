package org.iplantc.core.uidiskresource.client.services.errors.categories;

import org.iplantc.core.uidiskresource.client.services.errors.ErrorDiskResourceMove;

import com.google.common.base.Joiner;
import com.google.web.bindery.autobean.shared.AutoBean;

public class ErrorDiskResourceMoveCategory {

    public static String generateErrorMsg(AutoBean<ErrorDiskResourceMove> instance) {
        ErrorDiskResourceMove moveErr = instance.as();
        String paths = Joiner.on(',').join(moveErr.getPaths());

        return ErrorDiskResourceCategory.getErrorMessage(
                ErrorDiskResourceCategory.getDiskResourceErrorCode(moveErr.getErrorCode()), paths);
    }
}