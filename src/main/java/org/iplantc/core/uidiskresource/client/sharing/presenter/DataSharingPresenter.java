/**
 * 
 */
package org.iplantc.core.uidiskresource.client.sharing.presenter;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.I18N;
import org.iplantc.core.uicommons.client.models.collaborators.Collaborator;
import org.iplantc.core.uicommons.client.models.sharing.Sharing;
import org.iplantc.core.uicommons.client.util.CollaboratorsUtil;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.models.autobeans.Permissions;
import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uidiskresource.client.sharing.models.DataSharing;
import org.iplantc.core.uidiskresource.client.sharing.models.DataSharing.TYPE;
import org.iplantc.core.uidiskresource.client.sharing.views.DataSharingView;
import org.iplantc.core.uidiskresource.client.sharing.views.DataSharingView.Presenter;
import org.iplantc.core.uidiskresource.client.sharing.views.PermissionsLayoutContainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

/**
 * @author sriram
 *
 */
public class DataSharingPresenter implements Presenter {

    DataSharingView view;
    List<DiskResource> selectedResources;
    private FastMap<Sharing> sharingList;
    private FastMap<List<Sharing>> dataSharingMap;
    private final DiskResourceServiceFacade facade;
    private final PermissionsLayoutContainer permissionsPanel;

    public DataSharingPresenter(List<DiskResource> selectedResources, DataSharingView view) {
        facade = GWT.create(DiskResourceServiceFacade.class);
        this.view = view;
        this.selectedResources = selectedResources;
        view.setPresenter(this);
        permissionsPanel = new PermissionsLayoutContainer(this);
        view.addShareWidget(permissionsPanel.asWidget());
        loadCollaborators();
        loadDiskResources();
    }
    
    
    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
    }

    @Override
    public void loadCollaborators() {
       CollaboratorsUtil.getCollaborators(new AsyncCallback<Void>() {
        
        @Override
        public void onSuccess(Void result) {
                view.setCollaborators(CollaboratorsUtil.getCurrentCollaborators());
                loadPermissions();
        }
        
        @Override
        public void onFailure(Throwable caught) {
            // TODO Auto-generated method stub
            
        }
        });

    }

    @Override
    public void loadDiskResources() {
        view.setSelectedDiskResources(selectedResources);
    }

    @Override
    public void loadPermissions() {
        permissionsPanel.mask(I18N.DISPLAY.loadingMask());
        facade.getPermissions(buildPermissionsRequestBody(), new LoadPermissionsCallback());
    }

    private void parsePermissions(String path, JSONArray user_arr) {
        DiskResourceAutoBeanFactory factory = GWT.create(DiskResourceAutoBeanFactory.class);
        for (int i = 0; i < user_arr.size(); i++) {
            JSONObject obj = user_arr.get(i).isObject();
            JSONObject perm = JsonUtil.getObject(obj, "permissions");
            Collaborator collaborator = CollaboratorsUtil.findCollaboratorByUserName(JsonUtil.getString(
                    obj, "user"));

            String userName = collaborator.getUserName();
            Sharing s = sharingList.get(userName);
            AutoBean<Permissions> autoBean = AutoBeanCodex.decode(factory, Permissions.class,
                    perm.toString());
            Sharing dataSharing = new DataSharing(collaborator, autoBean.as(), path);
            if (s == null) {
                s = new Sharing(collaborator);
                sharingList.put(userName, s);
            }
            List<Sharing> list = dataSharingMap.get(userName);
            if (list == null) {
                list = new ArrayList<Sharing>();
                dataSharingMap.put(userName, list);
            }
            list.add(dataSharing);

        }

    }

    private JSONObject buildPermissionsRequestBody() {
        JSONObject obj = new JSONObject();
        JSONArray ids = new JSONArray();
        for (int i = 0; i < selectedResources.size(); i++) {
            ids.set(i, new JSONString(selectedResources.get(i).getId()));
        }
        obj.put("paths", ids);
        return obj;
    }

    private final class LoadPermissionsCallback implements AsyncCallback<String> {
        @Override
        public void onFailure(Throwable caught) {
            permissionsPanel.unmask();
            ErrorHandler.post(caught);
        }

        @Override
        public void onSuccess(String result) {
            JSONObject obj = JsonUtil.getObject(result);
            JSONArray permissionsArray = JsonUtil.getArray(obj, "paths");
            sharingList = new FastMap<Sharing>();
            dataSharingMap = new FastMap<List<Sharing>>();
            if (permissionsArray != null) {
                for (int i = 0; i < permissionsArray.size(); i++) {
                    JSONObject user_perm_obj = permissionsArray.get(i).isObject();
                    String path = JsonUtil.getString(user_perm_obj, "path");
                    JSONArray user_arr = JsonUtil.getArray(user_perm_obj, "user-permissions");
                    parsePermissions(path, user_arr);
                }
            }
            ArrayList<Sharing> list = new ArrayList<Sharing>(sharingList.values());
            permissionsPanel.loadSharingData(list, dataSharingMap);
            permissionsPanel.unmask();

        }
    }


    @Override
    public void processRequest() {
        JSONObject requestBody = buildSharingJson();
        JSONObject unshareRequestBody = buildUnSharingJson();
        if (requestBody != null) {
            share(requestBody);
        }

        if (unshareRequestBody != null) {
            unshare(unshareRequestBody);
        }

        if (requestBody != null || unshareRequestBody != null) {
            AlertMessageBox amb = new AlertMessageBox(
                    org.iplantc.core.uidiskresource.client.I18N.DISPLAY.share() + "/ "
                            + org.iplantc.core.uidiskresource.client.I18N.DISPLAY.unshare(),
                    org.iplantc.core.uidiskresource.client.I18N.DISPLAY.sharingCompleteMsg());
            amb.show();
        }

    }

    private void share(JSONObject requestBody) {

        if (requestBody != null) {
            callSharingService(requestBody);
        }

    }

    private void unshare(JSONObject unshareRequestBody) {

        if (unshareRequestBody != null) {
            callUnshareService(unshareRequestBody);
        }

    }

    private void callSharingService(JSONObject obj) {
        facade.shareDiskResource(obj, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                // do nothing intentionally
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);

            }
        });
    }

    private void callUnshareService(JSONObject obj) {
        facade.unshareDiskResource(obj, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                // do nothing
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);

            }
        });
    }

    private JSONObject buildSharingJson() {
        JSONObject sharingObj = new JSONObject();
        FastMap<List<Sharing>> sharingMap = permissionsPanel.getSharingMap();

        if (sharingMap != null && sharingMap.size() > 0) {
            JSONArray sharingArr = new JSONArray();
            int index = 0;
            for (String userName : sharingMap.keySet()) {
                List<Sharing> shareList = sharingMap.get(userName);
                JSONObject userObj = new JSONObject();
                userObj.put("user", new JSONString(userName));
                userObj.put("paths", buildPathArrWithPermissions(shareList));
                sharingArr.set(index++, userObj);
            }

            sharingObj.put("sharing", sharingArr);
            return sharingObj;
        } else {
            return null;
        }
    }

    private JSONArray buildPathArrWithPermissions(List<Sharing> list) {
        JSONArray pathArr = new JSONArray();
        int index = 0;
        JSONObject obj;
        for (Sharing s : list) {
            DataSharing ds = (DataSharing)s;
            obj = new JSONObject();
            obj.put("path", new JSONString(ds.getPath()));
            obj.put("permissions", buildSharingPermissions(ds));
            pathArr.set(index++, obj);
        }

        return pathArr;
    }

    private JSONArray buildPathArr(List<Sharing> list) {
        JSONArray pathArr = new JSONArray();
        int index = 0;
        for (Sharing s : list) {
            DataSharing ds = (DataSharing)s;
            pathArr.set(index++, new JSONString(ds.getPath()));
        }
        return pathArr;
    }

    private JSONObject buildSharingPermissions(DataSharing sh) {
        JSONObject permission = new JSONObject();
        permission.put("read", JSONBoolean.getInstance(sh.isReadable()));
        permission.put("write", JSONBoolean.getInstance(sh.isWritable()));
        permission.put("own", JSONBoolean.getInstance(sh.isOwner()));
        return permission;
    }

    private JSONObject buildUnSharingJson() {
        JSONObject unsharingObj = new JSONObject();
        FastMap<List<Sharing>> unSharingMap = permissionsPanel.getUnshareList();

        if (unSharingMap != null && unSharingMap.size() > 0) {
            JSONArray unsharingArr = new JSONArray();
            int index = 0;
            for (String userName : unSharingMap.keySet()) {
                List<Sharing> shareList = unSharingMap.get(userName);
                JSONObject userObj = new JSONObject();
                userObj.put("user", new JSONString(userName));
                userObj.put("paths", buildPathArr(shareList));
                unsharingArr.set(index++, userObj);
            }
            unsharingObj.put("unshare", unsharingArr);
            return unsharingObj;
        } else {
            return null;
        }

    }

    @Override
    public TYPE getSharingResourceType(String path) {
        for (DiskResource dr : selectedResources) {
            if (dr.getId().equalsIgnoreCase(path)) {
                if (dr instanceof Folder) {
                    return TYPE.FOLDER;
                } else {
                    return TYPE.FILE;
                }
            }
        }

        return null;
    }

    @Override
    public void addDataSharing(FastMap<DataSharing> smap) {
        permissionsPanel.addDataSharing(smap);
    }

    @Override
    public Permissions getDefaultPermissions() {
        JSONObject obj = new JSONObject();
        obj.put(DataSharing.READ, JSONBoolean.getInstance(true));
        obj.put(DataSharing.WRITE, JSONBoolean.getInstance(false));
        obj.put(DataSharing.OWN, JSONBoolean.getInstance(false));
        DiskResourceAutoBeanFactory factory = GWT.create(DiskResourceAutoBeanFactory.class);
        AutoBean<Permissions> bean = AutoBeanCodex.decode(factory, Permissions.class, obj.toString());
        return bean.as();
    }


}
