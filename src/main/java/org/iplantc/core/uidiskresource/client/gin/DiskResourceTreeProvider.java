package org.iplantc.core.uidiskresource.client.gin;

import org.iplantc.core.uidiskresource.client.models.Folder;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class DiskResourceTreeProvider implements Provider<Tree<Folder, String>> {

    private final TreeStore<Folder> treeStore;

    @Inject
    public DiskResourceTreeProvider(TreeStore<Folder> treeStore) {
        this.treeStore = treeStore;
    }

    @Override
    public Tree<Folder, String> get() {
        return new Tree<Folder, String>(treeStore, new ValueProvider<Folder, String>() {

            @Override
            public String getValue(Folder object) {
                return object.getName();
            }

            @Override
            public void setValue(Folder object, String value) {}

            @Override
            public String getPath() {
                return null;
            }
        });
    }

}
