package org.iplantc.core.uidiskresource.client.services.callbacks;

import java.util.Collection;
import java.util.Set;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.views.IsMaskable;
import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.services.errors.DiskResourceErrorAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.services.errors.ErrorDuplicateDiskResource;

import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;


public abstract class DuplicateDiskResourceCallback extends DiskResourceServiceCallback {
    private final Set<String> diskResourceIds;

    public DuplicateDiskResourceCallback(Iterable<String> diskResourceIds, final IsMaskable maskable) {
        super(maskable);
        this.diskResourceIds = Sets.newHashSet(diskResourceIds);
    }

    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.duplicateCheckFailed();
    }

    @Override
    public void onSuccess(String response) {
        unmaskCaller();
        Splittable split = StringQuoter.split(response).get("paths");
        Set<String> dupes = Sets.newHashSet();
        for (String key : split.getPropertyKeys()) {
            if (split.get(key).asBoolean()) {
                dupes.add(key);
            }
        }

        // always call mark duplicates. if no duplicates are found the list is empty.
        // clients implementing this class then just needs to override only on method
        markDuplicates(Sets.intersection(dupes, diskResourceIds));

        // JSONObject jsonResponse = JsonUtil.getObject(response);
        //
        //        String status = JsonUtil.getString(jsonResponse, "status"); //$NON-NLS-1$
        //        JSONObject paths = JsonUtil.getObject(jsonResponse, "paths"); //$NON-NLS-1$
        //
        //        if (!status.equalsIgnoreCase("success") || paths == null) { //$NON-NLS-1$
        //            onFailure(new Exception(JsonUtil.getString(jsonResponse, "reason"))); //$NON-NLS-1$
        // return;
        // }
        //
        // List<String> duplicateFiles = new ArrayList<String>();
        //
        // for (final String resourceId : diskResourceIds) {
        // // TODO add an extra check to make sure the resourceId key is found in paths?
        // boolean fileExists = JsonUtil.getBoolean(paths, resourceId, false);
        //
        // if (fileExists) {
        // duplicateFiles.add(DiskResourceUtil.parseNameFromPath(resourceId));
        // }
        // }
        //
        // // always call mark duplicates. if no duplicates are found the list is empty.
        // // clients implementing this class then just needs to override only on method
        // markDuplicates(duplicateFiles);
    }
    
    @Override
    public void onFailure(Throwable caught) {
        unmaskCaller();
        DiskResourceErrorAutoBeanFactory factory = GWT.create(DiskResourceErrorAutoBeanFactory.class);
        AutoBean<ErrorDuplicateDiskResource> errorBean = AutoBeanCodex.decode(factory, ErrorDuplicateDiskResource.class,
                caught.getMessage());

        ErrorHandler.post(errorBean.as(), caught);
    }

    public abstract void markDuplicates(Collection<String> duplicates);

}
