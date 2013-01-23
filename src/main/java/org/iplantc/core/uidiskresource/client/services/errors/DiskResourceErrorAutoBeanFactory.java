package org.iplantc.core.uidiskresource.client.services.errors;

import org.iplantc.core.uidiskresource.client.services.errors.categories.ErrorDiskResourceCategory;
import org.iplantc.core.uidiskresource.client.services.errors.categories.ErrorDiskResourceMoveCategory;
import org.iplantc.core.uidiskresource.client.services.errors.categories.ErrorDiskResourceRenameCategory;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanFactory.Category;

@Category({ErrorDiskResourceCategory.class, ErrorDiskResourceRenameCategory.class, ErrorDiskResourceMoveCategory.class})
public interface DiskResourceErrorAutoBeanFactory extends AutoBeanFactory {

    AutoBean<ErrorDiskResourceDelete> diskResourceDeleteError();

    AutoBean<ErrorCreateFolder> createFolderError();
    
    AutoBean<ErrorDiskResourceMove> moveDiskResourceError();
    
    AutoBean<ErrorUpdateMetadata> errorUpdateMetadata();

    AutoBean<ErrorDuplicateDiskResource> errorDuplicateDiskResource();
}
