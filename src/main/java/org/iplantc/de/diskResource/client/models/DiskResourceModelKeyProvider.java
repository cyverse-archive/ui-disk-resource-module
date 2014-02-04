package org.iplantc.de.diskResource.client.models;


import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;

import com.sencha.gxt.data.shared.ModelKeyProvider;

public class DiskResourceModelKeyProvider implements ModelKeyProvider<DiskResource> {
    @Override
    public String getKey(DiskResource item) {
        return item.getId();
    }
}
