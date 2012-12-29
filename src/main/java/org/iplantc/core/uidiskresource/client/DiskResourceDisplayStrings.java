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

}
