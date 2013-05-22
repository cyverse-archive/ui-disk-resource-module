package org.iplantc.core.uidiskresource.client.models;

import java.util.List;

/**
 * An AutoBean interface for requests to the data service "restore" endpoint.
 * 
 * @author psarando
 * 
 */
public interface RestoreRequest {

    List<String> getPaths();

    void setPaths(List<String> paths);
}
