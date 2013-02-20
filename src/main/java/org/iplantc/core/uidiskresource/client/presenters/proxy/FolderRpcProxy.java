package org.iplantc.core.uidiskresource.client.presenters.proxy;

import java.util.List;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.Services;
import org.iplantc.core.uidiskresource.client.models.DiskResourceAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.models.RootFolders;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.data.client.loader.RpcProxy;

public class FolderRpcProxy extends RpcProxy<Folder, List<Folder>> implements DiskResourceView.Proxy {

    private final DiskResourceAutoBeanFactory factory = GWT.create(DiskResourceAutoBeanFactory.class);
    private DiskResourceView.Presenter presenter;

    @Override
    public void load(final Folder parentFolder, final AsyncCallback<List<Folder>> callback) {

        presenter.maskView();
        if (parentFolder == null) {
            Services.DISK_RESOURCE_SERVICE.getHomeFolder(new AsyncCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    // Looking for "roots" key here
                    AutoBean<RootFolders> bean = AutoBeanCodex
                            .decode(factory, RootFolders.class, result);
                    List<Folder> roots = bean.as().getRoots();
                    if (callback != null) {
                        callback.onSuccess(roots);
                    }
                    presenter.unMaskView();
                }

                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);

                    if (callback != null) {
                        callback.onFailure(caught);
                    }
                    presenter.unMaskView(true);
                }

            });
        } else {
            Services.DISK_RESOURCE_SERVICE.getFolderContents(parentFolder.getId(), true,
                    new AsyncCallback<String>() {

                        @Override
                        public void onSuccess(String result) {
                            // Turn json result into a Splittable and wrap the loaded folder
                            Splittable split = StringQuoter.split(result);
                            AutoBeanCodex.decodeInto(split,
                                    AutoBeanUtils.<Folder, Folder> getAutoBean(parentFolder));

                            if (callback != null) {
                                callback.onSuccess(parentFolder.getFolders());
                            }
                            presenter.unMaskView();
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
                            if (callback != null) {
                                callback.onFailure(caught);
                            }
                            presenter.unMaskView();
                        }
                    });
        }
    }

    @Override
    public void setPresenter(DiskResourceView.Presenter presenter) {
        this.presenter = presenter;
    }

}
