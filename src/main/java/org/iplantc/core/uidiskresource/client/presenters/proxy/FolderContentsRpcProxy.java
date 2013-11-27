package org.iplantc.core.uidiskresource.client.presenters.proxy;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

public class FolderContentsRpcProxy extends RpcProxy<FolderContentsLoadConfig, PagingLoadResult<DiskResource>> {

    private final class FolderContentsCallback implements AsyncCallback<Folder> {
        private final FolderContentsLoadConfig loadConfig;
        private final AsyncCallback<PagingLoadResult<DiskResource>> callback;

        private FolderContentsCallback(FolderContentsLoadConfig loadConfig,
                AsyncCallback<PagingLoadResult<DiskResource>> callback) {
            this.loadConfig = loadConfig;
            this.callback = callback;
        }

        @Override
        public void onSuccess(Folder result) {
          if(callback != null && result != null) {
              List<DiskResource> list = new ArrayList<DiskResource>();
              list.addAll(result.getFolders());
              list.addAll(result.getFiles());
                loadConfig.getFolder().setTotalFiltered(result.getTotalFiltered());
              callback.onSuccess(new PagingLoadResultBean<DiskResource>(list, result.getTotal(), loadConfig.getOffset()));
          } else {
              onFailure(null);
          }
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
        }
    }

    private final DiskResourceServiceFacade drService;
    
    public FolderContentsRpcProxy(DiskResourceServiceFacade drService) {
        this.drService = drService;
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

        String sortField = "NAME"; //$NON-NLS-1$
        String sortDir = "ASC"; //$NON-NLS-1$

        List<SortInfoBean> sortInfos = loadConfig.getSortInfo();
        if (sortInfos != null && sortInfos.size() == 1) {
            SortInfoBean sortInfo = sortInfos.get(0);
            sortField = sortInfo.getSortField();
            sortDir = sortInfo.getSortDir().toString();
        }

        drService.getFolderContents(folder.getPath(), loadConfig.getLimit(), loadConfig.getOffset(),
                sortField, sortDir, new FolderContentsCallback(loadConfig, callback));
    }
}
