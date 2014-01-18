package org.iplantc.core.uidiskresource.client.gin;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
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

    /*final class TreeValueProvider implements ValueProvider<Folder, Folder> {
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
    }*/

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

    final class TreeCell extends AbstractCell<Folder> {
        @Override
        public void render(Cell.Context context, Folder value, SafeHtmlBuilder sb) {
            if (value instanceof DiskResourceQueryTemplate) {
                if (!((DiskResourceQueryTemplate)value).isSaved()) {
                    // TODO handle special rendering of non-saved query templates

                }
                if (((DiskResourceQueryTemplate)value).isDirty()) {
                    // FIXME JDS This needs to be abstracted into an appearance
                    sb.append(SafeHtmlUtils.fromString("* " + value.getName()));

                } else {
                    sb.append(SafeHtmlUtils.fromString(value.getName()));

                }
            } else {
                sb.append(SafeHtmlUtils.fromString(value.getName()));
            }

        }

        @Override
        public void onBrowserEvent(Context context, Element parent, Folder value, NativeEvent event, ValueUpdater<Folder> valueUpdater) {
            // TODO Auto-generated method stub
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
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
        /*
         * JDS May have to change the cell data type to folder. If that is the case, then we can pass an
         * IdentityValueProvider to the Tree constructor
         */
        tree.setCell(new TreeCell());
        tree.setStyle(new CustomTreeStyle(tree.getAppearance()));
        return tree;
    }

}
