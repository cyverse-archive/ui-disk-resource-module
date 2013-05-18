package org.iplantc.core.uidiskresource.client.views;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface HasHandlerRegistrationMgmt {
    void addEventHandlerRegistration(EventHandler handler, HandlerRegistration reg);

    void unregisterHandler(EventHandler handler);
}
