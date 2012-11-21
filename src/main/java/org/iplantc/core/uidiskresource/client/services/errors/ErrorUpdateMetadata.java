package org.iplantc.core.uidiskresource.client.services.errors;

import org.iplantc.core.uicommons.client.errorHandling.models.ServiceError;

public interface ErrorUpdateMetadata extends ServiceError {

    String getPath();
}
