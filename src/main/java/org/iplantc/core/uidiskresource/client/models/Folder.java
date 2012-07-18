package org.iplantc.core.uidiskresource.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONObject;

/**
 * Models the metadata related to a Folder
 */
public class Folder extends DiskResource {
    private static final long serialVersionUID = 2525604102944798997L;

    private boolean hasSubFolders;

    /**
     * Instantiate from id and name.
     * 
     * @param id unique folder id.
     * @param name folder name.
     */
    public Folder(String id, String name, boolean hasSubFolders, Permissions permissions) {
        super(id, name, permissions);
        this.setHasSubFolders(hasSubFolders);
    }

    /**
     * Instantiate from a JSON object that contains at least "id" and "label" values.
     * 
     * @param folder A JSON object which should contain at least "id" and "label" values.
     */
    public Folder(JSONObject folder) {
        super(folder);

        setHasSubFolders(JsonUtil.getBoolean(folder, "hasSubDirs", true)); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatus() {
        return getChildren().size() + " " + "files";
    }

    /**
     * @param hasSubFolders the hasSubFolders to set
     */
    public void setHasSubFolders(boolean hasSubFolders) {
        this.hasSubFolders = hasSubFolders;
    }

    /**
     * @return the hasSubFolders
     */
    public boolean hasSubFolders() {
        return hasSubFolders;
    }
}
