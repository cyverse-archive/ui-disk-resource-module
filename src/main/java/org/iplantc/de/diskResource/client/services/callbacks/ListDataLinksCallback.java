package org.iplantc.de.diskResource.client.services.callbacks;

import java.util.List;

import org.iplantc.de.jsonutil.client.JsonUtil;
import org.iplantc.de.diskResource.client.dataLink.models.DataLink;
import org.iplantc.de.diskResource.client.dataLink.models.DataLinkFactory;
import org.iplantc.de.diskResource.client.dataLink.models.DataLinkList;
import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

public class ListDataLinksCallback<M> implements AsyncCallback<String> {

    private final Tree<M, M> tree;
    private final DataLinkFactory dlFactory;
    
    public ListDataLinksCallback(Tree<M, M> tree, DataLinkFactory dlFactory) {
        this.tree = tree;
        this.dlFactory = dlFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(String result) {
        // Get tickets by resource id, add them to the tree.
        JSONObject response = JsonUtil.getObject(result);
        JSONObject tickets = JsonUtil.getObject(response, "tickets");
        
        Splittable placeHolder;
        for(String key : tickets.keySet()){
            placeHolder = StringQuoter.createSplittable();
            M dr = tree.getStore().findModelWithKey(key);
            
            JSONArray dlIds = JsonUtil.getArray(tickets, key);
            Splittable splittable = StringQuoter.split(dlIds.toString());
            splittable.assign(placeHolder, "tickets");
            AutoBean<DataLinkList> ticketsAB = AutoBeanCodex.decode(dlFactory, DataLinkList.class, placeHolder);
            
            List<DataLink> dlList = ticketsAB.as().getTickets();
            
            for(DataLink dl : dlList){
                tree.getStore().add(dr, (M)dl);
                tree.setChecked((M)dl, CheckState.CHECKED);
            }
        }
        // Select all roots automatically
        tree.setCheckedSelection(tree.getStore().getAll());
        for (M m : tree.getStore().getAll()) {
            tree.setExpanded(m, true);
        }
    }

    @Override
    public void onFailure(Throwable caught) {
        ErrorHandler.post(I18N.ERROR.listDataLinksError(), caught);
    }
}
