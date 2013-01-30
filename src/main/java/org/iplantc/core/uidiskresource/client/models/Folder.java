package org.iplantc.core.uidiskresource.client.models;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface Folder extends DiskResource {

    @PropertyName("hasSubDirs")
    boolean hasSubDirs();

    @PropertyName("folders")
    List<Folder> getFolders();

    @PropertyName("files")
    List<File> getFiles();

}
