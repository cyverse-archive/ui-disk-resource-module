package org.iplantc.core.uidiskresource.client.services.errors;

import org.iplantc.core.uicommons.client.errorHandling.models.ServiceError;

public interface ErrorMoveDiskResource extends ServiceError{

    String getPaths();
}
