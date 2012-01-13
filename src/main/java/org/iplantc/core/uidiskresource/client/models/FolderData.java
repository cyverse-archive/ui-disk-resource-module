package org.iplantc.core.uidiskresource.client.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Folder data model object.
 * 
 * @author amuir
 * 
 */
public class FolderData {
    private String path;
    private String name;
    private ArrayList<DiskResource> resources;

    /**
     * Instantiate from name and id.
     * 
     * @param name folder name,
     */
    public FolderData(final String path, final String name) {
        setPath(path);
        setName(name);

        resources = new ArrayList<DiskResource>();
    }

    /**
     * Set our name.
     * 
     * @param name desired name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Retrieve the folder name.
     * 
     * @return folder name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set our path.
     * 
     * @param path current path.
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * Retrieve our path.
     * 
     * @return the path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Retrieve files and sub-folders in this folder.
     * 
     * @return list of contained disk resource.
     */
    public List<DiskResource> getResources() {
        return resources;
    }

    /**
     * Add a new disk resource to this folder.
     * 
     * @param resource disk resource to add.
     */
    public void addDiskResource(DiskResource resource) {
        if (resource != null) {
            resources.add(resource);
        }
    }

    /**
     * Remove disk resource to this folder.
     * 
     * @param resource disk resource to remove.
     */
    public void removeDiskResource(DiskResource resource) {
        if (resources.contains(resource)) {
            resources.remove(resource);
        }
    }
}
