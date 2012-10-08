package org.iplantc.core.uidiskresource.client.models.autobeans;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface Permissions {

    @PropertyName("own")
    boolean isOwner();

    @PropertyName("own")
    void setOwner(boolean owner);

    @PropertyName("read")
    boolean isReadable();

    @PropertyName("read")
    void setReadable(boolean readable);

    @PropertyName("write")
    boolean isWritable();

    @PropertyName("write")
    void setWritable(boolean writable);
}
