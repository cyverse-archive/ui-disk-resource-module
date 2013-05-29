package org.iplantc.core.uidiskresource.client.models;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean;

/**
 * An interface for an {@link AutoBean} which has a "paths" key.
 * 
 * @author jstroot
 * 
 */
public interface HasPaths {

    List<String> getPaths();

    void setPaths(List<String> paths);
}
