package org.iplantc.core.uidiskresource.client.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.DiskResourceMetadata;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Folder;

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
     * @param parentFolder parent folder where the new folder will be created
     * @param callback executed when RPC call completes.
     */
    void createFolder(Folder parentFolder, String newFolderName, AsyncCallback<String> callback);

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
     * @param destFolder the destination folder where the disk resources will be moved.
     */
    void moveDiskResources(Set<DiskResource> diskResources, Folder destFolder, AsyncCallback<String> callback);

    /**
     * Call service rename a file or folder.
     * 
     * @param src
     * @param destName
     * @param callback service success/failure callback
     */
    void renameDiskResource(org.iplantc.core.uidiskresource.client.models.DiskResource src,
            String destName, AsyncCallback<String> callback);

    /**
     * Call service to upload a file from a given URL.
     * 
     * @param url
     * @param dest id of the destination folder.
     * @param callback service success/failure callback
     */
    void importFromUrl(String url, HasId dest, AsyncCallback<String> callback);

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
     * Call service to delete disk resources (i.e. {@link File}s and {@link Folder}s)
     * 
     * @param diskResources a set of <code>DiskResource</code>s to be deleted
     * @param callback callback executed when service call completes.
     */
    <T extends DiskResource> void deleteDiskResources(Set<T> diskResources, AsyncCallback<String> callback);

    /**
     * @param resource the <code>DiskResource</code> for which metadata will be retrieved.
     * @param callback callback executed when service call completes.
     */
    void getDiskResourceMetaData(DiskResource resource, AsyncCallback<String> callback);

    /**
     * Calls service to set disk resource metadata.
     * 
     * @param resource the <code>DiskResource</code> whose metadata will be updated
     * @param mdToUpdate a list of <code>DiskResourceMetadata</code> objects which will be updated
     * @param mdToDelete a list of <code>DiskResourceMetadata</code> objects which will be deleted
     * @param callback executed when the service call completes.
     */
    void setDiskResourceMetaData(DiskResource resource, Set<DiskResourceMetadata> mdToUpdate, Set<DiskResourceMetadata> mdToDelete,
            AsyncCallback<String> callback);

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

    /**
     * search users irods directory structure
     * 
     * @param term search term
     * @param size limit for results to return
     * @param type file or folder
     * @param callback callback object
     */
    void search(String term, int size, String type, AsyncCallback<String> callback);
    
    /**
     * Get info about a selected file or folder
     * 
     * @param body request body
     * @param callback callback object
     */
    void getStat(String body, AsyncCallback<String> callback);
    
    /**
     * get data search history
     * 
     * @param callback callback object
     * 
     */
    void getDataSearchHistory(AsyncCallback<String> callback);

    /**
     * save users data search history
     * 
     * @param body json object search history
     * @param callback callback object
     */
    void saveDataSearchHistory(String body, AsyncCallback<String> callback);

    /**
     * empty user's trash
     * 
     * @param user
     * @param callback
     */
    public void emptyTrash(String user, AsyncCallback<String> callback);
    
    /**
    * get users trash path
    *
    * @param userName
    * @param callback
    */
    public void getUserTrashPath(String userName, AsyncCallback<String> callback);

    /**
     * restore a deleted disk resources
     * 
     * @param body
     * @param callback
     */
    public void restoreDiskResource(JSONObject body, AsyncCallback<String> callback);

    /**
     * Creates a set of public data links for the given disk resources.
     * 
     * @param ticketIdToResourceIdMap the id of the disk resource for which the ticket will be created.
     * @param isPublicTicket
     * @param callback
     */
    public void createDataLinks(Map<String, String> ticketIdToResourceIdMap,
            AsyncCallback<String> callback);

    /**
     * Requests a listing of all the tickets for the given disk resources.
     * 
     * @param diskResourceIds the disk resources whose tickets will be listed.
     * @param callback
     */
    public void listDataLinks(List<String> diskResourceIds, AsyncCallback<String> callback);
    /**
     * Requests that the given Kif Share tickets will be deleted.
     * 
     * @param dataLinkIds the tickets which will be deleted.
     * @param callback
     */
    public void deleteDataLinks(List<String> dataLinkIds, AsyncCallback<String> callback);
}