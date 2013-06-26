package org.iplantc.core.uidiskresource.client.services.callbacks;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceAutoBeanFactory;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceInfo;
import org.iplantc.core.uidiskresource.client.services.errors.DiskResourceErrorAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.services.errors.ErrorGetManifest;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public final class GetDiskResourceDetailsCallback implements AsyncCallback<String> {
    private final DiskResourceView.Presenter presenter;
    private final String path;
    private final DiskResourceAutoBeanFactory factory;

    public GetDiskResourceDetailsCallback(DiskResourceView.Presenter presenter, String path, DiskResourceAutoBeanFactory factory) {
        this.presenter = presenter;
        this.path = path;
        this.factory = factory;
        presenter.getView().maskDetailsPanel();
    }

    @Override
    public void onFailure(Throwable caught) {
        DiskResourceErrorAutoBeanFactory errFactory = GWT.create(DiskResourceErrorAutoBeanFactory.class);
        String errMessage = caught.getMessage();
        presenter.getView().unmaskDetailsPanel();
        if (JsonUtils.safeToEval(errMessage)) {
            AutoBean<ErrorGetManifest> errorBean = AutoBeanCodex.decode(errFactory,
                    ErrorGetManifest.class, errMessage);

            ErrorHandler.post(errorBean.as(), caught);
        } else {
            ErrorHandler.post(I18N.ERROR.retrieveStatFailed(), caught);
        }
    }

    @Override
    public void onSuccess(String result) {
        JSONObject json = JsonUtil.getObject(result);
        JSONObject pathsObj = JsonUtil.getObject(json, "paths");
        JSONObject details = JsonUtil.getObject(pathsObj, path);
        AutoBean<DiskResourceInfo> bean = AutoBeanCodex.decode(factory, DiskResourceInfo.class,
                details.toString());
        presenter.getView().updateDetails(path, bean.as());
        presenter.getView().unmaskDetailsPanel();
    }
}