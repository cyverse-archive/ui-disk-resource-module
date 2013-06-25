package org.iplantc.core.uidiskresource.client.services.callbacks;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceAutoBeanFactory;
import org.iplantc.core.uicommons.client.models.diskresources.File;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.views.IsMaskable;
import org.iplantc.core.uidiskresource.client.events.DiskResourceRenamedEvent;
import org.iplantc.core.uidiskresource.client.services.errors.DiskResourceErrorAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.services.errors.ErrorDiskResourceRename;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

public class RenameDiskResourceCallback extends DiskResourceServiceCallback {

    private final DiskResource dr;
    private final DiskResourceAutoBeanFactory factory;

    public RenameDiskResourceCallback(DiskResource dr, IsMaskable maskable,
            final DiskResourceAutoBeanFactory factory) {
        super(maskable);
        this.dr = dr;
        this.factory = factory;
    }

    @Override
    public void onSuccess(String result) {
        unmaskCaller();
        Splittable split = StringQuoter.split(result);

        Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(dr));
        AutoBean<? extends DiskResource> newDr = null;
        if( dr instanceof Folder) {
             newDr = AutoBeanCodex.decode(factory, Folder.class, encode); 
        } else {
            newDr = AutoBeanCodex.decode(factory, File.class, encode); 
        }
        
        
        // AutoBeanUtils.getAutoBean(dr);
        String newId = split.get("dest").asString();
        newDr.as().setId(newId);
        newDr.as().setName(newId.substring(newId.lastIndexOf("/") + 1));
        EventBus.getInstance().fireEvent(new DiskResourceRenamedEvent(dr, newDr.as()));
    }

    @Override
    public void onFailure(Throwable caught) {
        unmaskCaller();
        DiskResourceErrorAutoBeanFactory factory = GWT.create(DiskResourceErrorAutoBeanFactory.class);
        String errMessage = caught.getMessage();
        if (JsonUtils.safeToEval(errMessage)) {
            AutoBean<ErrorDiskResourceRename> errorBean = AutoBeanCodex.decode(factory,
                    ErrorDiskResourceRename.class, errMessage);

            ErrorHandler.post(errorBean.as(), caught);
        } else {
            ErrorHandler.post(caught);
        }


    }

    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.renameFailed();
    }

}
