/**
 * 
 */
package org.iplantc.core.uidiskresource.client.sharing.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uidiskresource.client.sharing.models.DataSharing;
import org.iplantc.core.uidiskresource.client.sharing.models.DataSharingKeyProvider;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
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
	
	public ShareBreakDownDialog(List<DataSharing> shares) {
        init();

        

        ToolBar toolbar = new ToolBar();
        //toolbar.add(buildGroupByUserButton(store));
        //toolbar.add(buildGroupByDataButton(store));

        //add(buildGrid(store, buildColumnModel()));
    }
	
	private void init() {
        setPixelSize(640, 480);
        setHideOnButtonClick(true);
        setModal(true);
        buildGrid();
    }
	
	private Grid<DataSharing> buildGrid() {
		ListStore<DataSharing> store = new ListStore<DataSharing>(new DataSharingKeyProvider());
		ColumnModel<DataSharing> cm = buildColumnModel();
		GroupingView<DataSharing> view = new GroupingView<DataSharing>();
		view.groupBy(cm.getColumn(0));
		Grid<DataSharing> grid = new Grid<DataSharing>(store, cm);
		grid.setView(view);
		return grid;
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
        name.setWidth(170);
        
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

        diskRsc.setHeader("File / Folder Name");
        diskRsc.setWidth(170);
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
	
	

}
