/**
 * 
 */
package org.iplantc.core.uidiskresource.client.sharing.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.sharing.models.DataSharing;
import org.iplantc.core.uidiskresource.client.sharing.models.DataSharingKeyProvider;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.views.cells.Resources;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GroupingView;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * @author sriram
 *
 */
public class ShareBreakDownDialog extends Dialog {
	
    private Grid<DataSharing> grid;
    final Resources res = GWT.create(Resources.class);
    public ShareBreakDownDialog(List<DataSharing> shares) {
        init();

        ToolBar toolbar = new ToolBar();
        toolbar.setHeight(30);
        toolbar.add(buildGroupByUserButton());
        toolbar.add(buildGroupByDataButton());
        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.add(toolbar);
        container.add(buildGrid());
        container.setScrollMode(ScrollMode.AUTOY);
        setWidget(container);
        loadGrid(shares);
    }
	
	private void init() {
        setPixelSize(400, 375);
        setHideOnButtonClick(true);
        setModal(true);
        setHeadingText(I18N.DISPLAY.whoHasAccess());
        buildGrid();
    }
	
	private Grid<DataSharing> buildGrid() {
		ListStore<DataSharing> store = new ListStore<DataSharing>(new DataSharingKeyProvider());
		ColumnModel<DataSharing> cm = buildColumnModel();
		GroupingView<DataSharing> view = new GroupingView<DataSharing>();
		view.groupBy(cm.getColumn(0));
        view.setAutoExpandColumn(cm.getColumn(0));
        view.setShowGroupedColumn(false);
        grid = new Grid<DataSharing>(store, cm);
		grid.setView(view);
		return grid;
	}
	
    private void loadGrid(List<DataSharing> shares) {
        grid.getStore().clear();
        grid.getStore().addAll(shares);
    }

    private TextButton buildGroupByUserButton() {
        TextButton button = new TextButton(I18N.DISPLAY.groupByUser());
        button.addSelectHandler(new SelectHandler() {
            
            @Override
            public void onSelect(SelectEvent event) {
                GroupingView<DataSharing> view = (GroupingView<DataSharing>)grid.getView();
                view.groupBy(grid.getColumnModel().getColumn(0));
                
            }
        });
        button.setIcon(org.iplantc.core.uicommons.client.images.Resources.ICONS.share());
        return button;
    }
	
	private TextButton buildGroupByDataButton() {
        TextButton button = new TextButton(I18N.DISPLAY.groupByData());
        button.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                GroupingView<DataSharing> view = (GroupingView<DataSharing>)grid.getView();
                view.groupBy(grid.getColumnModel().getColumn(1));

            }
        });
        button.setIcon(res.folder());
        return button;
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

        name.setHeader(I18N.DISPLAY.name());
        name.setWidth(120);
        
        ColumnConfig<DataSharing, String> diskRsc = new ColumnConfig<DataSharing, String>(
                new ValueProvider<DataSharing, String>() {

                    @Override
                    public String getValue(DataSharing object) {
                            return DiskResourceUtil.parseNameFromPath((object.getPath()));
                    }

                    @Override
                    public void setValue(DataSharing object, String value) {
                        // do nothing intentionally

                    }

                    @Override
                    public String getPath() {
                        return "path";
                    }
                });

        diskRsc.setHeader(org.iplantc.core.uidiskresource.client.I18N.DISPLAY.name());
        diskRsc.setWidth(120);
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

        permission.setHeader(org.iplantc.core.uidiskresource.client.I18N.DISPLAY.permissions());
        permission.setWidth(80);
        configs.add(name);
        configs.add(diskRsc);
        configs.add(permission);
        return new ColumnModel<DataSharing>(configs);

	}
	
	

}
