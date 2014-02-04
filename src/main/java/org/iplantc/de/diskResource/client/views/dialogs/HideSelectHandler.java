package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IsHideable;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

final class HideSelectHandler implements SelectHandler {
    private final IsHideable dlg;

    public HideSelectHandler(IsHideable dlg) {
        this.dlg = dlg;
    }

    @Override
    public void onSelect(SelectEvent event) {
        dlg.hide();
    }
}