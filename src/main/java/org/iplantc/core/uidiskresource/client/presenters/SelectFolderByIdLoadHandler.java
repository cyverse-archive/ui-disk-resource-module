package org.iplantc.core.uidiskresource.client.presenters;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.iplantc.core.uidiskresource.client.Services;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

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
final class SelectFolderByIdLoadHandler implements LoadHandler<Folder, List<Folder>> {
    private final String folderId;

    private final Stack<String> pathsToLoad = new Stack<String>();
    private final LinkedList<String> path;
    private boolean folderExists = false;
    private boolean folderVerifyCalled = false;
    private final HasHandlerRegistrationMgmt regMgr;
    private final DiskResourceView view;

    SelectFolderByIdLoadHandler(String folderId, final HasHandlerRegistrationMgmt regMgr,
            final DiskResourceView view) {
        this.folderId = folderId;
        this.regMgr = regMgr;
        this.view = view;

        // Split the string on "/"
        path = Lists.newLinkedList(Splitter.on("/").trimResults().omitEmptyStrings().split(folderId));
    }

    @Override
    public void onLoad(LoadEvent<Folder, List<Folder>> event) {
        if ((folderVerifyCalled && !folderExists)
                || (folderVerifyCalled && folderExists && pathsToLoad.isEmpty())) {
            regMgr.unregisterHandler(this);
            return;
        }

        if (folderVerifyCalled && folderExists && !pathsToLoad.isEmpty()) {

            path.add(pathsToLoad.pop());
            Folder folder = view.getFolderById("/".concat(Joiner.on("/").join(path)));
            if (folder != null) {
                // Trigger remote load by expanding folder
                view.expandFolder(folder);
                if (pathsToLoad.isEmpty()) {
                    view.setSelectedFolder(folder);
                    regMgr.unregisterHandler(this);
                }
            } else {
                GWT.log("Folder not found!!");
            }

        } else {
            verifyFolderExists();
        }
    }

    /**
     * Verify if the desired selected folder exists
     * This only needs to occur once.
     */
    private void verifyFolderExists() {
        folderVerifyCalled = true;
        Services.DISK_RESOURCE_SERVICE.getFolderContents(folderId, false, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                folderExists = true;

                Folder folder = view.getFolderById(folderId);
                // Find the paths which are not yet loaded, and push them onto the 'pathsToLoad' stack
                while ((folder == null) && !path.isEmpty()) {
                    pathsToLoad.push(path.removeLast());
                    folder = view.getFolderById("/".concat(Joiner.on("/").join(path)));
                }

                if (folder != null) {
                    // Once a valid folder is found in the view, expand it to trigger a load of that
                    // folder, which will add the next folder in the path to the view's treeStore.
                    view.expandFolder(folder);

                    if (pathsToLoad.isEmpty()) {
                        // If this is the first iteration, no paths have been pushed onto the stack, and
                        // our desired folder already exists.
                        view.setSelectedFolder(folder);
                    }

                }
                // If no folders could be found in view
                if (path.isEmpty()) {
                    GWT.log("NO ROOT FOLDERS FOUND");
                    regMgr.unregisterHandler(SelectFolderByIdLoadHandler.this);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                // If the folder does not exist, inform user..... or something
                folderExists = false;
                GWT.log("Time to unregister");
                regMgr.unregisterHandler(SelectFolderByIdLoadHandler.this);

            }
        });
    }
}