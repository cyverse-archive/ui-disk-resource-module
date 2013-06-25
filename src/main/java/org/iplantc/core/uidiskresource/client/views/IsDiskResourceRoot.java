package org.iplantc.core.uidiskresource.client.views;

import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;

public interface IsDiskResourceRoot {

    /**
     * @param dr
     * @return true if the given <code>DiskResource</code> is a root folder, false otherwise
     */
    boolean isRoot(final DiskResource dr);

}
