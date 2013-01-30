package org.iplantc.core.uidiskresource.client.views;

import java.util.Collection;
import java.util.Set;

import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView.Presenter;

import com.google.common.collect.Sets;
import com.google.gwt.dom.client.Element;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent.DndDragEnterHandler;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent.DndDragMoveHandler;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;

class DiskResourceViewDnDHandler implements DndDragStartHandler, DndDropHandler, DndDragMoveHandler,
        DndDragEnterHandler {

    private final Presenter presenter;

    public DiskResourceViewDnDHandler(final DiskResourceView.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDragStart(DndDragStartEvent event) {
        // Just load up data from selection
        Set<? extends DiskResource> newSet = null;
        if (presenter.isViewGrid(event.getTarget()) && !presenter.getSelectedDiskResources().isEmpty()) {
            newSet = Sets.newHashSet(presenter.getSelectedDiskResources());
        } else if (presenter.isViewTree(event.getTarget()) && (presenter.getSelectedFolder() != null)) {
            newSet = Sets.newHashSet(presenter.getSelectedFolder());
        }

        if ((newSet != null) 
                && !newSet.isEmpty()) {
            event.setData(newSet);
            event.getStatusProxy().update(I18N.DISPLAY.dataDragDropStatusText(newSet.size()));
            event.getStatusProxy().setStatus(true);
            event.setCancelled(false);
        } else {
            event.setCancelled(true);
        }
    }

    @Override
    public void onDragEnter(DndDragEnterEvent event) {
        Set<DiskResource> dropData = getDropData(event.getDragSource().getData());
        // Verify that drag data is a list of disk resources
        if (dropData == null) {
            event.setCancelled(true);
            return;
        }
        // Reset status message
        event.getStatusProxy().update(I18N.DISPLAY.dataDragDropStatusText(dropData.size()));
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
            } else if (!selectedFolder.getPermissions().isWritable()
                    || !DiskResourceUtil.isMovable(dropData)) {
                // Check for permissions
                event.getStatusProxy().setStatus(false);
                event.getStatusProxy().update(I18N.ERROR.permissionErrorMessage());
                event.setCancelled(true);

            } else if (!presenter.canDragDataToTargetFolder(selectedFolder, dropData)) {
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

        if (dropData == null) {
            event.setCancelled(true);
            return;
        }

        event.getStatusProxy().setStatus(true);
        event.getStatusProxy().update(I18N.DISPLAY.dataDragDropStatusText(dropData.size()));

        Folder targetFolder = presenter.getDropTargetFolder(event.getDropTarget().getWidget(), event
                .getDragMoveEvent().getNativeEvent().getEventTarget().<Element> cast());

        // Permissions of drop data should have been checked on drag enter
        // if the target is not a folder, don't allow a drop there
        if (targetFolder == null) {
            event.getStatusProxy().update("NULL FOLDER");
            event.getStatusProxy().setStatus(false);
            event.setCancelled(true);
        } else if (!(targetFolder instanceof Folder)) {
            event.getStatusProxy().setStatus(false);
        } else if (!targetFolder.getPermissions().isWritable() 
                || !DiskResourceUtil.isMovable(dropData)) {
            // Check for permissions
            event.getStatusProxy().setStatus(false);
            event.getStatusProxy().update(I18N.ERROR.permissionErrorMessage());
            event.setCancelled(true);
        } else if (!presenter.canDragDataToTargetFolder(targetFolder, dropData)) {
            event.getStatusProxy().setStatus(false);
            event.getStatusProxy().update(I18N.ERROR.resourcesContainAncestors());
            event.setCancelled(true);
        } else if (presenter.isViewTree(event.getTarget())) {
            // If it is the view's tree,
        }

    }

    @Override
    public void onDrop(DndDropEvent event) {
        // Reset status message
        Set<DiskResource> dropData = getDropData(event.getData());
        event.getStatusProxy().update(I18N.DISPLAY.dataDragDropStatusText(dropData.size()));

        Folder targetFolder = presenter.getDropTargetFolder(event.getDropTarget().getWidget(), event
                .getDragEndEvent().getNativeEvent().getEventTarget().<Element> cast());

        presenter.doMoveDiskResources(targetFolder, dropData);
    }

    @SuppressWarnings("unchecked")
    private Set<DiskResource> getDropData(Object data) {
        if (!((data instanceof Collection<?>) 
                && !((Collection<?>)data).isEmpty() 
                && ((Collection<?>)data).iterator().next() instanceof DiskResource)) {
            return null;
        }
        Set<DiskResource> dropData = null;
        dropData = Sets.newHashSet((Collection<DiskResource>)data);

        return dropData;
    }
}
