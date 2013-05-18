package org.iplantc.core.uidiskresource.client.services;


import java.util.List;

import org.iplantc.core.uidiskresource.client.models.DiskResourceAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.models.Permissions;
import org.iplantc.core.uidiskresource.client.models.RootFolders;
import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * This test class verifies the expected result of creating autobeans from the expected json
 * output of the endpoints under test.
 * 
 * @author jstroot
 * 
 */
public class GwtTestDiskResourceServiceAutoBean extends GWTTestCase {

    private JsonConstants CONSTANTS;
    private DiskResourceAutoBeanFactory factory;

    @Override
    protected void gwtSetUp() throws Exception {
        CONSTANTS = GWT.create(JsonConstants.class);
        factory = GWT.create(DiskResourceAutoBeanFactory.class);
    }
    /**
     * Verify {@link Folder} autobean construction for the root-folders endpoint.
     * 
     */
    @Test
    public void testGetHomeFolder() {
        // String ROOT_FOLDERS_JSON =
        // "{\"action\":\"root\",\"status\":\"success\",\"roots\":[{\"date-modified\":\"1349374189000\",\"file-size\":\"0\",\"hasSubDirs\":true,\"permissions\":{\"read\":true,\"write\":true,\"own\":true},\"date-created\":\"1349367382000\",\"label\":\"TEST_USER\",\"id\":\"\\/iplant\\/home\\/TEST_USER\"},{\"date-modified\":\"1349367891000\",\"file-size\":\"0\",\"hasSubDirs\":true,\"permissions\":{\"read\":true,\"write\":false,\"own\":false},\"date-created\":\"1349367891000\",\"label\":\"Community Data\",\"id\":\"\\/iplant\\/home\\/shared\"}]}";

        // Convert to autobean
        // AutoBean<RootFolders> bean = AutoBeanCodex.decode(factory, RootFolders.class,
        // ROOT_FOLDERS_JSON);
        AutoBean<RootFolders> bean = AutoBeanCodex.decode(factory, RootFolders.class,
                CONSTANTS.root_folders());
        List<Folder> roots = bean.as().getRoots();

        // Verify autobean contents
        assertEquals("Only two Folders were returned", 2, roots.size());
        for (Folder folder : roots) {
            assertNotNull("Name is not null", folder.getName());
            assertNotNull("ID is not null", folder.getId());
            assertNotNull("Permissions is not null", folder.getPermissions());
            assertNotNull("Date created is not null", folder.getDateCreated());
            assertNotNull("Date modified is not null", folder.getLastModified());
            assertNull("Folder does not contain subfolders", folder.getFolders());
            assertNull("Folder does not contain files", folder.getFiles());
        }
        Permissions userFolderPermissions = roots.get(0).getPermissions();
        Permissions sharedFolderPermissions = roots.get(1).getPermissions();
        assertTrue("User has full permissions to user folder", userFolderPermissions.isOwner()
                && userFolderPermissions.isReadable() && userFolderPermissions.isWritable());
        assertTrue("User only has READ permissions to shared folder", !sharedFolderPermissions.isOwner()
                && sharedFolderPermissions.isReadable() && !sharedFolderPermissions.isWritable());
        assertEquals("Shared folder name is correct", "Community Data", roots.get(1).getName());
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment";
    }

}
