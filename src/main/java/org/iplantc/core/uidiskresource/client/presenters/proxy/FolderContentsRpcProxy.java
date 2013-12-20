package org.iplantc.core.uidiskresource.client.presenters.proxy;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;

import java.util.List;

/**
 * This proxy is responsible for retrieving directory listings and search requests from the server.
 *
 */
public class FolderContentsRpcProxy extends RpcProxy<FolderContentsLoadConfig, PagingLoadResult<DiskResource>> {

    final class FolderContentsCallback implements AsyncCallback<Folder> {


        private final FolderContentsLoadConfig loadConfig;
        private final AsyncCallback<PagingLoadResult<DiskResource>> callback;

        private FolderContentsCallback(FolderContentsLoadConfig loadConfig,
                AsyncCallback<PagingLoadResult<DiskResource>> callback) {
            this.loadConfig = loadConfig;
            this.callback = callback;
        }

        @Override
        public void onSuccess(Folder result) {
            if (callback == null || result == null) {
                onFailure(null);
                return;
            }
            List<DiskResource> list = Lists.newArrayList(Iterables.concat(result.getFolders(), result.getFiles()));
            loadConfig.getFolder().setTotalFiltered(result.getTotalFiltered());
            callback.onSuccess(new PagingLoadResultBean<DiskResource>(list, result.getTotal(), loadConfig.getOffset()));

        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
        }

        public FolderContentsLoadConfig getLoadConfig() {
            return loadConfig;
        }

        public AsyncCallback<PagingLoadResult<DiskResource>> getCallback() {
            return callback;
        }

    }

    private final DiskResourceServiceFacade drService;
    private SearchServiceFacade searchService;

    @Inject
    public FolderContentsRpcProxy(final DiskResourceServiceFacade drService, final SearchServiceFacade searchService) {
        this.drService = drService;
        this.searchService = searchService;
    }
    
    @Override
    public void load(final FolderContentsLoadConfig loadConfig,
                     final AsyncCallback<PagingLoadResult<DiskResource>> callback) {
        Folder folder = loadConfig.getFolder();
        if (folder.isFilter()) {
            if (callback != null) {
                List<DiskResource> emptyResult = Lists.newArrayList();
                callback.onSuccess(new PagingLoadResultBean<DiskResource>(emptyResult, 0, 0));
            }

            return;
        }

        drService.getFolderContents(folder, loadConfig, new FolderContentsCallback(loadConfig, callback));
    }
}
