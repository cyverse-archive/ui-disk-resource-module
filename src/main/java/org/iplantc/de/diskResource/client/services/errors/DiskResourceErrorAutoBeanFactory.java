package org.iplantc.de.diskResource.client.services.errors;

import org.iplantc.de.diskResource.client.services.errors.categories.ErrorCreateFolderCategory;
import org.iplantc.de.diskResource.client.services.errors.categories.ErrorDiskResourceCategory;
import org.iplantc.de.diskResource.client.services.errors.categories.ErrorDiskResourceDeleteCategory;
import org.iplantc.de.diskResource.client.services.errors.categories.ErrorDiskResourceMoveCategory;
import org.iplantc.de.diskResource.client.services.errors.categories.ErrorDiskResourceRenameCategory;
import org.iplantc.de.diskResource.client.services.errors.categories.ErrorDuplicateDiskResourceCategory;
import org.iplantc.de.diskResource.client.services.errors.categories.ErrorGetManifestCategory;
import org.iplantc.de.diskResource.client.services.errors.categories.ErrorUpdateMetadataCategory;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanFactory.Category;

@Category({ErrorDiskResourceCategory.class, 
    ErrorDiskResourceDeleteCategory.class, 
    ErrorCreateFolderCategory.class,
    ErrorDiskResourceMoveCategory.class, 
    ErrorUpdateMetadataCategory.class,
    ErrorDuplicateDiskResourceCategory.class,
    ErrorGetManifestCategory.class,
    ErrorDiskResourceRenameCategory.class})
public interface DiskResourceErrorAutoBeanFactory extends AutoBeanFactory {

    AutoBean<ErrorDiskResource> errorDiskResource();

    AutoBean<ErrorDiskResourceDelete> diskResourceDeleteError();

    AutoBean<ErrorCreateFolder> createFolderError();
    
    AutoBean<ErrorDiskResourceMove> moveDiskResourceError();
    
    AutoBean<ErrorDiskResourceRename> renameDiskResourceError();

    AutoBean<ErrorUpdateMetadata> errorUpdateMetadata();

    AutoBean<ErrorDuplicateDiskResource> errorDuplicateDiskResource();

    AutoBean<ErrorGetManifest> errorGetManifest();
}
