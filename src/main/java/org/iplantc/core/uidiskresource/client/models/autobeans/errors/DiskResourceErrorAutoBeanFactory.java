package org.iplantc.core.uidiskresource.client.models.autobeans.errors;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface DiskResourceErrorAutoBeanFactory extends AutoBeanFactory {

    AutoBean<DiskResourceDeleteError> diskResourceDeleteError();

    AutoBean<CreateFolderError> createFolderError();
}
