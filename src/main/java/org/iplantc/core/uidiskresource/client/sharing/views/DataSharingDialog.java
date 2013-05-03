/**
 *
 */
package org.iplantc.core.uidiskresource.client.sharing.views;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.DiskResourceModelKeyProvider;
import org.iplantc.core.uidiskresource.client.sharing.presenter.DataSharingPresenter;
import org.iplantc.core.uidiskresource.client.sharing.views.DataSharingView.Presenter;
import org.iplantc.core.uidiskresource.client.views.cells.DiskResourceNameCell;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

/**
 * @author sriram
 *
 */
public class DataSharingDialog extends Dialog {

    // private CheckBoxSelectionModel<Collaborator> collabCheckBoxModel;
    private CheckBoxSelectionModel<DiskResource> drCheckBoxModel;
    private ToolButton tool_help;

    public DataSharingDialog(Set<DiskResource> resources) {
        setPixelSize(600, 500);
        setHideOnButtonClick(true);
        setModal(true);
        setResizable(false);
        setHeadingText(I18N.DISPLAY.manageSharing());
        ListStore<DiskResource> drStore = new ListStore<DiskResource>(new DiskResourceModelKeyProvider());
        DataSharingView view = new DataSharingViewImpl(buildDiskResourceColumnModel(), drStore);
        final Presenter p = new DataSharingPresenter(getSelectedResourcesAsList(resources), view);
        p.go(this);
        getButtonById(PredefinedButton.OK.toString()).setText("Done");
        getButtonById(PredefinedButton.OK.toString()).addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                p.processRequest();
            }
        });
        tool_help = new ToolButton(ToolButton.QUESTION);
        getHeader().addTool(tool_help);
    }

    private ColumnModel<DiskResource> buildDiskResourceColumnModel() {
        List<ColumnConfig<DiskResource, ?>> list = new ArrayList<ColumnConfig<DiskResource, ?>>();

        drCheckBoxModel = new CheckBoxSelectionModel<DiskResource>(
                new IdentityValueProvider<DiskResource>());
        ColumnConfig<DiskResource, DiskResource> name = new ColumnConfig<DiskResource, DiskResource>(
                new IdentityValueProvider<DiskResource>(), 130, I18N.DISPLAY.name());
        name.setCell(new DiskResourceNameCell(DiskResourceNameCell.CALLER_TAG.SHARING));

        list.add(drCheckBoxModel.getColumn());
        list.add(name);

        return new ColumnModel<DiskResource>(list);
    }

    private List<DiskResource> getSelectedResourcesAsList(Set<DiskResource> models) {
        List<DiskResource> dr = new ArrayList<DiskResource>();

        for (DiskResource item : models) {
            dr.add(item);
        }

        return dr;

    }

}
