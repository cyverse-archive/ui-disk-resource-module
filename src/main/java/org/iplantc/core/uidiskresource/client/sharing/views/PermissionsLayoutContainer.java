/**
 * 
 */
package org.iplantc.core.uidiskresource.client.sharing.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uicommons.client.models.collaborators.Collaborator;
import org.iplantc.core.uicommons.client.models.sharing.Sharing;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.sharing.models.DataSharing;
import org.iplantc.core.uidiskresource.client.sharing.models.DataSharing.TYPE;
import org.iplantc.core.uidiskresource.client.sharing.views.DataSharingView.Presenter;
import org.iplantc.core.uidiskresource.client.views.cells.Resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.dnd.core.client.DND.Operation;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.TreeGridDropTarget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;
import com.sencha.gxt.widget.core.client.treegrid.TreeGridSelectionModel;

/**
 * @author sriram
 *
 */
public class PermissionsLayoutContainer extends VerticalLayoutContainer implements IsWidget {
    
    private TreeGrid<Sharing> grid;
    private TreeStore<Sharing> treeStore;
    private ToolBar toolbar;
    private FastMap<List<Sharing>> originalList;
    private final Presenter presenter;
    private final FastMap<List<Sharing>> unshareList;

    private static final String ID_PERM_GROUP = "idPermGroup";
    private static final String ID_BTN_REMOVE = "idBtnRemove";
    private SimpleComboBox<String> permCombo;
    private TextButton removeBtn;

    public PermissionsLayoutContainer(Presenter dataSharingPresenter) {
        this.presenter = dataSharingPresenter;
        unshareList = new FastMap<List<Sharing>>();
        init();
    }

    private void init() {
        initToolbar();
        add(toolbar);
        ColumnModel<Sharing> cm = buildColumnModel();
        treeStore = new TreeStore<Sharing>(new KeyProvider());
        initGrid(cm);
        add(grid);
    }

    private void initGrid(ColumnModel<Sharing> cm) {
        grid = new TreeGrid<Sharing>(treeStore, cm, cm.getColumn(0));
        grid.setIconProvider(new ShareIconProvider());
        grid.setHeight(350);
        TreeGridSelectionModel<Sharing> sm = (TreeGridSelectionModel<Sharing>)grid.getSelectionModel();
        sm.addSelectionChangedHandler(new SelectionChangedHandler<Sharing>() {

            @Override
            public void onSelectionChanged(SelectionChangedEvent<Sharing> event) {
                permCombo.clear();
                if (grid.getSelectionModel().getSelectedItems().size() > 0) {
                    permCombo.enable();
                    removeBtn.enable();
                } else {
                    permCombo.disable();
                    permCombo.disable();
                }
            }
        });
        
        TreeGridDropTarget<Sharing> sgdt = new ShareTreeGridDropTargetImpl(grid);

        sgdt.setOperation(Operation.COPY);
        sgdt.setAllowDropOnLeaf(false);
        sgdt.setAutoExpand(true);
    }

    private void initToolbar() {
        toolbar = new ToolBar();
        toolbar.setHeight(30);
        TextButton removeBtn = buildUnshareButton();
        toolbar.add(removeBtn);
        toolbar.add(new FillToolItem());
        SimpleComboBox<String> permissionsCombo = buildPermissionsCombo();
        permissionsCombo.setEmptyText("Permissions");
        permissionsCombo.disable();
        toolbar.add(permissionsCombo);
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
                List<Sharing> items = grid.getSelectionModel().getSelectedItems();
                if (items != null) {
                    for (Sharing s : items) {
                        String value = permCombo.getCurrentValue();
                        if (value != null) {
                            if (s instanceof DataSharing) {
                                updatePermissions(value, (DataSharing)s);
                            } else {
                                TreeStore<Sharing> treeStore = grid.getTreeStore();
                                Sharing sharing = treeStore.findModel(s);
                                if (sharing != null) {
                                    List<Sharing> models = treeStore.getChildren(sharing);
                                    if (models != null) {
                                        for (Sharing md : models) {
                                            updatePermissions(value, (DataSharing)md);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
            }
                
        });
        return permCombo;
    }

    private TextButton buildUnshareButton() {
        removeBtn = new TextButton(I18N.DISPLAY.unshare());
        removeBtn.setId(ID_BTN_REMOVE);
        removeBtn.setIcon(org.iplantc.core.uicommons.client.images.Resources.ICONS.delete());
        removeBtn.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                List<Sharing> models = grid.getSelectionModel().getSelectedItems();
                removeModels(models);
            }
        });
        removeBtn.disable();
        return removeBtn;
    }

    private void removeModels(List<Sharing> models) {
        // prepared unshared list here
        TreeStore<Sharing> store = grid.getTreeStore();
        for (Sharing model : models) {
            String userName = model.getUserName();
            List<Sharing> list = unshareList.get(userName);
            if (list == null) {
                list = new ArrayList<Sharing>();
            }
            if (model instanceof DataSharing) {
                if (isExistedOriginally(model)) {
                    list.add(model);
                }
                Sharing parent = store.getParent(model);
                store.remove(model);
                // prevent parent turning into a leaf
                if (parent != null) {
                    grid.setLeaf(parent, false);
                }
            } else {
                Sharing sharing = store.findModel(model);
                if (sharing != null) {
                    List<Sharing> removeList = store.getChildren(sharing);
                    for (Sharing remItem : removeList) {
                        if (isExistedOriginally(remItem)) {
                            list.add(remItem);
                        }
                    }
                    store.removeChildren(sharing);
                    store.remove(sharing);
                }
            }
            if (!list.isEmpty()) {
                unshareList.put(userName, list);
            }
        }
    }

    private final class ShareIconProvider implements IconProvider<Sharing> {
        @Override
        public ImageResource getIcon(Sharing model) {
            Resources res = GWT.create(Resources.class);
            if (model instanceof DataSharing) {
                DataSharing ds = (DataSharing)model;
                TYPE type = presenter.getSharingResourceType(ds.getPath());
                if (type == null) {
                    return org.iplantc.core.uicommons.client.images.Resources.ICONS.share();
                }
                if (type.equals(TYPE.FOLDER)) {
                    return res.folder();
                } else {
                    return res.file();
                }

            } else {
                return org.iplantc.core.uicommons.client.images.Resources.ICONS.share();
            }
        }
    }


    class KeyProvider implements ModelKeyProvider<Sharing> {
        @Override
        public String getKey(Sharing item) {
            return item.getKey();
        }
    }
    
    
    public void loadSharingData(List<Sharing> roots, FastMap<List<Sharing>> sharingMap) {
        originalList = new FastMap<List<Sharing>>();
        TreeStore<Sharing> treeStore = grid.getTreeStore();
        treeStore.clear();
        treeStore.add(roots);
        for (Sharing s : roots) {
            String userName = s.getUserName();
            List<Sharing> list = sharingMap.get(userName);
            List<Sharing> newList = new ArrayList<Sharing>();
            if (list != null) {
                for (Sharing item : list) {
                    treeStore.add(s, item);
                    newList.add(item.copy());
                }
                originalList.put(userName, newList);
            }
        }

        grid.expandAll();
    }


    private ColumnModel<Sharing> buildColumnModel() {
        List<ColumnConfig<Sharing, ?>> configs = new ArrayList<ColumnConfig<Sharing, ?>>();
        ColumnConfig<Sharing, String> name = new ColumnConfig<Sharing, String>(
                new ValueProvider<Sharing, String>() {

                    @Override
                    public String getValue(Sharing object) {
                        if (object instanceof DataSharing) {
                            return ((DataSharing)(object)).getName();
                        } else {
                            return object.getName();
                        }
                    }

                    @Override
                    public void setValue(Sharing object, String value) {
                        // do nothing intentionally

                    }

                    @Override
                    public String getPath() {
                        return "name";
                    }
                });

        name.setHeader("Name");
        name.setWidth(220);
        ColumnConfig<Sharing, String> permission = new ColumnConfig<Sharing, String>(
                new ValueProvider<Sharing, String>() {

                    @Override
                    public String getValue(Sharing object) {
                        if (object instanceof DataSharing) {
                            return ((DataSharing)(object)).getDisplayPermission();
                        } else {
                            return null;
                        }
                    }

                    @Override
                    public void setValue(Sharing object, String value) {
                        DataSharing ds = (DataSharing)object;
                        ds.setDisplayPermission(value);
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
        return new ColumnModel<Sharing>(configs);
    }

    /**
     * 
     * 
     * @return the sharing list
     */
    public FastMap<List<Sharing>> getSharingMap() {
        FastMap<List<Sharing>> sharingList = new FastMap<List<Sharing>>();
        for (Sharing s : grid.getTreeStore().getAll()) {
            if (!(s instanceof DataSharing)) {
                List<Sharing> childrens = grid.getTreeStore().getChildren(s);
                List<Sharing> updatedSharingList = getUpdatedSharingList(s.getUserName(), childrens);
                if (updatedSharingList != null && updatedSharingList.size() > 0) {
                    sharingList.put(s.getUserName(), updatedSharingList);
                }
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
    private List<Sharing> getUpdatedSharingList(String userName, List<Sharing> list) {
        List<Sharing> updateList = new ArrayList<Sharing>();
        if (list != null && userName != null) {
            List<Sharing> fromOriginal = originalList.get(userName);
            if (fromOriginal == null || fromOriginal.isEmpty()) {
                updateList = list;
            } else {
                for (Sharing s : list) {
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
    private boolean isExistedOriginally(Sharing s) {
        String userName = s.getUserName();
        List<Sharing> fromOriginal = originalList.get(userName);
        if (fromOriginal != null && fromOriginal.contains(s)) {
            return true;
        } else {
            return false;
        }

    }

    private void addSharing(Sharing obj) {
        TreeStore<Sharing> treeStore = grid.getTreeStore();
        if (treeStore.findModel(obj) == null) {
            treeStore.add(obj);
            grid.setLeaf(obj, false);
        }
        grid.getSelectionModel().select(false, obj);
    }

    public void addDataSharing(FastMap<DataSharing> sharingMap) {
        TreeStore<Sharing> treeStore = grid.getTreeStore();
        if (sharingMap != null) {
            for (DataSharing s : sharingMap.values()) {
                Sharing find = new Sharing(s.getCollaborator());
                Sharing exists = treeStore.findModel(find);
                if (exists == null) {
                    treeStore.add(find);
                    exists = find;
                }
                List<Sharing> childerens = treeStore.getChildren(exists);
                if (childerens != null) {
                    for (Sharing temp : childerens) {
                        DataSharing tempDs = (DataSharing)temp;
                        if (tempDs.equals(s)) {
                            return;
                        }
                    }
                }
                treeStore.add(exists, s);
            }
            grid.expandAll();
        }

    }

    private final class ShareTreeGridDropTargetImpl extends TreeGridDropTarget<Sharing> {
        private ShareTreeGridDropTargetImpl(TreeGrid<Sharing> tree) {
            super(tree);
        }

        @Override
        public void onDragMove(DndDragMoveEvent e) {
            super.onDragMove(e);
            Element tn = e.getDragMoveEvent().getNativeEvent().getEventTarget().cast();
            if (tn == null) {
                e.getStatusProxy().setStatus(false);
                e.setCancelled(true);
                return;
            }
        }

        @Override
        public void onDragEnter(DndDragEnterEvent e) {
            super.onDragEnter(e);
            Element tn = e.getDragEnterEvent().getNativeEvent().getEventTarget().cast();
            if (tn == null) {
                e.getStatusProxy().setStatus(false);
                e.setCancelled(true);
                return;
            }

        }

        @Override
        public void onDragDrop(DndDropEvent e) {
            Element tn = e.getDragEndEvent().getNativeEvent().getEventTarget().cast();
            if (tn == null) {
                e.getStatusProxy().setStatus(false);
                return;
            }
            List<?> items = (List<?>)e.getData();
            if (items != null) {
                if (items.get(0) instanceof Collaborator) {
                    for (Object coll : items) {
                        addSharing(new Sharing((Collaborator)coll));
                    }
                    return;
                }
            }

            TreeNode<Sharing> node = grid.findNode(tn);
            if(node != null) {
                Sharing s = node.getModel();
                if (s != null) {
                    for (Object dr : items) {
                        DataSharing ds = new DataSharing(s.getCollaborator(),
                                presenter.getDefaultPermissions(), ((DiskResource)dr).getId());
                        grid.getTreeStore().add(s, ds);
                    }
                }
            }
        }
    }

    private void updatePermissions(String perm, DataSharing model) {
        if (perm.equals(DataSharing.READ)) {
            model.setReadable(true);
            model.setWritable(false);
            model.setOwner(false);
        } else if (perm.equals(DataSharing.WRITE)) {
            model.setWritable(true);
            model.setReadable(true);
            model.setOwner(false);
        } else {
            model.setOwner(true);
            model.setReadable(true);
            model.setWritable(true);
        }
        model.setDisplayPermission(perm);
        grid.getTreeStore().update(model);
    }

    /**
     * @return the unshareList
     */
    public FastMap<List<Sharing>> getUnshareList() {
        return unshareList;
    }

}
