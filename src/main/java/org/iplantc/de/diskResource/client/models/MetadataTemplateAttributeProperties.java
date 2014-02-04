package org.iplantc.de.diskResource.client.models;

import org.iplantc.core.uicommons.client.models.diskresources.MetadataTemplateAttribute;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface MetadataTemplateAttributeProperties extends PropertyAccess<MetadataTemplateAttribute> {

	ValueProvider<MetadataTemplateAttribute, String> id();

	ValueProvider<MetadataTemplateAttribute, String> name();

}
