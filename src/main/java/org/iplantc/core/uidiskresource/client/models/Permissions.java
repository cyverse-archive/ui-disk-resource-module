package org.iplantc.core.uidiskresource.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONObject;

/**
 * 
 * A class to model DiskResouce permissions
 * 
 * @author sriram
 *
 */
public class Permissions {

    private boolean readable;
    private boolean writable;
    private boolean owner;
    
    
    
    /**
     * Create new permission model
     * 
     * @param read resource is readable
     * @param write resource is writable
     * @param own resource is owned by the user;
     */
    public Permissions(boolean read, boolean write, boolean own) {
        setReadable(read);
        setWriteable(write);
        setOwner(own);
    }
    
    /**
     * Create new permission model
     * 
     * @param obj JSONObject containing permission model
     * 
     */
    public Permissions(JSONObject obj) {
        setReadable(JsonUtil.getBoolean(obj, "read", false));
        setWriteable(JsonUtil.getBoolean(obj, "write", false));
        setOwner(JsonUtil.getBoolean(obj, "own", false));
    }
  
    /**
     * @return the readable
     */
    public boolean isReadable() {
        return readable;
    }


    /**
     * @param readable the readable to set
     */
    public void setReadable(boolean readOnly) {
        this.readable = readOnly;
    }


    /**
     * @return the writable
     */
    public boolean isWritable() {
        return writable;
    }


    /**
     * @param writable the writable to set
     */
    public void setWriteable(boolean writeEnabled) {
        this.writable = writeEnabled;
    }


    /**
     * @return the owner
     */
    public boolean isOwner() {
        return owner;
    }


    /**
     * @param owner the owner to set
     */
    public void setOwner(boolean owner) {
        this.owner = owner;
    }


  
    
    
}
