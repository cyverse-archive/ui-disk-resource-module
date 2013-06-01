package org.iplantc.core.uidiskresource.client.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.DEClientConstants;
import org.iplantc.core.uicommons.client.DEServiceFacade;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.util.WindowUtil;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.DiskResourceMetadata;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.models.HasPaths;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sencha.gxt.core.client.util.Format;

/**
 * Provides access to remote services for folder operations.
 *
 * @author amuir
 *
 */
public class DiskResourceServiceFacadeImpl implements DiskResourceServiceFacade {

    @Override
    public void getHomeFolder(AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getDataMgmtBaseUrl() + "root"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        callService(callback, wrapper);
    }

    @Override
    public void getDefaultOutput(final String folderName, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "default-output-dir?name="
                + folderName;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
    public void putDefaultOutput(AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "default-output-dir?name="
                + DEProperties.getInstance().getDefaultOutputFolderName();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                "{\"path\":\"" + DEProperties.getInstance().getDefaultOutputFolderName() + "\"}");
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
    public void getFolderContents(final String path, AsyncCallback<String> callback) {
        getFolderContents(path, true, callback);
    }

    @Override
    public void getFolderContents(final String path, boolean includeFiles, AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getDataMgmtBaseUrl() + "directory?includefiles=" //$NON-NLS-1$
                + (includeFiles ? "1" : "0"); //$NON-NLS-1$ //$NON-NLS-2$

        if (path != null && !path.isEmpty()) {
            fullAddress += "&path=" + URL.encodePathSegment(path); //$NON-NLS-1$
        }

        ServiceCallWrapper wrapper = new ServiceCallWrapper(fullAddress);
        callService(callback, wrapper);
    }

    @Override
    public void createFolder(Folder parentFolder, String newFolderName, AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getDataMgmtBaseUrl() + "directory/create"; //$NON-NLS-1$
        JSONObject obj = new JSONObject();
        obj.put("path", new JSONString(parentFolder.getId() + "/" + newFolderName)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                obj.toString());
        callService(callback, wrapper);
    }

    @Override
    public void diskResourcesExist(List<String> diskResourceIds, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getDataMgmtBaseUrl() + "exists"; //$NON-NLS-1$

        JSONObject json = new JSONObject();
        json.put("paths", JsonUtil.buildArrayFromStrings(diskResourceIds)); //$NON-NLS-1$
        String body = json.toString();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address, body);
        callService(callback, wrapper);
    }

    @Override
    public void previewFile(final String path, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getDataMgmtBaseUrl() + "file/preview"; //$NON-NLS-1$
        JSONObject body = new JSONObject();
        body.put("source", new JSONString(path)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        callService(callback, wrapper);
    }

    @Override
    public void moveDiskResources(
            final Set<org.iplantc.core.uidiskresource.client.models.DiskResource> diskResources,
            final Folder idDestFolder, AsyncCallback<String> callback) {

        String address = DEProperties.getInstance().getDataMgmtBaseUrl() + "move"; //$NON-NLS-1$

        List<String> idSrcFiles = DiskResourceUtil.asStringIdList(diskResources);
        JSONObject body = new JSONObject();
        body.put("dest", new JSONString(idDestFolder.getId())); //$NON-NLS-1$
        body.put("sources", JsonUtil.buildArrayFromStrings(idSrcFiles)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());

        callService(callback, wrapper);
    }

    @Override
    public void renameDiskResource(DiskResource src, String destName, AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getDataMgmtBaseUrl() + "rename"; //$NON-NLS-1$
        JSONObject body = new JSONObject();
        body.put("source", new JSONString(src.getId())); //$NON-NLS-1$
        body.put("dest", new JSONString(DiskResourceUtil.parseParent(src.getId()) + "/" + destName)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body.toString());
        callService(callback, wrapper);
    }

    /**
     * search data
     *
     * @param term search termt
     * @param callback
     */
    @Override
    public void search(String term, int size, String type, AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getMuleServiceBaseUrl() + "search?search-term="
                + URL.encodePathSegment(term) + "&size=" + size;

        if (type != null && !type.isEmpty()) {
            fullAddress = fullAddress + "&type=" + type;
        }

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, fullAddress);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
    public void importFromUrl(final String url, final HasId dest, AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getFileIoBaseUrl() + "urlupload"; //$NON-NLS-1$
        JSONObject body = new JSONObject();
        body.put("dest", new JSONString(dest.getId())); //$NON-NLS-1$
        body.put("address", new JSONString(url)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body.toString());
        callService(callback, wrapper);
    }

    @Override
    public void upload(AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getDataMgmtBaseUrl() + "upload"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        callService(callback, wrapper);
    }

    @Override
    public void download(HasPaths paths, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getDataMgmtBaseUrl() + "download"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(paths)).getPayload());
        callService(callback, wrapper);
    }

    private final DEClientConstants constants = GWT.create(DEClientConstants.class);

    @Override
    public void simpleDownload(String path) {
        // We must proxy the download requests through a servlet, since the actual download service may
        // be on a port behind a firewall that the servlet can access, but the client can not.
        String address = Format.substitute("{0}{1}?user={2}&path={3}", GWT.getModuleBaseURL(), //$NON-NLS-1$
                constants.fileDownloadServlet(), UserInfo.getInstance().getUsername(), path);

        WindowUtil.open(URL.encode(address), "width=100,height=100"); //$NON-NLS-1$
    }

    @Override
    public <T extends DiskResource> void deleteDiskResources(Set<T> diskResources,
            AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getDataMgmtBaseUrl() + "delete"; //$NON-NLS-1$
        List<String> drIds = DiskResourceUtil.asStringIdList(diskResources);
        String body = "{\"paths\": " + JsonUtil.buildJsonArrayString(drIds) + "}"; //$NON-NLS-1$ //$NON-NLS-2$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body);
        callService(callback, wrapper);

    }

    @Override
    public void getDiskResourceMetaData(DiskResource resource, AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getDataMgmtBaseUrl() + "metadata" + "?path=" //$NON-NLS-1$ //$NON-NLS-2$
                + URL.encodePathSegment(resource.getId());
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, fullAddress);
        callService(callback, wrapper);

    }

    @Override
    public void setDiskResourceMetaData(DiskResource resource, Set<DiskResourceMetadata> mdToUpdate,
            Set<DiskResourceMetadata> mdToDelete, AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getDataMgmtBaseUrl() + "metadata-batch" //$NON-NLS-1$
                + "?path=" + URL.encodePathSegment(resource.getId()); //$NON-NLS-1$

        // Create json body consisting of md to updata and md to delete.
        JSONObject obj = new JSONObject();
        obj.put("add", buildMetadataToAddJsonArray(mdToUpdate));
        obj.put("delete", buildMetadataToDeleteJsonArray(mdToDelete));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                obj.toString());
        callService(callback, wrapper);
    }

    private JSONValue buildMetadataToAddJsonArray(Set<DiskResourceMetadata> metadata) {
        JSONArray arr = new JSONArray();
        int i = 0;
        for (DiskResourceMetadata md : metadata) {
            AutoBean<DiskResourceMetadata> bean = AutoBeanUtils.getAutoBean(md);
            JSONValue jsonValue = JSONParser.parseStrict(AutoBeanCodex.encode(bean).getPayload());
            arr.set(i++, jsonValue);
        }
        return arr;
    }

    private JSONValue buildMetadataToDeleteJsonArray(Set<DiskResourceMetadata> metadataToDelete) {
        JSONArray arr = new JSONArray();
        int i = 0;
        for (DiskResourceMetadata md : metadataToDelete) {
            arr.set(i++, new JSONString(md.getAttribute()));
        }
        return arr;
    }

    @Override
    public void setFolderMetaData(String folderId, String body, AsyncCallback<String> callback) {
        // String fullAddress = serviceNamePrefix
        //                + ".folder-metadata-batch" + "?path=" + URL.encodePathSegment(folderId); //$NON-NLS-1$
        // ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
        // body);
        // callService(callback, wrapper);
    }

    @Override
    public void setFileMetaData(String fileId, String body, AsyncCallback<String> callback) {
        // String fullAddress = serviceNamePrefix
        //                + ".file-metadata-batch" + "?path=" + URL.encodePathSegment(fileId); //$NON-NLS-1$
        // ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
        // body);
        // callService(callback, wrapper);
    }

    @Override
    public void shareDiskResource(JSONObject body, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "share"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
    public void unshareDiskResource(JSONObject body, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "unshare"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
    public void getPermissions(JSONObject body, AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getDataMgmtBaseUrl() + "user-permissions"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body.toString());
        callService(callback, wrapper);
    }

    @Override
    public void getStat(String body, AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getDataMgmtBaseUrl() + "stat"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body.toString());
        callService(callback, wrapper);
    }

    /**
     * Performs the actual service call.
     *
     * @param callback executed when RPC call completes.
     * @param wrapper the wrapper used to get to the actual service via the service proxy.
     */
    private void callService(AsyncCallback<String> callback, ServiceCallWrapper wrapper) {
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
    public void getDataSearchHistory(AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "search-history";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
    public void saveDataSearchHistory(String body, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "search-history";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address, body);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restoreDiskResource(HasPaths request, AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getDataMgmtBaseUrl() + "restore"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(request)).getPayload());
        callService(callback, wrapper);
    }

    /**
     * empty user's trash
     *
     * @param user
     * @param callback
     */
    @Override
    public void emptyTrash(String user, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getDataMgmtBaseUrl() + "trash"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.DELETE, address);
        callService(callback, wrapper);
    }

    /**
     * get users trash path
     *
     * @param userName
     * @param callback
     */
    @Override
    public void getUserTrashPath(String userName, AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getDataMgmtBaseUrl() + "user-trash-dir" //$NON-NLS-1$
                + "?path=" + URL.encodePathSegment(userName); //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, fullAddress);
        callService(callback, wrapper);
    }

    /**
     * Creates a set of public data links for the given disk resources.
     *
     * @param ticketIdToResourceIdMap the id of the disk resource for which the ticket will be created.
     * @param isPublicTicket
     * @param callback
     */
    @Override
    public void createDataLinks(Map<String, String> ticketIdToResourceIdMap,
            AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getDataMgmtBaseUrl() + "tickets"; //$NON-NLS-1$
        String args = "public=1";

        JSONObject body = new JSONObject();
        JSONArray tickets = new JSONArray();
        int index = 0;
        for (String ticketId : ticketIdToResourceIdMap.keySet()) {
            String resourceId = ticketIdToResourceIdMap.get(ticketId);

            JSONObject subBody = new JSONObject();
            subBody.put("path", new JSONString(resourceId));
            subBody.put("ticket-id", new JSONString(ticketId));
            tickets.set(index, subBody);
            index++;
        }
        body.put("tickets", tickets);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body.toString());
        wrapper.setArguments(args);
        callService(callback, wrapper);

    }

    /**
     * Requests a listing of all the tickets for the given disk resources.
     *
     * @param diskResourceIds the disk resources whose tickets will be listed.
     * @param callback
     */
    @Override
    public void listDataLinks(List<String> diskResourceIds, AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getDataMgmtBaseUrl() + "list-tickets"; //$NON-NLS-1$

        JSONObject body = new JSONObject();
        body.put("paths", JsonUtil.buildArrayFromStrings(diskResourceIds));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body.toString());
        callService(callback, wrapper);

    }

    /**
     * Requests that the given Kif Share tickets will be deleted.
     *
     * @param dataLinkIds the tickets which will be deleted.
     * @param callback
     */
    @Override
    public void deleteDataLinks(List<String> dataLinkIds, AsyncCallback<String> callback) {
        String fullAddress = DEProperties.getInstance().getDataMgmtBaseUrl() + "delete-tickets"; //$NON-NLS-1$

        JSONObject body = new JSONObject();
        body.put("tickets", JsonUtil.buildArrayFromStrings(dataLinkIds));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body.toString());
        callService(callback, wrapper);
    }

	@Override
	public void getFileTypes(AsyncCallback<String> callback) {
		String address = DEProperties.getInstance().getMuleServiceBaseUrl()
				+ "filetypes/type-list";

		ServiceCallWrapper wrapper = new ServiceCallWrapper(
				ServiceCallWrapper.Type.GET, address);
		DEServiceFacade.getInstance().getServiceData(wrapper, callback);
	}

	@Override
	public void setFileType(String filePath, String type,
			AsyncCallback<String> callback) {
		JSONObject obj = new JSONObject();
		obj.put("path", new JSONString(filePath));

		String address = DEProperties.getInstance().getMuleServiceBaseUrl()
				+ "filetypes/type?type=" + type
				+ DEProperties.getInstance().getDefaultOutputFolderName();
		ServiceCallWrapper wrapper = new ServiceCallWrapper(
				ServiceCallWrapper.Type.POST, address, obj.toString());
		DEServiceFacade.getInstance().getServiceData(wrapper, callback);
	}

}
