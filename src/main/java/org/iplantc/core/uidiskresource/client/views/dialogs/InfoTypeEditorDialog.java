/**
 * 
 */
package org.iplantc.core.uidiskresource.client.views.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacadeImpl;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

/**
 * @author sriram
 *
 */
public class InfoTypeEditorDialog extends IPlantDialog {
    
    private DiskResourceServiceFacade facade;
    
    private SimpleComboBox<String> infoTypeCbo;

    private String type;


    public InfoTypeEditorDialog(String currentType) {
        setSize("300", "100");
        this.type = currentType;
        setHeadingText("Select Type");
        this.facade = new DiskResourceServiceFacadeImpl();
        infoTypeCbo = new SimpleComboBox<String>(new LabelProvider<String>() {

            @Override
            public String getLabel(String item) {
                return item;
            }
            
        });
        infoTypeCbo.setAllowBlank(false);
        infoTypeCbo.setTriggerAction(TriggerAction.ALL);
        infoTypeCbo.setEditable(false);
        loadInfoTypes();
        add(infoTypeCbo);
        
    }
    
    public String getSelectedValue() {
        return infoTypeCbo.getCurrentValue();
    }
    
    
    private void loadInfoTypes() {
        facade.getFileTypes(new AsyncCallback<String>() {
            
            @Override
            public void onSuccess(String result) {
               JSONObject obj = JsonUtil.getObject(result);
               JSONArray typesArr = JsonUtil.getArray(obj, "types");
               List<String> types = new ArrayList<String>();
               if(typesArr != null && typesArr.size() >0) {
                  
                   for (int i = 0;i < typesArr.size(); i++) {
                      types.add(typesArr.get(i).isString().stringValue());
                   }
               }
               infoTypeCbo.add(types);
               infoTypeCbo.setValue(type);
            }
            
            @Override
            public void onFailure(Throwable arg0) {
               ErrorHandler.post(arg0);
                
            }
        });
    }

}