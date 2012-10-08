package org.iplantc.core.uidiskresource.client;

import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;

import com.google.gwt.core.client.GWT;

public class Services {

    public static final DiskResourceServiceFacade DISK_RESOURCE_SERVICE = GWT
            .create(DiskResourceServiceFacade.class);
}
