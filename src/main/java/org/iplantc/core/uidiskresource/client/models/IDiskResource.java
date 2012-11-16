package org.iplantc.core.uidiskresource.client.models;

import java.util.Date;

public interface IDiskResource {

    /**
     * Retrieve unique id.
     * 
     * @return resource id.
     */
    String getId();

    /**
     * Retrieve our id.
     * 
     * @return resource name.
     */
    String getName();

    /**
     * Gets the last modified date
     * 
     * @return a Date object for the last modified date
     */
    Date getLastModified();

    /**
     * Gets the created date.
     * 
     * @return a Date object for the created date
     */
    Date getDateCreated();

}