
package org.iplantc.core.uidiskresource.client.services.callbacks;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceExistMap;
import org.iplantc.core.uicommons.client.views.IsMaskable;
import org.iplantc.core.uidiskresource.client.services.errors.DiskResourceErrorAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.services.errors.ErrorDuplicateDiskResource;

import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;


public abstract class DuplicateDiskResourceCallback extends DiskResourceServiceCallback<DiskResourceExistMap> {
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
    public void onSuccess(final DiskResourceExistMap response) {
        unmaskCaller();

        final Map<String, Boolean> existMapping = response.getMap();
        final Set<String> dupes = Sets.newHashSet();
        for (String res : diskResourceIds) {
            if (existMapping.get(res)) {
                dupes.add(res);
            }
        }

        // always call mark duplicates. if no duplicates are found the list is empty.
        // clients implementing this class then just needs to override only on method
        markDuplicates(dupes);
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
