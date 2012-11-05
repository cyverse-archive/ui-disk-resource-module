package org.iplantc.core.uidiskresource.client;

import org.iplantc.core.uicommons.client.CommonUIErrorStrings;

public interface DiskResourceErrorStrings extends CommonUIErrorStrings {

    String diskResourceIncompleteRename();

    String diskResourceIncompleteMove();

    String diskResourceIncompleteDeletion();

    String dataErrorMissingQueryParameter();

    String dataErrorNotAuthorized();

    String dataErrorBadOrMissingField();

    String dataErrorInvalidJson();

    String diskResourceIsAFolder(String resourceNames);

    String diskResourceIsAFile(String resourceNames);

    String diskResourceNotAFolder(String resourceNames);

    String diskResourceNotAFile(String resourceNames);

    String dataErrorNotAUser();

    String diskResourceReadable(String resourceNames);

    String diskResourceWriteable(String resourceNames);

    String diskResourceNotReadable(String resourceNames);

    String diskResourceNotWriteable(String resourceNames);

    String diskResourceExists(String resourceNames);

    String diskResourceDoesNotExist(String resourceNames);

    String folderDoesNotExist(String folderNames);

    String folderExists(String folderNames);

    String folderNotWriteable(String folderNames);

    String folderNotReadable(String folderNames);

    String folderWriteable(String folderNames);

    String folderReadable(String folderNames);

    String fileDoesNotExist(String fileNames);

    String fileExists(String fileNames);

    String fileNotWriteable(String fileNames);

    String fileNotReadable(String fileNames);

    String fileWriteable(String fileNames);

    String fileReadable(String fileNames);

    String serviceErrorStatus(String status);

    String serviceErrorCode(String errCode);

    String serviceErrorReason(String reason);

    String renameFolderFailed();

    String renameFileFailed();

    String deleteFolderFailed();

    String deleteFileFailed();

    String createFolderFailed();

    String permissionErrorTitle();

    String permissionErrorMessage();

    String metadataUpdateFailed();

}
