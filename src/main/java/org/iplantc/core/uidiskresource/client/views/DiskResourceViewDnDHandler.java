package org.iplantc.core.uidiskresource.client.views;

import java.util.List;
import java.util.Set;

import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView.Presenter;

import com.google.common.collect.Sets;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent.DndDragEnterHandler;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent.DndDragMoveHandler;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;

class DiskResourceViewDnDHandler implements DndDragStartHandler, DndDropHandler,
        DndDragMoveHandler, DndDragEnterHandler {
    
    private final Presenter presenter;

    public DiskResourceViewDnDHandler(final DiskResourceView.Presenter presenter){
        this.presenter = presenter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDragStart(DndDragStartEvent event) {
        Set<DiskResource> dragData = getDropData(event.getData());
        /* 
         * If the drag data is not a List<DiskResource> (it is null), check to see if the drag source widget is the View's tree
         * If it is the tree, unwrap the treenodes
         */
        if((dragData == null)  
                && presenter.isViewTree(event.getTarget())) {
            List<TreeNode<Folder>> treeDragData = (List<TreeNode<Folder>>)event.getData();
            dragData = Sets.newHashSet();
            for (TreeNode<Folder> tn : treeDragData) {
                dragData.add(tn.getModel());
            }
        }
        
        if((dragData != null) 
                && !dragData.isEmpty()
                && DiskResourceUtil.isOwner(dragData)){

            event.setData(dragData);
            event.getStatusProxy().setStatus(true);
            event.getStatusProxy().update(I18N.DISPLAY.dataDragDropStatusText(dragData.size()));
        } else {
            event.setCancelled(true);
        }

    }

    @Override
    public void onDragEnter(DndDragEnterEvent event) {
        Set<DiskResource> dropData = getDropData(event.getDragSource().getData());
        // Verify that drag data is a list of disk resources
        if (dropData == null){
            event.setCancelled(true);
        }
        // Reset status message
        event.getStatusProxy().update(
                I18N.DISPLAY.dataDragDropStatusText(dropData.size()));
        // Cancel if user does not own the data
        if (!DiskResourceUtil.isOwner(dropData)) {
            event.getStatusProxy().setStatus(false);
            event.getStatusProxy().update(I18N.ERROR.permissionErrorMessage());
            event.setCancelled(true);
        } else if (presenter.isViewGrid(event.getTarget())) {// If we are entering the View's grid
            Folder selectedFolder = presenter.getSelectedFolder();
            /*
             * if there is no drop data
             * if the current selected folder is null
             * OR user does not own the selected folder
             * OR the drop data contains an ancestor folder of current selected folder.
             * THEN cancel the event and set visual status to false
             */
            if (selectedFolder == null) {
                // Check that drop data and the drop area is valid.
                event.getStatusProxy().setStatus(false);
                event.getStatusProxy().update(I18N.ERROR.noFolderSelected());
                event.setCancelled(true);
            } else if (!DiskResourceUtil.isOwner(selectedFolder)) {
                // Check for permissions
                event.getStatusProxy().setStatus(false);
                event.getStatusProxy().update(I18N.ERROR.permissionErrorMessage());
                event.setCancelled(true);

            } else if (presenter.resourcesContainAncestorsOfTargetFolder(selectedFolder, dropData)) {
                event.getStatusProxy().setStatus(false);
                event.getStatusProxy().update(I18N.ERROR.resourcesContainAncestors());
                event.setCancelled(true);
            } 
        }
    }

    @Override
    public void onDragMove(DndDragMoveEvent event) {
        // Reset status message
        Set<DiskResource> dropData = getDropData(event.getDragSource().getData());
        event.getStatusProxy().update(
                I18N.DISPLAY.dataDragDropStatusText(dropData.size()));
        
        Folder targetFolder = presenter.getDropTargetFolder(event.getDropTarget().getWidget(), event.getDragMoveEvent()
                .getNativeEvent().getEventTarget().<Element> cast());
        
        
        // Permissions of drop data should have been checked on drag enter
        // if the target is not a folder, don't allow a drop there
        if(targetFolder == null){
            event.getStatusProxy().setStatus(false);
        }else if (!(targetFolder instanceof Folder)) {
            event.getStatusProxy().setStatus(false);
        }else if(!DiskResourceUtil.isOwner(targetFolder)){
            // Check for permissions
            event.getStatusProxy().setStatus(false);
            event.getStatusProxy().update(I18N.ERROR.permissionErrorMessage());
        }else if(presenter.resourcesContainAncestorsOfTargetFolder(targetFolder, dropData)){
            event.getStatusProxy().setStatus(false);
            event.getStatusProxy().update(I18N.ERROR.resourcesContainAncestors());
            event.setCancelled(true);
        }else if(targetFolder != null && targetFolder.getName().equals("Sharing")){
            GWT.log("BREAK HERE");
        }
        
    }

    @Override
    public void onDrop(DndDropEvent event) {
        // Reset status message
        Set<DiskResource> dropData = getDropData(event.getData());
        event.getStatusProxy().update(I18N.DISPLAY.dataDragDropStatusText(dropData.size()));

        // Folder target = null;
        Folder targetFolder = presenter.getDropTargetFolder(event.getTarget(), event.getDragEndEvent().getNativeEvent()
                .getEventTarget().<Element> cast());
        
        presenter.doMoveDiskResources(targetFolder, dropData);
    }
    
    private Set<DiskResource> getDropData(Object data) {
        if (!((data instanceof List<?>) && !((List<?>)data).isEmpty() && ((List<?>)data).get(0) instanceof DiskResource)) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Set<DiskResource> dropData = Sets.newHashSet((List<DiskResource>)data);

        return dropData;
    }
}
