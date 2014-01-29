package org.iplantc.core.uidiskresource.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;

import org.iplantc.core.uicommons.client.gin.ServicesInjector;
import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uicommons.client.services.SearchServiceFacade;
import org.iplantc.core.uidiskresource.client.presenters.DiskResourcePresenterImpl;
import org.iplantc.core.uidiskresource.client.presenters.proxy.FolderContentsRpcProxy;
import org.iplantc.core.uidiskresource.client.presenters.proxy.FolderRpcProxy;
import org.iplantc.core.uidiskresource.client.search.presenter.DataSearchPresenter;
import org.iplantc.core.uidiskresource.client.search.presenter.impl.DataSearchPresenterImpl;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.DiskResourceViewImpl;

public class DiskResourceGinModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<TreeStore<Folder>>() {}).toProvider(DiskResourceTreeStoreProvider.class);
        bind(new TypeLiteral<Tree<Folder, Folder>>() {}).toProvider(DiskResourceTreeProvider.class);
        bind(DiskResourceView.class).to(DiskResourceViewImpl.class);
        bind(DiskResourceView.Presenter.class).to(DiskResourcePresenterImpl.class);
        bind(DiskResourceView.Proxy.class).to(FolderRpcProxy.class);
        bind(FolderContentsRpcProxy.class);
        bind(DiskResourceServiceFacade.class).toProvider(DiskResourceServiceFacadeProvider.class);

        bind(DataSearchPresenter.class).to(DataSearchPresenterImpl.class);
    }

    @Provides
    @Singleton
    public SearchServiceFacade createSearchServiceFacade() {
        return ServicesInjector.INSTANCE.getSearchServiceFacade();
    }

    @Provides
    @Singleton
    public IplantAnnouncer createIplantAnnouncer() {
        return IplantAnnouncer.getInstance();
    }
}
