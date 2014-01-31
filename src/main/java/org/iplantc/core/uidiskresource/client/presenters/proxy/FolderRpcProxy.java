package org.iplantc.core.uidiskresource.client.presenters.proxy;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.sencha.gxt.data.client.loader.RpcProxy;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.info.ErrorAnnouncementConfig;
import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.models.diskresources.RootFolders;
import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;
import org.iplantc.core.uicommons.client.views.IsMaskable;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler;
import org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

import java.util.Collections;
import java.util.List;

public class FolderRpcProxy extends RpcProxy<Folder, List<Folder>> implements DiskResourceView.Proxy {

    class SubFoldersCallback implements AsyncCallback<List<Folder>> {
        final AsyncCallback<List<Folder>> callback;

        public SubFoldersCallback(AsyncCallback<List<Folder>> callback) {
            this.callback = callback;
        }

        @Override
        public void onSuccess(List<Folder> result) {
            if (callback != null) {
                callback.onSuccess(result);
            }
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
            if (callback != null) {
                callback.onFailure(caught);
            }
        }
    }

    class GetSavedQueryTemplatesCallback implements AsyncCallback<List<DiskResourceQueryTemplate>> {
        final IplantAnnouncer ipAnnouncer2;
        final DataSearchPresenter searchPresenter2;

        public GetSavedQueryTemplatesCallback(DataSearchPresenter searchPresenter1, IplantAnnouncer ipAnnouncer) {
            this.searchPresenter2 = searchPresenter1;
            this.ipAnnouncer2 = ipAnnouncer;
        }

        @Override
        public void onFailure(Throwable caught) {
            ipAnnouncer2.schedule(new ErrorAnnouncementConfig(SafeHtmlUtils.fromString("Failed to retrieve saved filters"), true));
        }

        @Override
        public void onSuccess(List<DiskResourceQueryTemplate> result) {
            // Save result
            searchPresenter2.loadSavedQueries(result);
        }
    }

    class RootFolderCallback implements AsyncCallback<RootFolders> {

        final AsyncCallback<List<Folder>> callback;
        final SearchServiceFacade searchSvc;
        final IplantAnnouncer ipAnnouncer;
        final DataSearchPresenter searchPresenter1;
        final IsMaskable maskable;

        public RootFolderCallback(final SearchServiceFacade searchService, final DataSearchPresenter searchPresenter, final AsyncCallback<List<Folder>> callback, final IplantAnnouncer announcer,
                final IsMaskable isMaskable) {
            this.searchSvc = searchService;
            this.searchPresenter1 = searchPresenter;
            this.callback = callback;
            this.ipAnnouncer = announcer;
            this.maskable = isMaskable;
        }

        @Override
        public void onSuccess(final RootFolders rootFolders) {
            if (callback != null) {
                List<Folder> roots = rootFolders.getRoots();
                callback.onSuccess(roots);
            }
            // Retrieve any saved searches.
            searchSvc.getSavedQueryTemplates(new GetSavedQueryTemplatesCallback(searchPresenter1, ipAnnouncer));
            maskable.unmask();
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);

            if (callback != null) {
                callback.onFailure(caught);
            }
            maskable.unmask();
        }
    }

    private final DiskResourceServiceFacade drService;
    private final SearchServiceFacade searchService;
    private final IplantAnnouncer announcer;
    private DataSearchPresenter searchPresenter;
    private IsMaskable isMaskable;
    private final HandlerManager handlerManager;

    @Inject
    public FolderRpcProxy(final DiskResourceServiceFacade drService, final SearchServiceFacade searchService, final IplantAnnouncer announcer) {
        this.drService = drService;
        this.searchService = searchService;
        this.announcer = announcer;
        handlerManager = new HandlerManager(this);
    }

    @Override
    public void load(final Folder parentFolder, final AsyncCallback<List<Folder>> callback) {
        if (parentFolder == null) {
            // Performing initial load of root folders and saved searches
            isMaskable.mask("");
            drService.getRootFolders(new RootFolderCallback(searchService, searchPresenter, callback, announcer, isMaskable));
        } else if (parentFolder.isFilter()) {
            if (callback != null) {
                callback.onSuccess(Collections.<Folder> emptyList());
            }
            return;

        } else if (parentFolder instanceof DiskResourceQueryTemplate) {
            fireEvent(new SubmitDiskResourceQueryEvent((DiskResourceQueryTemplate)parentFolder));
        } else {
            drService.getSubFolders(parentFolder, new SubFoldersCallback(callback));
        }
    }

    @Override
    public void init(final DataSearchPresenter presenter, final IsMaskable isMaskable) {
        this.searchPresenter = presenter;
        this.isMaskable = isMaskable;
        addSubmitDiskResourceQueryEventHandler(searchPresenter);
    }

    void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }

    @Override
    public HandlerRegistration addSubmitDiskResourceQueryEventHandler(SubmitDiskResourceQueryEventHandler handler) {
        return handlerManager.addHandler(SubmitDiskResourceQueryEvent.TYPE, handler);
    }

}
