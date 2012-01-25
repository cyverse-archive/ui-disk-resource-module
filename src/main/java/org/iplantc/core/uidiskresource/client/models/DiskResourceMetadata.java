package org.iplantc.core.uidiskresource.client.models;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class DiskResourceMetadata extends BaseModelData {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String ATTRIBUTE = "attr";
    public static String VALUE = "value";
    public static String UNIT = "unit";
    public static String ID = "id";

    public DiskResourceMetadata(String id, String attr, String value, String unit) {
        set(ATTRIBUTE, attr);
        set(VALUE, value);
        set(UNIT, unit);
        set(ID, id);
    }

    public String getAttribute() {
        return (get(ATTRIBUTE) == null) ? "" : get(ATTRIBUTE).toString();
    }

    public String getValue() {
        return (get(VALUE) == null) ? "" : get(VALUE).toString();
    }

    public String getUnit() {
        return (get(UNIT) == null) ? "" : get(UNIT).toString();
    }

    public String getId() {
        return (get(ID) == null) ? "" : get(ID).toString();
    }

    public void setAttribute(String attr) {
        set(ATTRIBUTE, attr);
    }

    public void setValue(String value) {
        set(VALUE, value);
    }

    public void setUnit(String unit) {
        set(UNIT, unit);
    }

    public void setId(String id) {
        set(ID, id);
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put(ATTRIBUTE, new JSONString(getAttribute()));
        obj.put(VALUE, new JSONString(getValue()));
        obj.put(UNIT, new JSONString(getUnit()));
        return obj;
    }
}
