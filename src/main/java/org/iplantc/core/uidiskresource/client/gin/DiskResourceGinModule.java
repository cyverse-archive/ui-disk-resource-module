package org.iplantc.core.uidiskresource.client.gin;

import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.presenters.DiskResourcePresenterImpl;
import org.iplantc.core.uidiskresource.client.presenters.proxy.FolderRpcProxy;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.DiskResourceViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class DiskResourceGinModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<TreeStore<Folder>>() {}).toProvider(DiskResourceTreeStoreProvider.class)
                .in(Singleton.class);
        bind(new TypeLiteral<Tree<Folder, String>>() {}).toProvider(DiskResourceTreeProvider.class).in(
                Singleton.class);
        bind(DiskResourceView.class).to(DiskResourceViewImpl.class);
        bind(DiskResourceView.Presenter.class).to(DiskResourcePresenterImpl.class);
        bind(DiskResourceView.Proxy.class).to(FolderRpcProxy.class);
    }

}
