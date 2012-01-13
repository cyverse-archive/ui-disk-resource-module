package org.iplantc.core.uidiskresource.client.models;

import java.util.Date;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.util.DateParser;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.json.client.JSONObject;

/**
 * Models the metadata related to a resource stored on disk (either a file or folder).
 * 
 * @author amuir
 * 
 */
public abstract class DiskResource extends BaseTreeModel {
    private static final long serialVersionUID = 557899228357342079L;

    public static final String ID = "id"; //$NON-NLS-1$
    public static final String NAME = "name"; //$NON-NLS-1$
    public static final String LABEL = "label"; //$NON-NLS-1$
    public static final String DATE_CREATED = "date-created"; //$NON-NLS-1$
    public static final String DATE_MODIFIED = "date-modified"; //$NON-NLS-1$
    public static final String PERMISSIONS = "permissions"; //$NON-NLS-1$

    private Permissions permissions;

    /**
     * Instantiate from id and name.
     * 
     * @param id unique id for this disk resource.
     * @param name disk resource name.
     */
    protected DiskResource(String id, String name, Permissions permissions) {
        setId(id);
        setName(name);
        setPermissions(permissions);
    }

    /**
     * Instantiate from a JSON object that contains at least "id" and "label" values.
     * 
     * @param resource A JSON object which should contain at least "id" and "name" values.
     */
    protected DiskResource(JSONObject resource) {
        setId(JsonUtil.getString(resource, ID));
        setName(JsonUtil.getString(resource, LABEL));

        setDateCreated(DateParser.parseDate(JsonUtil.getString(resource, DATE_CREATED)));
        setLastModified(DateParser.parseDate(JsonUtil.getString(resource, DATE_MODIFIED)));
        setPermissions(new Permissions(JsonUtil.getObject(resource, PERMISSIONS)));
    }

    /**
     * Set unique id.
     * 
     * @param id unique id for this disk resource.
     */
    public void setId(String id) {
        set(ID, id);
    }

    /**
     * Retrieve unique id.
     * 
     * @return resource id.
     */
    public String getId() {
        return get(ID);
    }

    /**
     * Set resource name.
     * 
     * @param name name for this disk resource.
     */
    public void setName(String name) {
        set(NAME, name);
    }

    /**
     * Retrieve our id.
     * 
     * @return resource name.
     */
    public String getName() {
        return get(NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Retrieve disk resource status.
     * 
     * @return status used for display.
     */
    public abstract String getStatus();

    /**
     * Set modified date
     * 
     * @param date
     */
    public void setLastModified(Date date) {
        set(DATE_MODIFIED, date);
    }

    /**
     * Gets the last modified date
     * 
     * @return a Date object for the last modified date
     */
    public Date getLastModified() {
        return get(DATE_MODIFIED);
    }

    /**
     * Set created date
     * 
     * @param date
     */
    public void setDateCreated(Date date) {
        set(DATE_CREATED, date);
    }

    /**
     * Gets the created date.
     * 
     * @return a Date object for the created date
     */
    public Date getDateCreated() {
        return get(DATE_CREATED);
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    /**
     * @return the permissions
     */
    public Permissions getPermissions() {
        return permissions;
    }
}
