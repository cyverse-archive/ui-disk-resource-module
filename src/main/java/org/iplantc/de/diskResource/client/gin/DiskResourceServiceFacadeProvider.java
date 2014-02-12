package org.iplantc.de.diskResource.client.gin;

import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.commons.client.gin.ServicesInjector;

import com.google.inject.Provider;

public class DiskResourceServiceFacadeProvider implements Provider<DiskResourceServiceFacade> {

    @Override
    public DiskResourceServiceFacade get() {
        return ServicesInjector.INSTANCE.getDiskResourceServiceFacade();
    }

}
