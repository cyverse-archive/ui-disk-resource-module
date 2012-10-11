package org.iplantc.core.uidiskresource.client.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeAppearance;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;

public class DiskResourceViewImpl implements DiskResourceView {

    @UiTemplate("DiskResourceView.ui.xml")
    interface DiskResourceViewUiBinder extends UiBinder<Widget, DiskResourceViewImpl> {
    }

    private static DiskResourceViewUiBinder BINDER = GWT.create(DiskResourceViewUiBinder.class);

    private Presenter presenter;

    @UiField
    BorderLayoutContainer con;

    @UiField
    ContentPanel westPanel;

    @UiField
    Tree<Folder, String> tree;

    @UiField
    TreeStore<Folder> treeStore;

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
    ContentPanel eastPanel;

    @UiField
    BorderLayoutData northData;
    @UiField
    BorderLayoutData eastData;

    private final Widget widget;

    public DiskResourceViewImpl() {
        widget = BINDER.createAndBindUi(this);

        // Set Leaf icon to a folder
        TreeStyle treeStyle = tree.getStyle();
        TreeAppearance appearance = tree.getAppearance();
        treeStyle.setLeafIcon(appearance.closeNodeIcon());

        tree.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Folder>() {

            @Override
            public void onSelectionChanged(SelectionChangedEvent<Folder> event) {
                if ((event.getSelection() != null) && !event.getSelection().isEmpty()) {
                    onFolderSelected(event.getSelection().get(0));
                }
            }

        });
        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<DiskResource>() {

            @Override
            public void onSelectionChanged(SelectionChangedEvent<DiskResource> event) {
                if ((event.getSelection() != null) && !event.getSelection().isEmpty()) {
                    presenter.onDiskResourceSelected(event.getSelection());
                }
            }

        });
    }

    @Override
    public void onDiskResourceSelected(List<DiskResource> selection) {
        onDiskResourceSelected(selection);
    }

    @Override
    public void onFolderSelected(Folder folder) {
        presenter.onFolderSelected(folder);
    }

    @UiFactory
    TreeStore<Folder> createTreeStore() {
        return new TreeStore<Folder>(new DiskResourceModelKeyProvider());
    }

    @UiFactory
    ListStore<DiskResource> createListStore() {
        return new ListStore<DiskResource>(new DiskResourceModelKeyProvider());
    }

    private class DiskResourceModelKeyProvider implements ModelKeyProvider<DiskResource> {
        @Override
        public String getKey(DiskResource item) {
            return item.getId();
        }
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
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setTreeLoader(TreeLoader<Folder> treeLoader) {
        tree.setLoader(treeLoader);
    }

    @Override
    public Folder getSelectedFolder() {
        return tree.getSelectionModel().getSelectedItem();
    }

    @Override
    public List<DiskResource> getSelectedDiskResources() {
        return grid.getSelectionModel().getSelectedItems();
    }

    @Override
    public void setRootFolders(List<Folder> rootFolders) {
        treeStore.add(rootFolders);
    }

    @Override
    public TreeStore<Folder> getTreeStore() {
        return treeStore;
    }

    @Override
    public boolean isLoaded(Folder folder) {
        return tree.findNode(folder).isLoaded();
    }

    @Override
    public void setDiskResources(ArrayList<DiskResource> folderChildren) {
        grid.getStore().clear();
        grid.getStore().addAll(folderChildren);
    }

    @Override
    public void setNorthWidget(IsWidget widget) {
        northData.setHidden(false);
        con.setNorthWidget(widget, northData);
    }

}
