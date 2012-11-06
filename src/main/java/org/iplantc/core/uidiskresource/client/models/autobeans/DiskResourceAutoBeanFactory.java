package org.iplantc.core.uidiskresource.client.models.autobeans;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface DiskResourceAutoBeanFactory extends AutoBeanFactory {

    AutoBean<Folder> folder();

    AutoBean<Folder> folder(Folder toWrap);

    AutoBean<DiskResource> diskResource();

    AutoBean<File> file();

    AutoBean<Permissions> permissions();

    AutoBean<RootFolders> rootFolders();

    AutoBean<DiskResourceMetadata> metadata();

    AutoBean<DiskResourceMetadataList> metadataList();

}
