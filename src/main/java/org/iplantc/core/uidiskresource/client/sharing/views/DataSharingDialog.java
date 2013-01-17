/**
 * 
 */
package org.iplantc.core.uidiskresource.client.sharing.views;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.iplantc.core.uicommons.client.I18N;
import org.iplantc.core.uicommons.client.models.collaborators.Collaborator;
import org.iplantc.core.uicommons.client.models.collaborators.CollaboratorKeyProvider;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceModelKeyProvider;
import org.iplantc.core.uidiskresource.client.sharing.presenter.DataSharingPresenter;
import org.iplantc.core.uidiskresource.client.sharing.views.DataSharingView.Presenter;
import org.iplantc.core.uidiskresource.client.views.cells.DiskResourceNameCell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
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

    private CheckBoxSelectionModel<Collaborator> collabCheckBoxModel;
    private CheckBoxSelectionModel<DiskResource> drCheckBoxModel;

    public DataSharingDialog(Set<DiskResource> resources) {
        setPixelSize(800, 400);
        setHideOnButtonClick(true);
        setModal(true);
        setResizable(false);
        setHeadingText(org.iplantc.core.uidiskresource.client.I18N.DISPLAY.manageSharing());
        ListStore<Collaborator> collabStore = new ListStore<Collaborator>(new CollaboratorKeyProvider());
        ListStore<DiskResource> drStore = new ListStore<DiskResource>(new DiskResourceModelKeyProvider());
        DataSharingView view = new DataSharingViewImpl(buildCollaboratorsColumnModel(),
                collabCheckBoxModel, buildDiskResourceColumnModel(), drCheckBoxModel, collabStore,
                drStore);
        final Presenter p = new DataSharingPresenter(getSelectedResourcesAsList(resources), view);
        p.go(this);
        getButtonById(PredefinedButton.OK.toString()).setText("Done");
        getButtonById(PredefinedButton.OK.toString()).addSelectHandler(new SelectHandler() {
            
            @Override
            public void onSelect(SelectEvent event) {
                p.processRequest();
            }
        });
    }

    private ColumnModel<Collaborator> buildCollaboratorsColumnModel() {
        List<ColumnConfig<Collaborator, ?>> configs = new ArrayList<ColumnConfig<Collaborator, ?>>();
        IdentityValueProvider<Collaborator> valueProvider = new IdentityValueProvider<Collaborator>();

        collabCheckBoxModel = new CheckBoxSelectionModel<Collaborator>(valueProvider);

        configs.add(collabCheckBoxModel.getColumn());

        ColumnConfig<Collaborator, Collaborator> name = new ColumnConfig<Collaborator, Collaborator>(
                valueProvider, 150);
        name.setHeader(I18N.DISPLAY.name());
        name.setCell(new AbstractCell<Collaborator>() {

            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, Collaborator value,
                    SafeHtmlBuilder sb) {
                StringBuilder builder = new StringBuilder();
                if (value.getFirstName() != null && !value.getFirstName().isEmpty()) {
                    builder.append(value.getFirstName());
                    if (value.getLastName() != null && !value.getLastName().isEmpty()) {
                        builder.append(" " + value.getLastName());
                    }
                    sb.appendEscaped(builder.toString());
                } else {
                    sb.appendEscaped(value.getUserName());
                }

            }
        });
        configs.add(name);
        
        return new ColumnModel<Collaborator>(configs);
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
