/**
 * 
 */
package org.iplantc.core.uidiskresource.client.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent.HeaderClickHandler;
import com.sencha.gxt.widget.core.client.event.RefreshEvent;
import com.sencha.gxt.widget.core.client.event.RefreshEvent.RefreshHandler;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowMouseDownEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;

/**
 * This class is copy of GXT's CheckBoxSelectionModel. But modified to fit live grid view which is not supported by GXT.
 * @author sriram
 * 
 */
public class DiskResourceSelectionModel extends GridSelectionModel<DiskResource> {
    public interface CheckBoxColumnAppearance {

        boolean isHeaderChecked(XElement header);

        void onHeaderChecked(XElement header, boolean checked);

        void renderCheckBox(Context context, DiskResource value, SafeHtmlBuilder sb);
    }

    protected ColumnConfig<DiskResource, DiskResource> config;

    private final CheckBoxColumnAppearance appearance = GWT.create(CheckBoxColumnAppearance.class);

    private GroupingHandlerRegistration handlerRegistration = new GroupingHandlerRegistration();

    private Map<String,DiskResource> selectedItemsCache = new HashMap<String,DiskResource>();

    private int viewIndex;

    private int rowcount;

    /**
     * Creates a CheckBoxSelectionModel that will operate on the row itself. To customize the row it is
     * acting on, use the {@link #CheckBoxSelectionModel(ValueProvider)} constructor.
     */
    public DiskResourceSelectionModel() {
        this(new IdentityValueProvider<DiskResource>());
    }

    /**
     * Creates a CheckBoxSelectionModel with a custom ValueProvider instance.
     * 
     * @param valueProvider the ValueProvider to use when constructing a ColumnConfig
     */
    public DiskResourceSelectionModel(ValueProvider<DiskResource, DiskResource> valueProvider) {
        config = newColumnConfig(valueProvider);
        config.setColumnClassSuffix("checker");
        config.setWidth(20);
        config.setSortable(false);
        config.setResizable(false);
        config.setFixed(true);
        config.setMenuDisabled(true);

        config.setCell(new AbstractCell<DiskResource>() {
            @Override
            public void render(Context context, DiskResource value, SafeHtmlBuilder sb) {
                appearance.renderCheckBox(context, value, sb);
            }
        });

        deselectOnSimpleClick = false;
       
    }

    @Override
    public void bindGrid(Grid<DiskResource> grid) {
        if (this.grid != null) {
            handlerRegistration.removeHandler();
        }
        super.bindGrid(grid);

        if (grid != null) {
            handlerRegistration.add(grid.addHeaderClickHandler(new HeaderClickHandler() {
                @Override
                public void onHeaderClick(HeaderClickEvent event) {
                    handleHeaderClick(event);
                }
            }));

            handlerRegistration.add(grid.addRefreshHandler(new RefreshHandler() {
                @Override
                public void onRefresh(RefreshEvent event) {
                    DiskResourceSelectionModel.this.onRefresh(event);
                }
            }));
        }

    }

    /**
     * Returns the column config.
     * 
     * @return the column config
     */
    public ColumnConfig<DiskResource, DiskResource> getColumn() {
        return config;
    }

    /**
     * Returns true if the header checkbox is selected.
     * 
     * @return true if selected
     */
    public boolean isSelectAllChecked() {
        if (grid != null && grid.isViewReady()) {
            XElement hd = grid.getView().getHeader().getElement().child(".x-grid-hd-checker");
            return appearance.isHeaderChecked(hd);
        }
        return false;
    }

    /**
     * Sets the select all checkbox in the grid header and selects / deselects all rows.
     * 
     * @param select true to select all
     */
    public void setSelectAllChecked(boolean select) {
        assert grid.isViewReady() : "cannot call this method before grid has been rendered";
        
        if (!select) {
            setChecked(false);
            deselectAll();
        } else {
            setChecked(true);
            selectAll();
        }
    }

    protected void handleHeaderClick(HeaderClickEvent event) {
        ColumnConfig<DiskResource, ?> c = grid.getColumnModel().getColumn(event.getColumnIndex());
        if (c == config) {
            XElement hd = event.getEvent().getEventTarget().<Element> cast().getParentElement().cast();
            boolean isChecked = appearance.isHeaderChecked(hd);
           /**
            * When  header is checked, everything unselected and all items only in view should be selected.
            * When  header is unchecked, everything in the view should be unselected.  
            */
            clearSelectedItemsCache();
            if (isChecked) {
                setChecked(false);
                deselectAll();
            } else {
                setChecked(true);
                selectAll();
            }
        }
    }

    @Override
    protected void handleRowClick(RowClickEvent event) {
        Element target = event.getEvent().getEventTarget().cast();
        if (target.getClassName().equals("x-grid-row-checker")) {
            return;
        }
        super.handleRowClick(event);
    }
    
    public void clearSelectedItemsCache() {
        selectedItemsCache.clear();
    }

    @Override
    protected void handleRowMouseDown(RowMouseDownEvent event) {
        boolean left = event.getEvent().getButton() == Event.BUTTON_LEFT;
        Element target = event.getEvent().getEventTarget().cast();
        if (left && target.getClassName().equals("x-grid-row-checker")) {
            DiskResource model = listStore.get(event.getRowIndex());
            boolean sel = isSelected(model);
            if (model != null) {
                if (sel) {
                    deselect(model);
                } else if (!sel) {
                    select(model, true);
                }
            }
        } else {
            // when a item is selected by clicking on the row (not on the checkbox)
            //we need to clear selection of everything else and select only that row.
            clearSelectedItemsCache();
            super.handleRowMouseDown(event);
        }
    }

    protected ColumnConfig<DiskResource, DiskResource> newColumnConfig(
            ValueProvider<DiskResource, DiskResource> valueProvider) {
        return new ColumnConfig<DiskResource, DiskResource>(valueProvider);
    }

    @Override
    protected void onAdd(List<? extends DiskResource> models) {
        super.onAdd(models);
        updateHeaderCheckBox();
    }

    @Override
    protected void onClear(StoreClearEvent<DiskResource> event) {
        super.onClear(event);
        updateHeaderCheckBox();
    };

    protected void onRefresh(RefreshEvent event) {
        updateHeaderCheckBox();
    }

    protected void onRemove(DiskResource model) {
        super.onRemove(model);
        updateHeaderCheckBox();
    };

   
    public void addToSelectedCache(DiskResource dr) {
        selectedItemsCache.put(dr.getId(),dr);
    }

    public List<DiskResource> getSelectedItemsCache() {
        return new ArrayList<DiskResource>(selectedItemsCache.values());
    }

    public DiskResource removeFromSelectedCache(DiskResource dr) {
        return selectedItemsCache.remove(dr.getId());
    }

    public void setViewIndex(int index) {
        this.viewIndex = index;
    }

    public void setRowCount(int rowCount) {
        this.rowcount = rowCount;
    }

    @Override
    protected void onSelectChange(DiskResource model, boolean select) {
        super.onSelectChange(model, select);
        if (select) {
                addToSelectedCache(model);
        } else {
            removeFromSelectedCache(model);
        }
        updateHeaderCheckBox();
    }

    public void setChecked(boolean checked) {
        if (grid.isViewReady()) {
            XElement hd = grid.getView().getHeader().getElement().child(".x-grid-hd-checker");
            if (hd != null) {
                appearance.onHeaderChecked(hd.getParentElement().<XElement> cast(), checked);
            }
        }
    }

    //check if everything in the view is selected.
    private void updateHeaderCheckBox() {
        ListStore<DiskResource> store = grid.getStore();
        if(rowcount == 0 || selectedItemsCache.size() == 0 ||selectedItemsCache.size() < rowcount) {
            setChecked(false);
            return;
        }
        for (int i = viewIndex; i < viewIndex + rowcount ; i++) {
            if(store.get(i)!= null && selectedItemsCache.get(store.get(i).getId()) == null) {
                setChecked(false);
                return;
            }
        }
        setChecked(true);
        return;
    }



}
