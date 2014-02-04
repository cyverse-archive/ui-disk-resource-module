package org.iplantc.de.diskResource.client.services.errors;

import com.google.web.bindery.autobean.shared.AutoBean;

/**
 * An interface for an {@link AutoBean} which has a "path" key.
 * @author jstroot
 *
 */
public interface HasPath {
    
    String getPath();
    
}
