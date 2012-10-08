package org.iplantc.core.uidiskresource.client.presenters.proxy;

import java.util.List;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.Services;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.models.autobeans.File;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.models.autobeans.RootFolders;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.data.client.loader.RpcProxy;

public class FolderRpcProxy extends RpcProxy<Folder, List<Folder>> {

    private final DiskResourceAutoBeanFactory factory = GWT.create(DiskResourceAutoBeanFactory.class);
    @Override
    public void load(final Folder parentFolder, final AsyncCallback<List<Folder>> callback) {
        if (parentFolder == null) {
            Services.DISK_RESOURCE_SERVICE.getHomeFolder(new AsyncCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    // Looking for "roots" key here
                    AutoBean<RootFolders> bean = AutoBeanCodex
                            .decode(factory, RootFolders.class, result);
                    List<Folder> roots = bean.as().getRoots();
                    for (Folder root : roots) {
                        // getFolderContents(root);
                    }
                    callback.onSuccess(roots);
                }

                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);

                    callback.onFailure(caught);
                }

            });
        } else {
            Services.DISK_RESOURCE_SERVICE.getFolderContents(parentFolder.getId(), false,
                    new AsyncCallback<String>() {

                        @Override
                        public void onSuccess(String result) {
                            // Looking for the "folders" key here
                            Splittable split = StringQuoter.split(result);
                            AutoBean<Folder> newFolder = AutoBeanCodex.decode(factory, Folder.class,
                                    split);
                            // AutoBean<Folder> bean = AutoBeanCodex.decode(factory, Folder.class,
                            // result);
                            AutoBean<Folder> beanToWrap = AutoBeanUtils.getAutoBean(parentFolder);
                            AutoBeanCodex.decodeInto(split, beanToWrap);
                            List<Folder> folders = parentFolder.getFolders();
                            List<File> files = parentFolder.getFiles();

                            List<Folder> folders2 = newFolder.as().getFolders();
                            List<File> files2 = newFolder.as().getFiles();

                            callback.onSuccess(newFolder.as().getFolders());

                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);

                            callback.onFailure(caught);
                        }

                    });
        }
    }

    public void getFolderContents(final Folder parentFolder, final AsyncCallback<List<Folder>> callback) {
        Services.DISK_RESOURCE_SERVICE.getFolderContents(parentFolder.getId(), false,
                new AsyncCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        // Looking for the "folders" key here
                        Splittable split = StringQuoter.split(result);
                        AutoBean<Folder> newFolder = AutoBeanCodex.decode(factory, Folder.class, split);
                        // AutoBean<Folder> bean = AutoBeanCodex.decode(factory, Folder.class, result);
                        AutoBean<Folder> beanToWrap = AutoBeanUtils.getAutoBean(parentFolder);
                        AutoBeanCodex.decodeInto(split, beanToWrap);
                        List<Folder> folders = parentFolder.getFolders();
                        List<File> files = parentFolder.getFiles();

                        List<Folder> folders2 = newFolder.as().getFolders();
                        List<File> files2 = newFolder.as().getFiles();
                        // callback.onSuccess(bean.as().getFolders());
                        if (split == null) {
                            
                        }

                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);

                        // callback.onFailure(caught);
                    }

                });

    }

}
