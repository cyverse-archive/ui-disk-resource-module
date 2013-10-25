package org.iplantc.core.uidiskresource.client.presenters.proxy;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;

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
    public void load(final FolderContentsLoadConfig loadConfig, final AsyncCallback<PagingLoadResult<DiskResource>> callback) {
        List<SortInfoBean> sortInfos = loadConfig.getSortInfo();
        if(sortInfos!= null && sortInfos.size() == 1) {
            drService.getFolderContents(loadConfig.getFolder().getPath(),loadConfig.getLimit(),loadConfig.getOffset(),sortInfos.get(0).getSortField(),loadConfig.getSortInfo().get(0).getSortDir().toString(), new FolderContentsCallback(loadConfig, callback));
        } else {
            drService.getFolderContents(loadConfig.getFolder().getPath(),loadConfig.getLimit(),loadConfig.getOffset(),"NAME","ASC", new FolderContentsCallback(loadConfig, callback));

        }
            
    }
}
