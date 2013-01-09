/**
 * 
 */
package org.iplantc.core.uidiskresource.client.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceModelKeyProvider;
import org.iplantc.core.uidiskresource.client.views.cells.DiskResourceNameCell;
import org.iplantc.core.uidiskresource.client.views.cells.DiskResourcePathCell;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

/**
 * @author sriram
 *
 */
public class DiskResourceSearchView implements IsWidget {

    private Grid<DiskResource> grid;

    private static CheckBoxSelectionModel<DiskResource> sm;

    public DiskResourceSearchView() {
        init();

    }

    private void init() {
        ListStore<DiskResource> store = new ListStore<DiskResource>(new DiskResourceModelKeyProvider());
        grid = new Grid<DiskResource>(store, new ColumnModel<DiskResource>(createColumnConfigList()));
        grid.setSelectionModel(sm);
    }

    public void loadResults(List<DiskResource> searchResults) {
        grid.getStore().clear();
        grid.getStore().addAll(searchResults);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
     */
    @Override
    public Widget asWidget() {
        return grid;
    }

    private List<ColumnConfig<DiskResource, ?>> createColumnConfigList() {
        List<ColumnConfig<DiskResource, ?>> list = new ArrayList<ColumnConfig<DiskResource, ?>>();

        sm = new CheckBoxSelectionModel<DiskResource>(new IdentityValueProvider<DiskResource>());
        ColumnConfig<DiskResource, DiskResource> name = new ColumnConfig<DiskResource, DiskResource>(
                new IdentityValueProvider<DiskResource>(), 130, I18N.DISPLAY.name());
        name.setCell(new DiskResourceNameCell(DiskResourceNameCell.CALLER_TAG.SEARCH));


        ColumnConfig<DiskResource, DiskResource> path = new ColumnConfig<DiskResource, DiskResource>(
                new IdentityValueProvider<DiskResource>(),
                200, "Path");
        path.setCell(new DiskResourcePathCell());

        list.add(sm.getColumn());
        list.add(name);
        list.add(path);

        return list;
    }

}
