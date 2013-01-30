package org.iplantc.core.uidiskresource.client.models;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface File extends DiskResource {

    @PropertyName("file-size")
    String getSize();

}
