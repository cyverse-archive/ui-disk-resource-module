package org.iplantc.core.uidiskresource.client.models;

import java.util.Date;

import org.iplantc.core.uicommons.client.models.HasId;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface DiskResource extends HasId, HasName {

    void setId(String id);

    void setPath(String path);

    String getPath();

    @Override
    @PropertyName("label")
    String getName();

    @Override
    @PropertyName("label")
    void setName(String name);

    @PropertyName("date-created")
    Date getDateCreated();

    @PropertyName("date-modified")
    Date getLastModified();

    Permissions getPermissions();
}
