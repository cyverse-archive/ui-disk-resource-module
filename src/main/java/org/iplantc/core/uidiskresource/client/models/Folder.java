package org.iplantc.core.uidiskresource.client.models;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface Folder extends DiskResource {

    @PropertyName("hasSubDirs")
    boolean hasSubDirs();

    List<Folder> getFolders();

    List<File> getFiles();
}
