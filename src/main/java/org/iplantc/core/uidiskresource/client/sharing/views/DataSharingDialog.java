/**
 *
 */
package org.iplantc.core.uidiskresource.client.sharing.views;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.core.uicommons.client.widgets.ContextualHelpPopup;
import org.iplantc.core.uidiskresource.client.models.DiskResourceModelKeyProvider;
import org.iplantc.core.uidiskresource.client.sharing.presenter.DataSharingPresenter;
import org.iplantc.core.uidiskresource.client.sharing.views.DataSharingView.Presenter;
import org.iplantc.core.uidiskresource.client.views.cells.DiskResourceNameCell;

import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

/**
 * @author sriram
 *
 */
public class DataSharingDialog extends IPlantDialog {

    public DataSharingDialog(Set<DiskResource> resources) {
        super(true);
        setPixelSize(600, 500);
        setHideOnButtonClick(true);
        setModal(true);
        setResizable(false);
        addHelp();
        setHeadingText(I18N.DISPLAY.manageSharing());
        ListStore<DiskResource> drStore = new ListStore<DiskResource>(new DiskResourceModelKeyProvider());
        DataSharingView view = new DataSharingViewImpl(buildDiskResourceColumnModel(), drStore);
        final Presenter p = new DataSharingPresenter(getSelectedResourcesAsList(resources), view);
        p.go(this);
        setOkButtonText(I18N.DISPLAY.done());
        addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                p.processRequest();
            }
        });

    }

    private void addHelp() {
        final ToolButton toolBtn = gelHelpToolButton();
        toolBtn.addSelectHandler(new SelectHandler() {
            
            @Override
            public void onSelect(SelectEvent event) {
                ContextualHelpPopup popup = new ContextualHelpPopup();
                popup.add(new HTML(I18N.HELP.sharePermissionsHelp()));
                popup.showAt(toolBtn.getAbsoluteLeft(), toolBtn.getAbsoluteTop() + 15);
                
            }
        });
    }

    private ColumnModel<DiskResource> buildDiskResourceColumnModel() {
        List<ColumnConfig<DiskResource, ?>> list = new ArrayList<ColumnConfig<DiskResource, ?>>();

        ColumnConfig<DiskResource, DiskResource> name = new ColumnConfig<DiskResource, DiskResource>(
                new IdentityValueProvider<DiskResource>(), 130, I18N.DISPLAY.name());
        name.setCell(new DiskResourceNameCell(this, DiskResourceNameCell.CALLER_TAG.SHARING));

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
