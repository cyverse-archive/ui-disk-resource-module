package org.iplantc.core.uidiskresource.client.models;

import com.google.gwt.core.client.JavaScriptObject;

public class JsDiskResourceMetaData extends JavaScriptObject {

    protected JsDiskResourceMetaData() {

    }

    /**
     * Gets the attribute
     * 
     * @return a string representing attribute
     */
    public final native String getAttr() /*-{
		return this.attr;
    }-*/;

    /**
     * Gets the unit
     * 
     * @return a string representing unit
     */
    public final native String getUnit() /*-{
		return this.unit;
    }-*/;

    /**
     * Gets the value
     * 
     * @return a string representing value
     */
    public final native String getVal() /*-{
		return this.value;
    }-*/;

}
