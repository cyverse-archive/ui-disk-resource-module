package org.iplantc.core.uidiskresource.client.presenters.proxy;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uidiskresource.client.Services;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.HasHandlerRegistrationMgmt;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;

/**
 * A <code>LoadHandler</code> which is used to lazily load, expand, and select a desired folder.
 * 
 * 
 * @author jstroot
 * 
 */
public final class SelectFolderByIdLoadHandler implements LoadHandler<Folder, List<Folder>> {

    private final Stack<String> pathsToLoad = new Stack<String>();
    private final LinkedList<String> path;
    private boolean folderExists = false;
    private boolean folderVerifyCalled = false;

    private final HasId folderToSelect;

    private final DiskResourceView view;
    private final DiskResourceView.Presenter presenter;
    private final HasHandlerRegistrationMgmt regMgr;

    public SelectFolderByIdLoadHandler(final HasId folderToSelect, final DiskResourceView.Presenter presenter) {
        presenter.maskView();
        this.folderToSelect = folderToSelect;
        this.presenter = presenter;
        this.regMgr = presenter;
        this.view = presenter.getView();
        // Split the string on "/"
        path = Lists.newLinkedList(Splitter.on("/").trimResults().omitEmptyStrings().split(folderToSelect.getId()));
    }

    @Override
    public void onLoad(LoadEvent<Folder, List<Folder>> event) {
        // If we haven't verified that the requested folder exists on the server, then do so.
        if (!folderVerifyCalled) {
            verifyFolderExists();
            return;
        } else if (folderVerifyCalled && !folderExists) {
            GWT.log("Requested folder could not be found on the server!!" + folderToSelect.getId());
            unmaskView();
            return;
        }

        // Exit condition
        if (folderExists && pathsToLoad.isEmpty()) {
            view.setSelectedFolder(event.getLoadConfig());
            unmaskView();
            return;
        } else if (folderExists && !pathsToLoad.isEmpty()) {
            path.add(pathsToLoad.pop());
            Folder folder = view.getFolderById("/".concat(Joiner.on("/").join(path)));
            if (folder != null) {
                // Trigger remote load by expanding folder
                view.expandFolder(folder);
            } else {
                GWT.log("Folder not found in view!!");
            }
        }
    }

    /**
     * Verify if the desired selected folder exists
     * This only needs to occur once.
     */
    private void verifyFolderExists() {
        folderVerifyCalled = true;
        Services.DISK_RESOURCE_SERVICE.getFolderContents(folderToSelect.getId(), false, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                folderExists = true;

                Folder folder = view.getFolderById(folderToSelect.getId());
                // Find the paths which are not yet loaded, and push them onto the 'pathsToLoad' stack
                while ((folder == null) && !path.isEmpty()) {
                    pathsToLoad.push(path.removeLast());
                    folder = view.getFolderById("/".concat(Joiner.on("/").join(path)));
                }

                if (folder != null) {
                    if (view.isLoaded(folder)) {
                        if (!presenter.getSelectedFolder().equals(folder)) {
                            view.setSelectedFolder(folder);
                        }
                        unmaskView();
                    } else {
                        // Once a valid folder is found in the view, remotely load the
                        // folder, which will add the next folder in the path to the view's treeStore.
                        view.expandFolder(folder);
                    }

                }
                // If no folders could be found in view
                if (path.isEmpty()) {
                    GWT.log("NO ROOT FOLDERS FOUND");
                    unmaskView();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                // If the folder does not exist, inform user..... or something
                folderExists = false;
                GWT.log("Time to unregister");
                unmaskView();
            }
        });
    }

    private void unmaskView() {
        regMgr.unregisterHandler(this);
        presenter.unMaskView();
    }
}