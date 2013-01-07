package org.iplantc.core.uidiskresource.client.sharing.views;

import java.util.List;

import org.iplantc.core.uicommons.client.models.collaborators.Collaborator;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.models.autobeans.Permissions;
import org.iplantc.core.uidiskresource.client.sharing.models.DataSharing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.dnd.core.client.DND.Operation;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class DataSharingViewImpl implements DataSharingView {

    @UiField
    BorderLayoutContainer con;

    @UiField
    FramedPanel collaboratorListPnl;

    @UiField
    FramedPanel diskResourceListPnl;

    @UiField(provided = true)
    ColumnModel<Collaborator> collaboratorsColumnModel;

    @UiField(provided = true)
    ListStore<Collaborator> collaboratorsListStore;

    @UiField(provided = true)
    ColumnModel<DiskResource> diskResourcesColumnModel;

    @UiField(provided = true)
    ListStore<DiskResource> diskResourcesListStore;

    @UiField
    Grid<Collaborator> collaboratorsGrid;

    @UiField
    Grid<DiskResource> diskResourcesGrid;

    @UiField
    FramedPanel permissionsPnl;

    Presenter presenter;

    final Widget widget;

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiTemplate("DataSharingView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, DataSharingViewImpl> {
    }

    public DataSharingViewImpl(ColumnModel<Collaborator> collaboratorsColumnModel,
            CheckBoxSelectionModel<Collaborator> collabSm, ColumnModel<DiskResource> diskReColumnModel,
            CheckBoxSelectionModel<DiskResource> drSm, ListStore<Collaborator> collabStore,
            ListStore<DiskResource> drStore) {
        this.collaboratorsColumnModel = collaboratorsColumnModel;
        this.collaboratorsListStore = collabStore;
        this.diskResourcesColumnModel = diskReColumnModel;
        this.diskResourcesListStore = drStore;
        widget = uiBinder.createAndBindUi(this);
        collaboratorsGrid.setSelectionModel(collabSm);
        diskResourcesGrid.setSelectionModel(drSm);
        initDragAndDrop();
    }

    private void initDragAndDrop() {
        new GridDragSource<Collaborator>(collaboratorsGrid) {
            @Override
            protected void onDragStart(DndDragStartEvent event) {
                List<Collaborator> list = collaboratorsGrid.getSelectionModel().getSelectedItems();
                if (list == null || list.size() == 0) {
                    event.setCancelled(true);
                } else {
                    event.setData(list);
                    event.setCancelled(false);
                }
            }
        };

        new GridDragSource<DiskResource>(diskResourcesGrid) {
            @Override
            protected void onDragStart(DndDragStartEvent event) {
                List<DiskResource> list = diskResourcesGrid.getSelectionModel().getSelectedItems();
                if (list == null || list.size() == 0) {
                    event.setCancelled(true);
                } else {
                    event.setData(list);
                    event.setCancelled(false);
                }
            }

        };
        new DiskResourceDropTarget(diskResourcesGrid);

    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void addShareWidget(Widget widget) {
        permissionsPnl.add(widget);
    }

    @Override
    public void setPresenter(Presenter dataSharingPresenter) {
        this.presenter = dataSharingPresenter;
    }

    @Override
    public void setCollaborators(List<Collaborator> models) {
        if (models != null && models.size() > 0) {
            collaboratorsListStore.clear();
            collaboratorsListStore.addAll(models);
        }

    }

    @Override
    public void setSelectedDiskResources(List<DiskResource> models) {
        if (models != null && models.size() > 0) {
            diskResourcesListStore.clear();
            diskResourcesListStore.addAll(models);
        }

    }

    private final class DiskResourceDropTarget extends GridDropTarget<DiskResource> {
        private DiskResourceDropTarget(Grid<DiskResource> grid) {
            super(grid);
            setOperation(Operation.COPY);
        }

        @Override
        public void onDragMove(DndDragMoveEvent e) {
            super.onDragMove(e);
            Element data = e.getDragMoveEvent().getNativeEvent().getEventTarget().cast();
            if (data == null) {
                e.getStatusProxy().setStatus(false);
                e.setCancelled(false);
            }

            int row = diskResourcesGrid.getView().findRowIndex(data);
            if (row < 0) {
                e.getStatusProxy().setStatus(false);
                e.setCancelled(false);
            }
        }

        @Override
        public void onDragEnter(DndDragEnterEvent e) {
            Element data = e.getDragEnterEvent().getNativeEvent().getEventTarget().cast();
            if (data == null) {
                e.getStatusProxy().setStatus(false);
                e.setCancelled(false);
            }

            int row = diskResourcesGrid.getView().findRowIndex(data);
            if (row < 0) {
                e.getStatusProxy().setStatus(false);
                e.setCancelled(false);
            }
        }

        @Override
        public void onDragDrop(DndDropEvent e) {
            Element data = (Element)e.getDragEndEvent().getNativeEvent().getEventTarget().cast();
            if (data == null) {
                e.getStatusProxy().setStatus(false);
                return;
            }

            int row = diskResourcesGrid.getView().findRowIndex(data);
            if (row < 0) {
                e.getStatusProxy().setStatus(false);
                return;
            }
            DiskResource dr = diskResourcesGrid.getStore().get(row);
            @SuppressWarnings("unchecked")
            List<Collaborator> selectedCollabs = (List<Collaborator>)e.getData();
            FastMap<DataSharing> smap = new FastMap<DataSharing>();
            for (Collaborator c : selectedCollabs) {
                DataSharing ds = new DataSharing(c, presenter.getDefaultPermissions(), dr.getId());
                smap.put(c.getUserName(), ds);
            }

            presenter.addDataSharing(smap);
        }


    }

}
