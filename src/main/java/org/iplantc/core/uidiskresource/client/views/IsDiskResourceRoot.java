package org.iplantc.core.uidiskresource.client.views;

import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;

public interface IsDiskResourceRoot {

    /**
     * @param dr
     * @return true if the given <code>DiskResource</code> is a root folder, false otherwise
     */
    boolean isRoot(final DiskResource dr);

}
