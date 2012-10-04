package org.iplantc.core.uidiskresource.client.services;

import java.util.List;

import org.iplantc.core.uidiskresource.client.models.DiskResource;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DiskResourceServiceFacade {

    /**
     * Call service to retrieve the root folder info for the current user
     * 
     * @param callback executed when RPC call completes.
     */
    void getHomeFolder(AsyncCallback<String> callback);

    /**
     * get user's default analyses output folder
     * 
     * @param folderName
     * @param callback
     */
    void getDefaultOutput(String folderName, AsyncCallback<String> callback);

    /**
     * set user's default analyses output folder
     * 
     * @param folderName
     * @param callback
     */
    void putDefaultOutput(AsyncCallback<String> callback);

    /**
     * Called to retrieve the entire contents of a folder.
     * 
     * @param path path to requested folder.
     * @param callback executed when RPC call completes.
     */
    void getFolderContents(String path, AsyncCallback<String> callback);

    /**
     * Called to retrieve the contents of a folder, with or without its file listing.
     * 
     * @param path path to requested folder.
     * @param includeFiles whether or not to include the file listing of the given folder
     * @param callback executed when RPC call completes.
     */
    void getFolderContents(String path, boolean includeFiles, AsyncCallback<String> callback);

    /**
     * Call service to create a new folder
     * 
     * @param folderpath path of the folder to be created
     * @param callback executed when RPC call completes.
     */
    void createFolder(String folderpath, AsyncCallback<String> callback);

    /**
     * Check if a list of files or folders exist.
     * 
     * @param diskResourceIds paths to desired resources.
     * @param callback callback executed when RPC call completes.
     */
    void diskResourcesExist(List<String> diskResourceIds, AsyncCallback<String> callback);

    /**
     * Fetch preview data for a file.
     * 
     * @param path path to desired file.
     * @param callback callback executed when RPC call completes.
     */
    void previewFile(String path, AsyncCallback<String> callback);

    /**
     * Calls the move folder and move file services for the list of given disk resource ids.
     * 
     * @param diskResources list of file and folder ids to move.
     * @param idDestFolder id of the destination folder.
     */
    void moveDiskResources(List<DiskResource> diskResources, String idDestFolder,
            AsyncCallback<String> callback);

    /**
     * Call service to move the given file ids to the given folder.
     * 
     * @param idSrcFiles list of file ids to move.
     * @param idDestFolder id of the destination folder.
     * @param callback service success/failure callback
     */
    void moveFile(List<String> idSrcFiles, String idDestFolder, AsyncCallback<String> callback);

    /**
     * Call service to move the given folder ids to the given destination folder.
     * 
     * @param idSrcFolders list of folder ids to move.
     * @param idDestFolder id of the destination folder.
     * @param callback service success/failure callback
     */
    void moveFolder(List<String> idSrcFolders, String idDestFolder, AsyncCallback<String> callback);

    /**
     * Call service rename a folder.
     * 
     * @param srcName
     * @param destName
     * @param callback service success/failure callback
     */
    void renameFolder(String srcName, String destName, AsyncCallback<String> callback);

    /**
     * Call service to rename a file.
     * 
     * @param srcId
     * @param destId
     * @param callback service success/failure callback
     */
    void renameFile(String srcId, String destId, AsyncCallback<String> callback);

    /**
     * Call service to upload a file from a given URL.
     * 
     * @param url
     * @param dest id of the destination folder.
     * @param description description of the file to upload.
     * @param callback service success/failure callback
     */
    void importFromUrl(String url, String dest, String description, AsyncCallback<String> callback);

    /**
     * Call service to retrieve upload configuration values for idrop-lite.
     * 
     * @param callback executed when RPC call completes.
     */
    void upload(AsyncCallback<String> callback);

    /**
     * Call service to retrieve upload configuration values for idrop-lite.
     * 
     * @param callback executed when RPC call completes.
     */
    void download(JSONArray paths, AsyncCallback<String> callback);

    /**
     * Opens a window to download the file with the given path.
     * 
     * @param path Path of the file to download.
     */
    void simpleDownload(String path);

    /**
     * Call service to delete folders.
     * 
     * @param pathsAsJsonArray
     * @param callback executed when RPC call completes.
     */
    void deleteFolders(String pathsAsJsonArray, AsyncCallback<String> callback);

    /**
     * Call service to delete folders.
     * 
     * @param pathsAsJsonArray
     * @param callback executed when RPC call completes.
     */
    void deleteFiles(String pathsAsJsonArray, AsyncCallback<String> callback);

    /**
     * call service to get file metadata
     * 
     * @param path path of resource
     * @param callback execute when RPC call complete
     */
    void getFileMetaData(String path, AsyncCallback<String> callback);

    /**
     * call service to get folder metadata
     * 
     * @param path path of resource
     * @param callback execute when RPC call complete
     */
    void getFolderMetaData(String path, AsyncCallback<String> callback);

    /**
     * call service to set folder metadata
     * 
     * @param folderId id of folder resource
     * @param body metadata in json format
     * @param callback execute when RPC call complete
     */
    void setFolderMetaData(String folderId, String body, AsyncCallback<String> callback);

    /**
     * call service to set file metadata
     * 
     * @param fileId id of file resource
     * @param body metadata in json format
     * @param callback execute when RPC call complete
     */
    void setFileMetaData(String fileId, String body, AsyncCallback<String> callback);

    /**
     * 
     * Share a resource with give user with permission
     * 
     * 
     * @param body - Post body in JSONObject format
     * @param callback callback object
     */
    void shareDiskResource(JSONObject body, AsyncCallback<String> callback);

    /**
     * UnShare a resource with give user with permission
     * 
     * @param body - Post body in JSONObject format
     * @param callback callback object
     */
    void unshareDiskResource(JSONObject body, AsyncCallback<String> callback);

    /**
     * get user permission info on selected disk resources
     * 
     * @param body - Post body in JSONObject format
     * @param callback callback object
     */
    void getPermissions(JSONObject body, AsyncCallback<String> callback);

}