package org.iplantc.de.diskResource.client.metadata.presenter;

import java.util.List;
import java.util.Set;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.gin.ServicesInjector;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceAutoBeanFactory;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceMetadata;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceMetadataList;
import org.iplantc.core.uicommons.client.models.diskresources.MetadataTemplate;
import org.iplantc.core.uicommons.client.models.diskresources.MetadataTemplateAttribute;
import org.iplantc.core.uicommons.client.models.diskresources.MetadataTemplateInfo;
import org.iplantc.core.uicommons.client.models.diskresources.MetadataTemplateInfoList;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;
import org.iplantc.de.diskResource.client.metadata.view.DiskResourceMetadataView;
import org.iplantc.de.diskResource.client.metadata.view.DiskResourceMetadataView.Presenter;
import org.iplantc.de.diskResource.client.services.callbacks.DiskResourceMetadataUpdateCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class MetadataPresenter implements Presenter {


	private DiskResource resource;
	private DiskResourceMetadataView view;
    private final DiskResourceServiceFacade drService = ServicesInjector.INSTANCE.getDiskResourceServiceFacade();
    private final DiskResourceAutoBeanFactory autoBeanFactory = GWT
            .create(DiskResourceAutoBeanFactory.class);

	public MetadataPresenter(DiskResource selected, DiskResourceMetadataView view) {
		this.resource = selected;
		this.view = view;
		view.setPresenter(this);
		getDiskResourceMetadata(new RetrieveMetadataCallback());
		getTemplates();
	}

	@Override
	public void go(HasOneWidget container) {
		container.setWidget(view.asWidget());
	}

    @Override
    public void getDiskResourceMetadata(AsyncCallback<String> callback) {
    	drService.getDiskResourceMetaData(resource, callback);
    }

    @Override
    public void setDiskResourceMetaData(Set<DiskResourceMetadata> metadataToAdd,
            Set<DiskResourceMetadata> metadataToDelete,
            DiskResourceMetadataUpdateCallback diskResourceMetadataUpdateCallback) {
    	drService.setDiskResourceMetaData(resource, metadataToAdd, metadataToDelete,
                diskResourceMetadataUpdateCallback);
    }

    @Override
    public void getTemplates() {
    	drService.getMetadataTemplateListing(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable arg0) {
				ErrorHandler.post("Unable to retrieve templates!", arg0);

			}

			@Override
			public void onSuccess(String result) {
				 AutoBean<MetadataTemplateInfoList> bean = AutoBeanCodex.decode(autoBeanFactory,
						 MetadataTemplateInfoList.class, result);
				 List<MetadataTemplateInfo> templates = bean.as().getTemplates();
				 view.populateTemplates(templates);

			}
		});
    }

    @Override
    public void onTemplateSelected(String templateId) {
    	drService.getMetadataTemplate(templateId, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable arg0) {
				ErrorHandler.post("Unable to retrieve template attributes!", arg0);

			}

			@Override
			public void onSuccess(String result) {
				AutoBean<MetadataTemplate> bean = AutoBeanCodex.decode(autoBeanFactory, MetadataTemplate.class, result);
				MetadataTemplate template = bean.as();
				List<MetadataTemplateAttribute> attributesList = template.getAttributes();
				view.loadTemplateAttributes(attributesList);
			}
		});

    }

    private final class RetrieveMetadataCallback implements AsyncCallback<String> {

        public RetrieveMetadataCallback() {

        }

        @Override
        public void onSuccess(String result) {
            AutoBean<DiskResourceMetadataList> bean = AutoBeanCodex.decode(autoBeanFactory,
                    DiskResourceMetadataList.class, result);
            view.loadMetadata(bean.as());
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(caught);
        }
    }

	@Override
	public DiskResource getSelectedResource() {
		return resource;
	}





}
