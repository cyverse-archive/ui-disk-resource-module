package org.iplantc.core.uidiskresource.client.gin;

import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeAppearance;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;

import org.iplantc.core.resources.client.IplantResources;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;

public class DiskResourceTreeProvider implements Provider<Tree<Folder, Folder>> {

    final class CustomTreeStyle extends TreeStyle {

        private final TreeAppearance appearance;

        public CustomTreeStyle(final TreeAppearance appearance) {
            this.appearance = appearance;
        }

        @Override
        public ImageResource getLeafIcon() {
            return appearance.closeNodeIcon();
        }

    }

    private final TreeStore<Folder> treeStore;

    @Inject
    public DiskResourceTreeProvider(TreeStore<Folder> treeStore) {
        this.treeStore = treeStore;
    }

    @Override
    public Tree<Folder, Folder> get() {

        final Tree<Folder, Folder> tree = new Tree<Folder, Folder>(treeStore, new IdentityValueProvider<Folder>()) {

            @Override
            protected ImageResource calculateIconStyle(Folder model) {
                if (model instanceof DiskResourceQueryTemplate) {
                    // Set magic folder icon
                    return IplantResources.RESOURCES.folderView();
                }
                return super.calculateIconStyle(model);
            }

        };

        tree.setCell(new TreeCell());
        tree.setStyle(new CustomTreeStyle(tree.getAppearance()));
        return tree;
    }

}
