package org.iplantc.core.uidiskresource.client.sharing.views;

import java.util.List;

import org.iplantc.core.uicommons.client.models.collaborators.Collaborator;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.sharing.models.DataSharing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
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
    FramedPanel diskResourceListPnl;

 
    @UiField(provided = true)
    ColumnModel<DiskResource> diskResourcesColumnModel;

    @UiField(provided = true)
    ListStore<DiskResource> diskResourcesListStore;

 
    @UiField
    Grid<DiskResource> diskResourcesGrid;

 
    Presenter presenter;

    final Widget widget;

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiTemplate("DataSharingView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, DataSharingViewImpl> {
    }

    public DataSharingViewImpl(ColumnModel<DiskResource> diskReColumnModel,
            ListStore<DiskResource> drStore) {
        this.diskResourcesColumnModel = diskReColumnModel;
        this.diskResourcesListStore = drStore;
        widget = uiBinder.createAndBindUi(this);
    }

 
    @Override
    public Widget asWidget() {
        return widget;
    }

  
    @Override
    public void setPresenter(Presenter dataSharingPresenter) {
        this.presenter = dataSharingPresenter;
    }

   

    @Override
    public void setSelectedDiskResources(List<DiskResource> models) {
        if (models != null && models.size() > 0) {
            diskResourcesListStore.clear();
            diskResourcesListStore.addAll(models);
        }

    }


	@Override
	public void addShareWidget(Widget widget) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setCollaborators(List<Collaborator> models) {
		// TODO Auto-generated method stub
		
	}


}
