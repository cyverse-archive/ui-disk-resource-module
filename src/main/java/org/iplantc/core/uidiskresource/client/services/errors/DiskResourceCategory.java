package org.iplantc.core.uidiskresource.client.services.errors;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;

/**
 * The auto-bean category class for DiskResource error objects. This class is responsible for defining
 * the non-property "getErrorMsg" methods for each error object.
 * 
 * @author jstroot
 * 
 */
public class DiskResourceCategory {
    private static DiskResourceServiceErrorStrings errStrings = GWT.create(DiskResourceServiceErrorStrings.class);

    public static String getErrorMsg(AutoBean<ErrorCreateFolder> instance) {
        return getErrorMessage(DiskResourceErrorCode.valueOf(instance.as().getErrorCode()), null);
    }
    
    
    @SuppressWarnings("incomplete-switch")
    private static String getErrorMessage(DiskResourceErrorCode code, String resourceNames) {
        switch (code) {
            case ERR_DOES_NOT_EXIST:
                return errStrings.diskResourceDoesNotExist(resourceNames);
            case ERR_EXISTS:
                return errStrings.diskResourceExists(resourceNames);
            case ERR_NOT_WRITEABLE:
                return errStrings.diskResourceNotWriteable(resourceNames);
            case ERR_NOT_READABLE:
                return errStrings.diskResourceNotReadable(resourceNames);
            case ERR_WRITEABLE:
                return errStrings.diskResourceWriteable(resourceNames);
            case ERR_READABLE:
                return errStrings.diskResourceReadable(resourceNames);
            case ERR_NOT_A_USER:
                return errStrings.dataErrorNotAUser();
            case ERR_NOT_A_FILE:
                return errStrings.diskResourceNotAFile(resourceNames);
            case ERR_NOT_A_FOLDER:
                return errStrings.diskResourceNotAFolder(resourceNames);
            case ERR_IS_A_FILE:
                return errStrings.diskResourceIsAFile(resourceNames);
            case ERR_IS_A_FOLDER:
                return errStrings.diskResourceIsAFolder(resourceNames);
            case ERR_INVALID_JSON:
                return errStrings.dataErrorInvalidJson();
            case ERR_BAD_OR_MISSING_FIELD:
                return errStrings.dataErrorBadOrMissingField();
            case ERR_NOT_AUTHORIZED:
                return errStrings.dataErrorNotAuthorized();
            case ERR_MISSING_QUERY_PARAMETER:
                return errStrings.dataErrorMissingQueryParameter();
            case ERR_INCOMPLETE_DELETION:
                return errStrings.diskResourceIncompleteDeletion();
            case ERR_INCOMPLETE_MOVE:
                return errStrings.diskResourceIncompleteMove();
            case ERR_INCOMPLETE_RENAME:
                return errStrings.diskResourceIncompleteRename();
        }

        return null;
    }

}
