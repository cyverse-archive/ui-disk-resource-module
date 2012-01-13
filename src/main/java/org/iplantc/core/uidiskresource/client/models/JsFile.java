package org.iplantc.core.uidiskresource.client.models;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;

/**
 * A class that uses native methods to return fields for attributes.
 * 
 * This object simulates a Plain Old Java Object from the Java perspective and use the JavaScript Native
 * Interface to interoperate with the JSON representation.
 * 
 * @author sriram
 * 
 */
public class JsFile extends JavaScriptObject {
    /**
     * Default constructor.
     */
    protected JsFile() {
    }

    // Define JSNI methods to get File info from the native JavaScript Object
    /**
     * Gets the name value of the file from the object.
     * 
     * @return a string representing the name of the file
     */
    public final native String getName() /*-{
                                         return this.name;
                                         }-*/;

    /**
     * Gets the label value of the file from the object.
     * 
     * @return a string representing the label of the file
     */
    public final native String getLabel() /*-{
                                          return this.label;
                                          }-*/;

    /**
     * Gets the uploaded date value of the file from the object.
     * 
     * @return a string representing the uploaded date of the file
     */
    public final native String getLastModified() /*-{
                                                 return this.date-modified;
                                                 }-*/;

    /**
     * Gets the type value of the file from the object.
     * 
     * This type attempting to identify the type of data the file represents.
     * 
     * @return a string representing the type of the data represented by the file
     */
    public final native String getType() /*-{
                                         return this.type;
                                         }-*/;

    /**
     * Gets a user-defined description value for the file from the object.
     * 
     * @return a string representing the user's description of the file
     */
    public final native String getDescription() /*-{
                                                return this.description;
                                                }-*/;

    /**
     * Gets an internal identifier value for the file from the object.
     * 
     * @return a string representing a unique identifier for the file
     */
    public final native String getId() /*-{
                                       return this.id;
                                       }-*/;

    /**
     * Gets an path value for the file from the object.
     * 
     * @return a string representing a path for the file
     */
    public final native String getPath() /*-{
                                         return this.path;
                                         }-*/;

    /**
     * Gets the size of the file
     * 
     * @return a string representing file size
     */
    public final native String getSize() /*-{
                                         return this.file-size;
                                         }-*/;

    /**
     * Gets the file permissions
     * 
     * @return a JSONObject representing file permissions
     */
    public final native JSONObject getPermissions() /*-{
                                                    return this.file-size;
                                                    }-*/;
}
