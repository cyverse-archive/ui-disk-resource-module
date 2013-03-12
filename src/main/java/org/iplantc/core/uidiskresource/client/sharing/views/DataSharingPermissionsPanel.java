/**
 * 
 */
package org.iplantc.core.uidiskresource.client.sharing.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uicommons.client.models.collaborators.Collaborator;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.DiskResourceAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.models.Permissions;
import org.iplantc.core.uidiskresource.client.sharing.models.DataSharing;
import org.iplantc.core.uidiskresource.client.sharing.models.DataSharingKeyProvider;
import org.iplantc.core.uidiskresource.client.sharing.views.DataSharingView.Presenter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * @author sriram
 *
 */
public class DataSharingPermissionsPanel extends FramedPanel implements IsWidget {
    
    private Grid<DataSharing> grid;
 	private ToolBar toolbar;
    private FastMap<List<DataSharing>> originalList;
    private final FastMap<DiskResource> resources;
    private final Presenter presenter;
 
    private static final String ID_PERM_GROUP = "idPermGroup";
    private SimpleComboBox<String> permCombo;
 	private FastMap<List<DataSharing>> sharingMap;

    public DataSharingPermissionsPanel(Presenter dataSharingPresenter, FastMap<DiskResource> resources) {
        this.presenter = dataSharingPresenter;
        this.resources = resources;
        init();
    }

    private void init() {
        VerticalLayoutContainer container = new VerticalLayoutContainer();
        setHeight("250px");
        initToolbar();
        container.add(toolbar);
        ColumnModel<DataSharing> cm = buildColumnModel();
        initGrid(cm);
        container.add(grid);
        setWidget(container);
    }


	private void initGrid(ColumnModel<DataSharing> cm) {
        grid = new Grid<DataSharing>(new ListStore<DataSharing>(new DataSharingKeyProvider()), cm);
    }

    private void initToolbar() {
        toolbar = new ToolBar();
        toolbar.setHeight(30);
        SimpleComboBox<String> permissionsCombo = buildPermissionsCombo();
    }

    private SimpleComboBox<String> buildPermissionsCombo() {
        permCombo = new SimpleComboBox<String>(
                new StringLabelProvider<String>());
        permCombo.setId(ID_PERM_GROUP);
        permCombo.setForceSelection(true);
        permCombo.add(DataSharing.READ);
        permCombo.add(DataSharing.WRITE);
        permCombo.add(DataSharing.OWN);
        permCombo.setEditable(false);

        permCombo.setTriggerAction(TriggerAction.ALL);
        permCombo.addSelectionHandler(new SelectionHandler<String>() {
            
            @Override
            public void onSelection(SelectionEvent<String> event) {
                List<DataSharing> items = grid.getSelectionModel().getSelectedItems();
            }
        });
        return permCombo;
    }


    private void removeModels(DataSharing model) {
    	ListStore<DataSharing> store = grid.getStore();

        DataSharing sharing = store.findModel(model);
        if (sharing != null) {
            // Remove the shares from the sharingMap as well as the grid.
            sharingMap.put(sharing.getUserName(), null);
            store.remove(sharing);
        }
    }




    
    
    public void loadSharingData(FastMap<List<DataSharing>> sharingMap) {
    	this.sharingMap = sharingMap;
        originalList = new FastMap<List<DataSharing>>();

        ListStore<DataSharing> store = grid.getStore();
        store.clear();
    //    explainPanel.hide();

        for (String userName : sharingMap.keySet()) {
            List<DataSharing> dataShares = sharingMap.get(userName);

            if (dataShares != null && !dataShares.isEmpty()) {
                List<DataSharing> newList = new ArrayList<DataSharing>();
                for (DataSharing share : dataShares) {
                    DataSharing copyShare = share.copy();
                    newList.add(copyShare);
                }
                originalList.put(userName, newList);

                // Add a dummy display share to the grid.
                DataSharing displayShare = dataShares.get(0).copy();
//                if (hasVaryingPermissions(dataShares)) {
//                    // Set the display permission to "varies" if this user's share list has varying
//                    // permissions.
//                    displayShare.setDisplayPermission(I18N.DISPLAY.varies());
//                    explainPanel.show();
//                }

                store.add(displayShare);
            }
        }
    }


    private ColumnModel<DataSharing> buildColumnModel() {
        List<ColumnConfig<DataSharing, ?>> configs = new ArrayList<ColumnConfig<DataSharing, ?>>();
        ColumnConfig<DataSharing, String> name = new ColumnConfig<DataSharing, String>(
                new ValueProvider<DataSharing, String>() {

                    @Override
                    public String getValue(DataSharing object) {
                        return object.getName();
                    }

                    @Override
                    public void setValue(DataSharing object, String value) {
                        // do nothing intentionally

                    }

                    @Override
                    public String getPath() {
                        return "name";
                    }
                });

        name.setHeader("Name");
        name.setWidth(220);
        ColumnConfig<DataSharing, String> permission = new ColumnConfig<DataSharing, String>(
                new ValueProvider<DataSharing, String>() {

                    @Override
                    public String getValue(DataSharing object) {
                            return ((DataSharing)(object)).getDisplayPermission();
                    }

                    @Override
                    public void setValue(DataSharing object, String value) {
                        object.setDisplayPermission(value);
                    }

                    @Override
                    public String getPath() {
                        return "displayPermission";
                    }
                });

        permission.setHeader("Permissions");
        permission.setWidth(80);
        configs.add(name);
        configs.add(permission);
        return new ColumnModel<DataSharing>(configs);
    }

    /**
    *
    *
    * @return the sharing list
    */
        public FastMap<List<DataSharing>> getSharingMap() {
            FastMap<List<DataSharing>> sharingList = new FastMap<List<DataSharing>>();
            for (DataSharing share : grid.getStore().getAll()) {
                String userName = share.getUserName();
                List<DataSharing> dataShares = sharingMap.get(userName);
                List<DataSharing> updatedSharingList = getUpdatedSharingList(userName, dataShares);
                if (updatedSharingList != null && updatedSharingList.size() > 0) {
                    sharingList.put(userName, updatedSharingList);
                }
            }

            return sharingList;
        }

        /**
    * check the list with original to see if things have changed. ignore unchanged records
    *
    * @param userName
    * @param list
    * @return
    */
        private List<DataSharing> getUpdatedSharingList(String userName, List<DataSharing> list) {
            List<DataSharing> updateList = new ArrayList<DataSharing>();
            if (list != null && userName != null) {
                List<DataSharing> fromOriginal = originalList.get(userName);

                if (fromOriginal == null || fromOriginal.isEmpty()) {
                    updateList = list;
                } else {
                    for (DataSharing s : list) {
                        if (!fromOriginal.contains(s)) {
                            updateList.add(s);
                        }
                    }
                }
            }

            return updateList;
        }
    /**
     * check if a sharing recored originally existed. Needed to remove false submission of unshare list
     * 
     * @return
     */
//    private boolean isExistedOriginally(Sharing s) {
//        String userName = s.getUserName();
//        List<Sharing> fromOriginal = originalList.get(userName);
//        if (fromOriginal != null && fromOriginal.contains(s)) {
//            return true;
//        } else {
//            return false;
//        }
//
//    }
//
//    private void addSharing(Sharing obj) {
//        TreeStore<Sharing> treeStore = grid.getTreeStore();
//        if (treeStore.findModel(obj) == null) {
//            treeStore.add(obj);
//            grid.setLeaf(obj, false);
//        }
//        grid.getSelectionModel().select(false, obj);
//    }

//    public void addDataSharing(FastMap<DataSharing> sharingMap) {
//        ListStore<Sharing> treeStore = grid.getTreeStore();
//        if (sharingMap != null) {
//            for (DataSharing s : sharingMap.values()) {
//                Sharing find = new Sharing(s.getCollaborator());
//                Sharing exists = treeStore.findModel(find);
//                if (exists == null) {
//                    treeStore.add(find);
//                    exists = find;
//                }
//                List<Sharing> childerens = treeStore.getChildren(exists);
//                if (childerens != null) {
//                    for (Sharing temp : childerens) {
//                        DataSharing tempDs = (DataSharing)temp;
//                        if (tempDs.equals(s)) {
//                            return;
//                        }
//                    }
//                }
//                treeStore.add(exists, s);
//            }
//            grid.expandAll();
//        }
//
//    }

   
//    private void updatePermissions(String perm, DataSharing model) {
//        if (perm.equals(DataSharing.READ)) {
//            model.setReadable(true);
//            model.setWritable(false);
//            model.setOwner(false);
//        } else if (perm.equals(DataSharing.WRITE)) {
//            model.setWritable(true);
//            model.setReadable(true);
//            model.setOwner(false);
//        } else {
//            model.setOwner(true);
//            model.setReadable(true);
//            model.setWritable(true);
//        }
//        model.setDisplayPermission(perm);
//        grid.getStore().update(model);
//    }
        
        private void updatePermissions(String perm, String username) {
            List<DataSharing> models = sharingMap.get(username);
            if (models != null) {
                boolean own = perm.equals("own");
                boolean write = own || perm.equals("write");
                boolean read = true;

                for (DataSharing share : models) {
                    if (own) {
                        share.setOwner(true);
                    } else if (write) {
                        share.setWritable(true);
                    } else {
                        share.setReadable(true);
                    }
                }

                if (resources.size() != models.size()) {
                    Collaborator user = models.get(0).getCollaborator();
                    DiskResourceAutoBeanFactory factory = GWT.create(DiskResourceAutoBeanFactory.class);
                    AutoBean<Permissions> autoBean = AutoBeanCodex.decode(factory, Permissions.class,buildSharingPermissions(read, write, own));
                    Permissions perms = autoBean.as();
                    for (String path : resources.keySet()) {
                        boolean shared = false;
                        for (DataSharing existingShare : models) {
                            if (path.equals(existingShare.getPath())) {
                                shared = true;
                                break;
                            }
                        }

                        if (!shared) {
                            models.add(new DataSharing(user, perms, path));
                        }
                    }
                }

               // checkExplainPanelVisibility();
            }
        }

        private String buildSharingPermissions(boolean read, boolean write, boolean own) {
            JSONObject permission = new JSONObject();
            permission.put("read", JSONBoolean.getInstance(read));
            permission.put("write", JSONBoolean.getInstance(write));
            permission.put("own", JSONBoolean.getInstance(own));
            return permission.toString();
         }
        
    /**
     * @return the unshareList
     */
    public FastMap<List<DataSharing>> getUnshareList() {
    	// Prepare unshared list here
        FastMap<List<DataSharing>> unshareList = new FastMap<List<DataSharing>>();

        for (String userName : originalList.keySet()) {
            if (sharingMap.get(userName) == null) {
                // The username entry from the original list was removed from the sharingMap, which means
                // it was unshared.
                List<DataSharing> removeList = originalList.get(userName);

                if (removeList != null && !removeList.isEmpty()) {
                    unshareList.put(userName, removeList);
                }
            }
        }

        return unshareList;
    }

}
