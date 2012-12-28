package org.iplantc.core.uidiskresource.client.models.autobeans;

import java.util.Date;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface DiskResource {

    String getId();

    void setId(String id);

    void setPath(String path);

    String getPath();

    @PropertyName("label")
    String getName();

    @PropertyName("label")
    void setName(String name);

    @PropertyName("date-created")
    Date getDateCreated();

    @PropertyName("date-modified")
    Date getLastModified();

    Permissions getPermissions();
}
