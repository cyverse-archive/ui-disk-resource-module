package org.iplantc.core.uidiskresource.client.services.callbacks;

import java.util.Set;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.DiskResourceAutoBeanFactory;
import org.iplantc.core.uidiskresource.client.models.RestoreResponse;
import org.iplantc.core.uidiskresource.client.models.RestoreResponse.RestoredResource;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;

import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

/**
 * A DiskResourceServiceCallback for data service "restore" endpoint requests.
 * 
 * @author psarando
 * 
 */
public class DiskResourceRestoreCallback extends DiskResourceServiceCallback {
    private final DiskResourceView view;
    private final DiskResourceAutoBeanFactory drFactory;
    private final Set<DiskResource> selectedResources;

    public DiskResourceRestoreCallback(DiskResourceView view, DiskResourceAutoBeanFactory drFactory,
            Set<DiskResource> selectedResources) {
        super(view);

        this.drFactory = drFactory;
        this.selectedResources = selectedResources;
        this.view = view;
    }

    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.restoreDefaultMsg();
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);

        checkForPartialRestore(result);
        view.removeDiskResources(selectedResources);
    }

    private void checkForPartialRestore(String result) {
        RestoreResponse response = AutoBeanCodex.decode(drFactory, RestoreResponse.class, result).as();
        Splittable restored = response.getRestored();

        for (DiskResource resource : selectedResources) {
            Splittable restoredResourceJson = restored.get(resource.getId());

            if (restoredResourceJson != null) {
                RestoredResource restoredResource = AutoBeanCodex.decode(drFactory,
                        RestoredResource.class, restoredResourceJson).as();

                if (restoredResource.isPartialRestore()) {
                    AlertMessageBox alert = new AlertMessageBox(I18N.DISPLAY.information(),
                            I18N.DISPLAY.partialRestore());
                    alert.show();
                    break;
                }
            }
        }
    }
}
