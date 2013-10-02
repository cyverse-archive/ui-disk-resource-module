package org.iplantc.core.uidiskresource.client.gin;

import org.iplantc.core.uicommons.client.gin.ServicesInjector;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;

import com.google.inject.Provider;

public class DiskResourceServiceFacadeProvider implements Provider<DiskResourceServiceFacade> {

    @Override
    public DiskResourceServiceFacade get() {
        return ServicesInjector.INSTANCE.getDiskResourceServiceFacade();
    }

}
