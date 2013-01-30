package org.iplantc.core.uidiskresource.client.gin;

import org.iplantc.core.uidiskresource.client.models.DiskResourceModelKeyProvider;
import org.iplantc.core.uidiskresource.client.models.Folder;

import com.google.inject.Provider;
import com.sencha.gxt.data.shared.TreeStore;

public class DiskResourceTreeStoreProvider implements Provider<TreeStore<Folder>> {

    @Override
    public TreeStore<Folder> get() {
        return new TreeStore<Folder>(new DiskResourceModelKeyProvider());
    }

}
