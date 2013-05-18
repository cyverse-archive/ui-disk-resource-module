package org.iplantc.core.uidiskresource.client.presenters;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uidiskresource.client.models.DiskResourceAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.models.DiskResourceInfo;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

final class GetDiskResourceDetailsCallback implements AsyncCallback<String> {
    private final DiskResourceView.Presenter presenter;
    private final String path;
    private DiskResourceAutoBeanFactory factory;

    GetDiskResourceDetailsCallback(DiskResourceView.Presenter presenter, String path, DiskResourceAutoBeanFactory factory) {
        this.presenter = presenter;
        this.path = path;
        this.factory = factory;
    }

    @Override
    public void onFailure(Throwable caught) {
        ErrorHandler.post(I18N.ERROR.retrieveStatFailed(), caught);
    }

    @Override
    public void onSuccess(String result) {
        JSONObject json = JsonUtil.getObject(result);
        JSONObject pathsObj = JsonUtil.getObject(json, "paths");
        JSONObject details = JsonUtil.getObject(pathsObj, path);
        AutoBean<DiskResourceInfo> bean = AutoBeanCodex.decode(factory, DiskResourceInfo.class,
                details.toString());
        presenter.getView().updateDetails(path, bean.as());
    }
}