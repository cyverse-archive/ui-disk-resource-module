package org.iplantc.core.uidiskresource.client.dataLink.presenter;

import java.util.List;

import org.iplantc.core.uicommons.client.gin.ServicesInjector;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uicommons.client.util.DiskResourceUtil;
import org.iplantc.core.uicommons.client.util.WindowUtil;
import org.iplantc.core.uidiskresource.client.dataLink.models.DataLink;
import org.iplantc.core.uidiskresource.client.dataLink.models.DataLinkFactory;
import org.iplantc.core.uidiskresource.client.dataLink.view.DataLinkPanel;
import org.iplantc.core.uidiskresource.client.services.callbacks.CreateDataLinkCallback;
import org.iplantc.core.uidiskresource.client.services.callbacks.DeleteDataLinksCallback;
import org.iplantc.core.uidiskresource.client.services.callbacks.ListDataLinksCallback;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasOneWidget;


public class DataLinkPresenter<M extends DiskResource> implements DataLinkPanel.Presenter<M> {

    private final DataLinkPanel<M> view;
    private final DiskResourceServiceFacade drService = ServicesInjector.INSTANCE.getDiskResourceServiceFacade();
    private final DataLinkFactory dlFactory = GWT.create(DataLinkFactory.class);

    public DataLinkPresenter(List<M> resources) {
        view = new DataLinkPanel<M>(resources);
        view.setPresenter(this);

        // Remove Folders
        List<M> allowedResources = Lists.newArrayList();
        for(M m : resources){
            if(!(m instanceof Folder)){
                allowedResources.add(m);
            }
        }
        // Retrieve tickets for root nodes
        getExistingDataLinks(allowedResources);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void getExistingDataLinks(List<M> resources) {
        view.addRoots(resources);
        drService.listDataLinks(DiskResourceUtil.asStringIdList(resources), new ListDataLinksCallback(
                view.getTree(),dlFactory));
    }



    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void deleteDataLink(DataLink value) {
        drService.deleteDataLinks(Lists.newArrayList(value.getId()),
                new DeleteDataLinksCallback(view));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void deleteDataLinks(List<DataLink> dataLinks){
        List<String> dataLinkIds = Lists.newArrayList();
        for (DataLink dl : dataLinks) {
            dataLinkIds.add(dl.getId());
        }
        view.mask();
        drService.deleteDataLinks(dataLinkIds, new DeleteDataLinksCallback(view));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void createDataLinks(List<M> selectedItems) {
        final List<String> drResourceIds = Lists.newArrayList();
        for(M dr : selectedItems){
            if(!(dr instanceof DataLink)){
                drResourceIds.add(dr.getId());
            }
        }

        view.mask();
        drService.createDataLinks(drResourceIds, new CreateDataLinkCallback(dlFactory, view));
    }

    @Override
    public String getSelectedDataLinkDownloadPage() {
        M model = view.getTree().getSelectionModel().getSelectedItem();
        if(model instanceof DataLink){
            return ((DataLink)model).getDownloadPageUrl();
        }
        return null;
    }

    @Override
    public String getSelectedDataLinkDownloadUrl() {
        M model = view.getTree().getSelectionModel().getSelectedItem();
        if (model instanceof DataLink) {
            return ((DataLink)model).getDownloadUrl();
        }
        return null;
    }

    @Override
    public void openSelectedDataLinkDownloadPage() {
        M model = view.getTree().getSelectionModel().getSelectedItem();
        if (model instanceof DataLink) {
            String url = ((DataLink)model).getDownloadPageUrl();
            WindowUtil.open(url);
        }
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
    }
}
