package org.iplantc.core.uidiskresource.client.presenters.proxy;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.models.diskresources.RootFolders;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.data.client.loader.RpcProxy;

public class FolderRpcProxy extends RpcProxy<Folder, List<Folder>> implements DiskResourceView.Proxy {

    private DiskResourceView.Presenter presenter;
    private final DiskResourceServiceFacade drService;

    @Inject
    public FolderRpcProxy(DiskResourceServiceFacade drService) {
        this.drService = drService;
    }

    @Override
    public void load(final Folder parentFolder, final AsyncCallback<List<Folder>> callback) {

        if (parentFolder == null) {
            presenter.maskView();
            drService.getRootFolders(new AsyncCallback<RootFolders>() {

                @Override
                public void onSuccess(final RootFolders rootFolders) {
                    List<Folder> roots = rootFolders.getRoots();
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
            drService.getFolderContents(parentFolder.getId(), false,
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
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
                            if (callback != null) {
                                callback.onFailure(caught);
                            }
                        }
                    });
        }
    }

    @Override
    public void setPresenter(DiskResourceView.Presenter presenter) {
        this.presenter = presenter;
    }

}
