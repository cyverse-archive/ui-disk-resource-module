package org.iplantc.de.diskResource.client.models;

import org.iplantc.de.commons.client.models.diskresources.DiskResourceMetadata;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface DiskResourceMetadataProperties extends PropertyAccess<DiskResourceMetadata> {

    ValueProvider<DiskResourceMetadata, String> attribute();

    ValueProvider<DiskResourceMetadata, String> value();

    ValueProvider<DiskResourceMetadata, String> unit();

}
