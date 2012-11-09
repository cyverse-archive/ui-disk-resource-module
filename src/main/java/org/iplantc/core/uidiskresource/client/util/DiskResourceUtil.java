/**
 * 
 */
package org.iplantc.core.uidiskresource.client.util;

import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;

/**
 * @author sriram
 *
 */
public class DiskResourceUtil {
    /**
     * Parse the parent folder from a path.
     * 
     * @param path the path to parse.
     * @return the parent folder.
     */
    public static String parseParent(String path) {
        String ret = ""; //$NON-NLS-1$
        if (path == null || path.trim().isEmpty()) {
            return ret;
        }
        String[] items = path.split("/"); //$NON-NLS-1$
        boolean firstPass = true;

        for (int i = 0; i < items.length - 1; i++) {
            if (firstPass) {
                firstPass = false;
            } else {
                ret += "/"; //$NON-NLS-1$
            }

            ret += items[i];
        }

        return ret;
    }

    /**
     * Parse the display name from a path.
     * 
     * @param path the path to parse.
     * @return the display name.
     */
    public static String parseNameFromPath(String path) {
        String ret = ""; //$NON-NLS-1$

        if (path != null && !path.trim().isEmpty()) {
            String[] items = path.split("/"); //$NON-NLS-1$
            ret = items[items.length - 1];
        }

        return ret;
    }
    
    public static boolean isOwner(DiskResource resource) {
        return resource.getPermissions().isOwner();
    }

    public static boolean isOwner(Iterable<DiskResource> resources) {
        // Use predicate to determine if user is owner of all disk resources
        boolean isDeletable = true;
        for (DiskResource dr : resources) {
            if (!dr.getPermissions().isOwner()) {
                isDeletable = false;
                break;
            }
        }
        return isDeletable;
    }
}
