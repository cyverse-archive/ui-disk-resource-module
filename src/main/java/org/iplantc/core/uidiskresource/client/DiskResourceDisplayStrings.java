package org.iplantc.core.uidiskresource.client;

import org.iplantc.core.uicommons.client.CommonUIDisplayStrings;

public interface DiskResourceDisplayStrings extends CommonUIDisplayStrings {
    String data();

    String download();

    String files();

    String folders();

    String newFolder();

    String bulkDownload();

    String simpleDownload();

    String metadata();

    String share();

    String importLabel();

    String bulkUploadFromDesktop();

    String simpleUploadFromDesktop();

    String selectAFile();

    String selectAFolder();

    String selectedFile();

    String selectedFolder();

    String deleteFilesTitle();

    String deleteFilesMsg();

    String folderName();

    String lastModified();

    String size();
    
    String dataDragDropStatusText(int numSelectedItems);

    /**
     * Text to be displayed when share / unshare request is submitted
     * 
     * @return
     */
    String sharingCompleteMsg();

    /**
     * shared with 0 people
     * 
     * @return
     */
    String nosharing();

    /**
     * select collabs
     * 
     * @return
     */
    String selectCollabs();

    /**
     * select files / folders
     * 
     * @return
     */
    String selectFilesFolders();

    /**
     * change permissions
     * 
     * @return
     */
    String changePermissions();

    /**
     * Manage sharing
     * 
     * @return
     */
    String manageSharing();

    /**
     * 
     * @return
     */
    String unshare();

    /**
     * 
     * @return
     */
    String dateSubmitted();

    /**
     * 
     * @return
     */
    String permissions();

    /**
     * 
     * @return
     */
    String readOnly();

    /**
     * 
     * @return
     */
    String readWrite();

    /**
     * 
     * @return
     */
    String owner();

    /**
     * 
     * @return
     */
    String noDetails();

    /**
     * restore message
     * 
     * @return
     */
    String restoreMsg();

    /**
     * delete message
     * 
     * @return
     */
    String deleteMsg();

    /**
     * empty trash warning msg
     * 
     * @return
     */
    String emptyTrashWarning();

    /**
     * empty trash label
     * 
     * @return
     */
    String emptyTrash();

    /**
     * Formats a message indicating the path of the folder receiving the files on upload.
     * 
     * @param folderId the path to the folder
     * 
     * @return the formatted message
     */
    String uploadingToFolder(String folderId);

    String urlPrompt();

    String searchHistory();

    /**
     * Localized display text for read
     * 
     * @return a string representing the localized text.
     */
    String read();

    /**
     * Localized display text for write
     * 
     * @return a string representing the localized text.
     */
    String write();

    /**
     * Localized display text for own
     * 
     * @return a string representing the localized text.
     */
    String own();

    /**
     * Localized display text for varies
     * 
     * @return a string representing the localized text.
     */
    String varies();

    /**
     * Localized display text for a variable permissions sharing notice.
     * 
     * @return a string representing the localized text.
     */
    String variablePermissionsNotice();

    /**
     * Localized display text for Explain.
     * 
     * @return a string representing the localized text.
     */
    String explain();

    /**
     * Localized display text for "Who has access".
     * 
     * @return a string representing the localized text.
     */
    String whoHasAccess();

    /**
     * Localized display text for "Group by User".
     * 
     * @return a string representing the localized text.
     */
    String groupByUser();

    /**
     * Localized display text for "Group by Data".
     * 
     * @return a string representing the localized text.
     */
    String groupByData();

    String selfShareWarning();
    
    String chooseFromCollab();
}
