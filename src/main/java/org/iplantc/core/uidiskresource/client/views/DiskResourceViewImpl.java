package org.iplantc.core.uidiskresource.client.views;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceInfo;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceModelKeyProvider;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.models.autobeans.Permissions;
import org.iplantc.core.uidiskresource.client.sharing.views.DataSharingDialog;
import org.iplantc.core.uidiskresource.client.views.cells.DiskResourceNameCell;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.dnd.core.client.DND.Operation;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeAppearance;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;

public class DiskResourceViewImpl implements DiskResourceView {
 
    @UiTemplate("DiskResourceView.ui.xml")
    interface DiskResourceViewUiBinder extends UiBinder<Widget, DiskResourceViewImpl> {
    }

    private static DiskResourceViewUiBinder BINDER = GWT.create(DiskResourceViewUiBinder.class);

    private Presenter presenter;

    @UiField
    DiskResourceViewToolbar toolbar;

    @UiField
    BorderLayoutContainer con;

    @UiField
    ContentPanel westPanel;

    @UiField(provided = true)
    Tree<Folder, String> tree;

    @UiField(provided = true)
    final TreeStore<Folder> treeStore;

    @UiField
    ContentPanel centerPanel;

    @UiField
    Grid<DiskResource> grid;

    @UiField
    ColumnModel<DiskResource> cm;

    @UiField
    ListStore<DiskResource> listStore;

    @UiField
    GridView<DiskResource> gridView;

    @UiField
    ContentPanel detailsPanel;

    @UiField
    ContentPanel historyPanel;

    @UiField
    BorderLayoutData westData;
    @UiField
    BorderLayoutData centerData;
    @UiField
    BorderLayoutData eastData;
    @UiField
    BorderLayoutData northData;
    @UiField
    BorderLayoutData southData;

    private final Widget widget;

    private TreeLoader<Folder> treeLoader;

    @Inject
    public DiskResourceViewImpl(final Tree<Folder, String> tree, final TreeStore<Folder> treeStore) {
        this.tree = tree;
        this.treeStore = treeStore;
        widget = BINDER.createAndBindUi(this);

        grid.setSelectionModel(((DiskResourceColumnModel)cm).getSelectionModel());
        // Set Leaf icon to a folder
        TreeStyle treeStyle = tree.getStyle();
        TreeAppearance appearance = tree.getAppearance();
        treeStyle.setLeafIcon(appearance.closeNodeIcon());
        tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tree.getSelectionModel().addSelectionHandler(new SelectionHandler<Folder>() {

            @Override
            public void onSelection(SelectionEvent<Folder> event) {
                if (event.getSelectedItem() != null) {
                    onFolderSelected(event.getSelectedItem());
                }
            }

        });

        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<DiskResource>() {

            @Override
            public void onSelectionChanged(SelectionChangedEvent<DiskResource> event) {
                if ((event.getSelection() != null) && !event.getSelection().isEmpty()) {
                    presenter.onDiskResourceSelected(Sets.newHashSet(event.getSelection()));
                } else {
                    resetDetailsPanel();
                }
            }

        });
    }

    @Override
    public void onDiskResourceSelected(Set<DiskResource> selection) {
        onDiskResourceSelected(selection);
    }

    @Override
    public void onFolderSelected(Folder folder) {
        presenter.onFolderSelected(folder);
    }

    @UiFactory
    ListStore<DiskResource> createListStore() {
        return new ListStore<DiskResource>(new DiskResourceModelKeyProvider());
    }

    @UiFactory
    public ValueProvider<Folder, String> createValueProvider() {
        return new ValueProvider<Folder, String>() {

            @Override
            public String getValue(Folder object) {
                return object.getName();
            }

            @Override
            public void setValue(Folder object, String value) {}

            @Override
            public String getPath() {
                return "name";
            }
        };
    }

    @UiFactory
    ColumnModel<DiskResource> createColumnModel() {
        return new DiskResourceColumnModel();
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
        toolbar.setPresenter(presenter);
        initDragAndDrop();
    }

    private void initDragAndDrop() {
        DiskResourceViewDnDHandler dndHandler = new DiskResourceViewDnDHandler(presenter);
        
        DropTarget gridDropTarget = new DropTarget(grid);
        gridDropTarget.setAllowSelfAsSource(true);
        gridDropTarget.setOperation(Operation.COPY);
        gridDropTarget.addDragEnterHandler(dndHandler);
        gridDropTarget.addDragMoveHandler(dndHandler);
        gridDropTarget.addDropHandler(dndHandler);
        
        DragSource gridDragSource = new DragSource(grid);
        gridDragSource.addDragStartHandler(dndHandler);

        DropTarget treeDropTarget = new DropTarget(tree);
        treeDropTarget.setAllowSelfAsSource(true);
        treeDropTarget.setOperation(Operation.COPY);
        treeDropTarget.addDragEnterHandler(dndHandler);
        treeDropTarget.addDragMoveHandler(dndHandler);
        treeDropTarget.addDropHandler(dndHandler);
        
        DragSource treeDragSource = new DragSource(tree);
        treeDragSource.addDragStartHandler(dndHandler);
        
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setTreeLoader(TreeLoader<Folder> treeLoader) {
        tree.setLoader(treeLoader);
        this.treeLoader = treeLoader;
    }

    @Override
    public Folder getSelectedFolder() {
        return tree.getSelectionModel().getSelectedItem();
    }

    @Override
    public Set<DiskResource> getSelectedDiskResources() {
        return Sets.newHashSet(grid.getSelectionModel().getSelectedItems());
    }

    @Override
    public void setRootFolders(Set<Folder> rootFolders) {
        treeStore.add(Lists.newArrayList(rootFolders));
    }

    @Override
    public TreeStore<Folder> getTreeStore() {
        return treeStore;
    }

    @Override
    public ListStore<DiskResource> getListStore() {
        return listStore;
    }

    @Override
    public boolean isLoaded(Folder folder) {
        TreeNode<Folder> findNode = tree.findNode(folder);
        return findNode.isLoaded();
    }

    @Override
    public void setDiskResources(Set<DiskResource> folderChildren) {
        grid.getStore().clear();
        grid.getStore().addAll(folderChildren);
    }

    @Override
    public void setWestWidgetHidden(boolean hideWestWidget) {
        westData.setHidden(hideWestWidget);
    }

    @Override
    public void setCenterWidgetHidden(boolean hideCenterWidget) {
        // If we are hiding the center widget, update west data to fill available space.
        if (hideCenterWidget) {
            westData.setSize(1);
        }
        centerData.setHidden(hideCenterWidget);
    }

    @Override
    public void setEastWidgetHidden(boolean hideEastWidget) {
        eastData.setHidden(hideEastWidget);
    }

    @Override
    public void setNorthWidgetHidden(boolean hideNorthWidget) {
        northData.setHidden(hideNorthWidget);
    }

    @Override
    public void setSouthWidget(IsWidget widget) {
        southData.setHidden(false);
        con.setSouthWidget(widget, southData);
    }

    @Override
    public void addDiskResourceSelectChangedHandler(SelectionChangedHandler<DiskResource> selectionChangedHandler) {
        grid.getSelectionModel().addSelectionChangedHandler(selectionChangedHandler);
    }
    
    @Override
    public void addFolderSelectionHandler(SelectionHandler<Folder> selectionHandler) {
        tree.getSelectionModel().addSelectionHandler(selectionHandler);
    }

    @Override
    public void setSelectedFolder(Folder folder) {

        if (treeStore.findModelWithKey(folder.getId()) != null) {
            showDataListingWidget();
            tree.getSelectionModel().setSelection(
                    Lists.newArrayList(treeStore.findModelWithKey(folder.getId())));
        }
    }

    @Override
    public void addFolder(Folder parent, Folder newChild) {
        treeStore.add(parent, newChild);
        listStore.add(newChild);
    }

    @Override
    public Folder getFolderById(String folderId) {
        return treeStore.findModelWithKey(folderId);
    }

    @Override
    public void expandFolder(Folder folder) {
        tree.setExpanded(folder, true);
    }

    @Override
    public void deSelectDiskResources() {
        grid.getSelectionModel().deselectAll();
    }

    @Override
    public Set<Folder> getRootFolders() {
        return Sets.newHashSet(treeStore.getRootItems());
    }

    @Override
    public void refreshAll() {
        treeStore.clear();
        treeLoader.load(null);
    }

    @Override
    public void refreshFolder(Folder folder) {
        if (folder == null) {
            return;
        }

        if (!isLoaded(folder)) {
            treeLoader.load(folder);
        } else {
            treeStore.removeChildren(folder);
            treeLoader.load(folder);
        }
    }

    @Override
    public DiskResourceViewToolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void mask(String loadingMask) {
        con.mask(loadingMask);
    }

    @Override
    public void unmask() {
        con.unmask();
    }

    @Override
    public <D extends DiskResource> void removeDiskResources(Collection<D> resources) {
        for (DiskResource dr : resources) {
            listStore.remove(dr);
            if (dr instanceof Folder) {
                treeStore.remove((Folder)dr);
            }
        }
    }

    @Override
    public void updateDiskResource(DiskResource originalDr, DiskResource newDr) {
        // Check each store for for existence of original disk resource
        Folder treeStoreModel = treeStore.findModelWithKey(originalDr.getId());
        if (treeStoreModel != null) {

            // Grab original disk resource's parent, then remove original from tree store
            Folder parentFolder = treeStore.getParent(treeStoreModel);
            treeStore.remove(treeStoreModel);

            treeStoreModel.setId(newDr.getId());
            treeStoreModel.setName(newDr.getName());
            treeStore.add(parentFolder, treeStoreModel);
        }

        DiskResource listStoreModel = listStore.findModelWithKey(originalDr.getId());
        if (listStoreModel != null) {
            listStore.remove(listStoreModel);
            listStore.add(newDr);
        }
    }

    @Override
    public boolean isViewTree(IsWidget widget){
        return widget.asWidget() == tree;
    }
    
    @Override
    public boolean isViewGrid(IsWidget widget){
        return widget.asWidget() == grid;
    }
    
    @Override
    public TreeNode<Folder> findTreeNode(Element el){
        return tree.findNode(el);
    }

    @Override
    public Element findGridRow(Element el) {
        Element row = grid.getView().findRow(el);
        if (row == null && listStore.size() > 0) {
            row = grid.getView().getRow(grid.getStore().size() - 1).cast();
        }
        return row;
    }

    @Override
    public int findRowIndex(Element targetRow) {
        return grid.getView().findRowIndex(targetRow);
    }

    @Override
    public void setSingleSelect() {
        grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // Hide the checkbox column
        cm.setHidden(0, true);
    }

    @Override
    public void disableDiskResourceHyperlink() {
        Cell<DiskResource> cell = cm.getCell(1);
        if (cell instanceof DiskResourceNameCell) {
            ((DiskResourceNameCell)cell).setHyperlinkEnabled(false);
        }

    }

    @Override
    public void showDataListingWidget() {
        centerPanel.clear();
        centerPanel.add(grid, centerData);
    }

    @Override
    public void showSearchResultWidget(IsWidget w) {
        w.asWidget().setHeight(centerPanel.getOffsetHeight(true) + "px");
        centerPanel.clear();
        centerPanel.add(w.asWidget(), centerData);
    }


    @Override
    public void updateDetails(String path, DiskResourceInfo info) {
        detailsPanel.clear();
        Set<DiskResource> selection = getSelectedDiskResources();
        VerticalLayoutContainer c = new VerticalLayoutContainer();
        detailsPanel.setWidget(c);

        // gaurd race condition
        if (selection != null && selection.size() == 1) {
            Iterator<DiskResource> it = selection.iterator();
            if (it.next().getId().equals(path)) {
                c.add(getDateLabel(I18N.DISPLAY.lastModified(), new Date(info.getModified())));
                c.add(getDateLabel(I18N.DISPLAY.createdDate(), new Date(info.getCreated())));
                c.add(getPermissionsLabel(I18N.DISPLAY.permissions(), info.getPermissions()));
                c.add(getSharingLabel(I18N.DISPLAY.share(), info.getShareCount()));
                if (info.getType().equalsIgnoreCase("file")) {
                    c.add(getNumberLabel(I18N.DISPLAY.size(), info.getSize()));

                } else {
                    c.add(getDirFileCount(I18N.DISPLAY.files() + " / " + I18N.DISPLAY.folders(),
                            info.getFileCount(), info.getDirCount()));
                }
            }

        }
    }

    @Override
    public void resetDetailsPanel() {
        detailsPanel.clear();
        FieldLabel fl = new FieldLabel();
        fl.setLabelSeparator("");
        fl.setHTML("<span style='font-size:10px'><b>" + I18N.DISPLAY.noDetails() + "</b> </span>");
        detailsPanel.add(fl);
    }
    /**
     * Parses a timestamp string into a formatted date string and adds it to this panel.
     * 
     * @param label
     * @param value
     */
    private HorizontalPanel getDateLabel(String label, Date date) {
        HorizontalPanel panel = buildDeatilsRow();
        String value = "";

        if (date != null) {
            DateTimeFormat formatter = DateTimeFormat
                    .getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM);

            value = formatter.format(date);
        }

       
        FieldLabel fl = new FieldLabel();
        fl.setHTML("<span style='font-size:10px'><b>" + label + "</b> </span>");
        panel.add(fl);

        FieldLabel fv = new FieldLabel();
        fv.setLabelSeparator("");
        fv.setHTML("<span style='font-size:10px'>" + value + "</span>");
        panel.add(fv);

        return panel;

    }

    private HorizontalPanel getNumberLabel(String label, long value) {
        HorizontalPanel panel = buildDeatilsRow();
        FieldLabel fl = new FieldLabel();
        fl.setHTML("<span style='font-size:10px'><b>" + label + "</b></span>");
        panel.add(fl);

        FieldLabel fv = new FieldLabel();
        fv.setLabelSeparator("");
        fv.setHTML("<span style='font-size:10px'>" + value + "</span>");
        panel.add(fv);

        return panel;
    }

    private HorizontalPanel getDirFileCount(String label, int file_count, int dir_count) {
        HorizontalPanel panel = buildDeatilsRow();
        FieldLabel fl = new FieldLabel();
        fl.setHTML("<span style='font-size:10px'><b>" + label + "</b></span>");
        panel.add(fl);

        FieldLabel fv = new FieldLabel();
        fv.setLabelSeparator("");
        fv.setHTML("<span style='font-size:10px'>" + file_count + " / " + dir_count + "</span>");
        panel.add(fv);

        return panel;
    }




    /**
     * Add permissions detail
     * 
     */
    private HorizontalPanel getPermissionsLabel(String label, Permissions p) {
        HorizontalPanel panel = buildDeatilsRow();
        String value;
        if (p.isOwner()) {
            value = I18N.DISPLAY.owner();
        }
        if (!p.isWritable()) {
            value = I18N.DISPLAY.readOnly();
        } else {
            value = I18N.DISPLAY.readWrite();
        }

        FieldLabel fl = new FieldLabel();
        fl.setHTML("<span style='font-size:10px'><b>" + label + "</b></span>");
        panel.add(fl);

        FieldLabel fv = new FieldLabel();
        fv.setLabelSeparator("");
        fv.setHTML("<span style='font-size:10px'>" + value + "</span>");
        panel.add(fv);

        return panel;
    }

    private HorizontalPanel buildDeatilsRow() {
        HorizontalPanel panel = new HorizontalPanel();
        panel.setHeight("25px");
        panel.setSpacing(3);
        return panel;
    }

    /**
     * 
     * Add sharing info
     * 
     */

    private HorizontalPanel getSharingLabel(String label, int shareCount) {
        Anchor link = null;
        HorizontalPanel panel = buildDeatilsRow();
        if (shareCount == 0) {
            link = new Anchor(I18N.DISPLAY.nosharing());
        } else {
            link = new Anchor("" + shareCount);
        }

        link.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                DataSharingDialog dsd = new DataSharingDialog(getSelectedDiskResources());
                dsd.show();
                
            }
        });

        FieldLabel fl = new FieldLabel();
        fl.setHTML("<span style='font-size:10px'><b>" + label + "</b></span>");
        panel.add(fl);
        panel.add(link);
        return panel;

    }

}
