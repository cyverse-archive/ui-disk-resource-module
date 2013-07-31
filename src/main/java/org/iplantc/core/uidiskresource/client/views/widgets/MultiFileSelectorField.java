package org.iplantc.core.uidiskresource.client.views.widgets;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.events.UserSettingsUpdatedEvent;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.models.DiskResourceModelKeyProvider;
import org.iplantc.core.uidiskresource.client.models.DiskResourceProperties;
import org.iplantc.core.uidiskresource.client.views.dialogs.FileSelectDialog;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.dnd.core.client.DND.Operation;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent.DndDragEnterHandler;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent.DndDragMoveHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.dnd.core.client.StatusProxy;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * TODO JDS Implement drag and drop
 *
 * @author jstroot
 *
 */
public class MultiFileSelectorField extends Composite implements IsField<List<HasId>>,
        ValueAwareEditor<List<HasId>>, HasValueChangeHandlers<List<HasId>>, DndDragEnterHandler,
        DndDragMoveHandler, DndDropHandler {

    interface MultiFileSelectorFieldUiBinder extends UiBinder<Widget, MultiFileSelectorField> {
    }

    private static MultiFileSelectorFieldUiBinder BINDER = GWT.create(MultiFileSelectorFieldUiBinder.class);

    @UiField
    ToolBar toolbar;

    @UiField
    TextButton addButton;

    @UiField
    TextButton deleteButton;

    @UiField
    Grid<DiskResource> grid;

    @UiField
    GridView<DiskResource> gridView;

    @UiField
    ListStore<DiskResource> listStore;

    @UiField
    ColumnModel<DiskResource> cm;

    private boolean addDeleteButtonsEnabled = true;

    UserSettings userSettings = UserSettings.getInstance();

    public MultiFileSelectorField() {
        initWidget(BINDER.createAndBindUi(this));

        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<DiskResource>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<DiskResource> event) {
                List<DiskResource> selection = event.getSelection();
                deleteButton.setEnabled((selection != null) && !selection.isEmpty());
            }
        });

        grid.setBorders(true);

        initDragAndDrop();
    }

    private void initDragAndDrop() {
        DropTarget dataDrop = new DropTarget(this);
        dataDrop.setOperation(Operation.COPY);
        dataDrop.addDragEnterHandler(this);
        dataDrop.addDragMoveHandler(this);
        dataDrop.addDropHandler(this);
    }

    @UiFactory
    ColumnModel<DiskResource> createColumnModel() {
        List<ColumnConfig<DiskResource, ?>> list = Lists.newArrayList();
        DiskResourceProperties props = GWT.create(DiskResourceProperties.class);

        ColumnConfig<DiskResource, String> name = new ColumnConfig<DiskResource, String>(props.name(), 130, I18N.DISPLAY.name());
        list.add(name);
        return new ColumnModel<DiskResource>(list);
    }

    @UiFactory
    ListStore<DiskResource> createListStore() {
        return new ListStore<DiskResource>(new DiskResourceModelKeyProvider());
    }

    @UiHandler("addButton")
    void onAddButtonSelected(SelectEvent event) {
        if (!addDeleteButtonsEnabled) {
            return;
        }
        // Open a multiselect file selector
        FileSelectDialog dlg = null;

        if (userSettings.isRememberLastPath()) {
            String id = userSettings.getLastPathId();
            dlg = FileSelectDialog.selectParentFolderById(id,false);
        } else {
            dlg = FileSelectDialog.selectParentFolderById(null,false);
        }
        dlg.addHideHandler(new FileSelectDialogHideHandler(dlg, listStore));
        dlg.show();
    }

    @UiHandler("deleteButton")
    void onDeleteButtonSelected(SelectEvent event) {
        if (!addDeleteButtonsEnabled) {
            return;
        }
        for (DiskResource dr : grid.getSelectionModel().getSelectedItems()) {
            listStore.remove(dr);
        }
    }

    @Override
    public void setValue(List<HasId> value) {
        if ((value == null) || !value.isEmpty())
            return;

        // TBI JDS Assume the incoming value is a JSON array of ..... ?

    }

    @Override
    public List<HasId> getValue() {
        List<HasId> hasIdList = Lists.newArrayList();
        for (DiskResource dr : listStore.getAll()) {
            hasIdList.add(dr);
        }
        return hasIdList;
    }

    private final class FileSelectDialogHideHandler implements HideHandler {
        private final FileSelectDialog dlg;
        private final ListStore<DiskResource> store;

        public FileSelectDialogHideHandler(final FileSelectDialog dlg, final ListStore<DiskResource> store) {
            this.dlg = dlg;
            this.store = store;
        }

        @Override
        public void onHide(HideEvent event) {
            Set<DiskResource> diskResources = dlg.getDiskResources();
            if ((diskResources == null) || diskResources.isEmpty()) {
                return;
            }
            store.addAll(diskResources);
            if (userSettings.isRememberLastPath()) {
                userSettings.setLastPathId(DiskResourceUtil.parseParent(store.get(0).getId()));
                UserSettingsUpdatedEvent usue = new UserSettingsUpdatedEvent();
                EventBus.getInstance().fireEvent(usue);
            }
            ValueChangeEvent.fire(MultiFileSelectorField.this, Lists.<HasId> newArrayList(store.getAll()));
        }
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public void clearInvalid() {
        // TODO Auto-generated method stub
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isValid(boolean preventMark) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean validate(boolean preventMark) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setDelegate(EditorDelegate<List<HasId>> delegate) {
        // TODO Auto-generated method stub

    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPropertyChange(String... paths) {
        // TODO Auto-generated method stub

    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<HasId>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public void disableAddDeleteButtons() {
        addDeleteButtonsEnabled = false;
    }

    public void setEmptyText(String emptyText) {
        gridView.setEmptyText(emptyText);
    }

    protected boolean validateDropStatus(Set<DiskResource> dropData, StatusProxy status) {
        if (dropData == null || dropData.isEmpty()) {
            status.setStatus(false);
            return false;
        }

        // Reset status message
        status.setStatus(true);
        status.update(I18N.DISPLAY.dataDragDropStatusText(dropData.size()));

        return true;
    }

    @Override
    public void onDragEnter(DndDragEnterEvent event) {
        Set<DiskResource> dropData = getDropData(event.getDragSource().getData());

        if (!validateDropStatus(dropData, event.getStatusProxy())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onDragMove(DndDragMoveEvent event) {
        Set<DiskResource> dropData = getDropData(event.getDragSource().getData());

        if (!validateDropStatus(dropData, event.getStatusProxy())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onDrop(DndDropEvent event) {
        Set<DiskResource> dropData = getDropData(event.getData());

        if (validateDropStatus(dropData, event.getStatusProxy())) {
            for (DiskResource data : dropData) {
                if (listStore.findModel(data) == null) {
                    listStore.add(data);
                }
            }
            ValueChangeEvent.fire(this, Lists.<HasId> newArrayList(dropData));
        }
    }

    @SuppressWarnings("unchecked")
    protected Set<DiskResource> getDropData(Object data) {
        if (!(data instanceof Collection<?>)) {
            return null;
        }
        Collection<?> dataColl = (Collection<?>)data;
        if (dataColl.isEmpty() || !(dataColl.iterator().next() instanceof DiskResource)) {
            return null;
        }

        Set<DiskResource> dropData = null;
        dropData = Sets.newHashSet((Collection<DiskResource>)dataColl);

        return dropData;
    }
}
