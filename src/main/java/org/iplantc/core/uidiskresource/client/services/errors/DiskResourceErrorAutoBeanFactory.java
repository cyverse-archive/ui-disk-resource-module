package org.iplantc.core.uidiskresource.client.services.errors;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanFactory.Category;

@Category(DiskResourceCategory.class)
public interface DiskResourceErrorAutoBeanFactory extends AutoBeanFactory {

    AutoBean<ErrorDiskResourceDelete> diskResourceDeleteError();

    AutoBean<ErrorCreateFolder> createFolderError();
    
    AutoBean<ErrorMoveDiskResource> moveDiskResourceError();
    
    AutoBean<ErrorUpdateMetadata> errorUpdateMetadata();
}
