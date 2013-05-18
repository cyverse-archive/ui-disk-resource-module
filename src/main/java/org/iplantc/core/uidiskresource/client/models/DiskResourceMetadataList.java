package org.iplantc.core.uidiskresource.client.models;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * Convenience autobean for de-serializing lists of metadata.
 * 
 * @author jstroot
 * 
 */
public interface DiskResourceMetadataList {

    @PropertyName("metadata")
    List<DiskResourceMetadata> getMetadata();
}
