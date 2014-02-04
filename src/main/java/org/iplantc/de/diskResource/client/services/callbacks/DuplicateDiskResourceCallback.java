
package org.iplantc.de.diskResource.client.services.callbacks;

import java.util.Collection;
import java.util.Set;

import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.models.diskresources.DiskResourceExistMap;
import org.iplantc.de.commons.client.views.IsMaskable;
import org.iplantc.de.diskResource.client.services.errors.DiskResourceErrorAutoBeanFactory;
import org.iplantc.de.diskResource.client.services.errors.ErrorDuplicateDiskResource;

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
    public void onSuccess(final DiskResourceExistMap existMapping) {
        unmaskCaller();

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
