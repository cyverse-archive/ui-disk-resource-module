package org.iplantc.core.uidiskresource.client.presenters.proxy;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import org.iplantc.core.uicommons.client.info.ErrorAnnouncementConfig;
import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;

import java.util.List;

/**
 * This proxy is responsible for retrieving directory listings and search requests from the server.
 *
 */
public class FolderContentsRpcProxy extends RpcProxy<FolderContentsLoadConfig, PagingLoadResult<DiskResource>> {

	/**
	 * Constructs a valid {@link PagingLoadResultBean} from the given {@link Folder} result.
	 * 
	 * @author jstroot
	 *
	 */
	class FolderContentsCallback implements AsyncCallback<Folder> {
        private final FolderContentsLoadConfig loadConfig;
        private final AsyncCallback<PagingLoadResult<DiskResource>> callback;
        private final IplantAnnouncer announcer;

        private FolderContentsCallback(IplantAnnouncer announcer, FolderContentsLoadConfig loadConfig,
                AsyncCallback<PagingLoadResult<DiskResource>> callback) {
            this.announcer = announcer;
            this.loadConfig = loadConfig;
            this.callback = callback;
        }

        @Override
        public void onSuccess(Folder result) {
            if (callback == null || result == null) {
                onFailure(null);
                return;
            }
            // Create list of all items within the result folder
            List<DiskResource> list = Lists.newArrayList(Iterables.concat(result.getFolders(), result.getFiles()));
            // Update the loadConfig folder with the totalFiltered count.
            loadConfig.getFolder().setTotalFiltered(result.getTotalFiltered());

            callback.onSuccess(new PagingLoadResultBean<DiskResource>(list, result.getTotal(), loadConfig.getOffset()));
        }

        @Override
        public void onFailure(Throwable caught) {
            if (loadConfig.getFolder() instanceof DiskResourceQueryTemplate) {
                announcer.schedule(new ErrorAnnouncementConfig(SafeHtmlUtils.fromString("Submission of search failed"), true));
            }
            callback.onFailure(caught);
        }

        public FolderContentsLoadConfig getLoadConfig() {
            return loadConfig;
        }

        public AsyncCallback<PagingLoadResult<DiskResource>> getCallback() {
            return callback;
        }

    }

    private final DiskResourceServiceFacade drService;
    private final SearchServiceFacade searchService;
    private final IplantAnnouncer announcer;

    @Inject
    public FolderContentsRpcProxy(final DiskResourceServiceFacade drService, final SearchServiceFacade searchService, final IplantAnnouncer announcer) {
        this.drService = drService;
        this.searchService = searchService;
        this.announcer = announcer;
    }
    
    @Override
    public void load(final FolderContentsLoadConfig loadConfig,
                     final AsyncCallback<PagingLoadResult<DiskResource>> callback) {
        Folder folder = loadConfig.getFolder();
        if (folder.isFilter()) {
        	if (callback != null) {
        		List<DiskResource> emptyResult = Lists.newArrayList();
        		callback.onSuccess(new PagingLoadResultBean<DiskResource>(
        				emptyResult, 0, 0));
        	}
        	return;
        } else if(folder instanceof DiskResourceQueryTemplate){
        	
            searchService.submitSearchFromQueryTemplate((DiskResourceQueryTemplate)folder, loadConfig, new FolderContentsCallback(announcer, loadConfig, callback));
        } else {

            drService.getFolderContents(folder, loadConfig, new FolderContentsCallback(announcer, loadConfig, callback));
        }

    }
}
