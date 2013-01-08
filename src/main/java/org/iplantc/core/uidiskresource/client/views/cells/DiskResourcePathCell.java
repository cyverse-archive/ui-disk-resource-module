/**
 * 
 */
package org.iplantc.core.uidiskresource.client.views.cells;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uidiskresource.client.events.DataSearchPathSelectedEvent;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author sriram
 *
 */
public class DiskResourcePathCell extends AbstractCell<DiskResource> {

    public DiskResourcePathCell() {
        super("click");
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context, DiskResource value,
            SafeHtmlBuilder sb) {
        sb.appendHtmlConstant("<div style=\"cursor:pointer;text-decoration:underline;white-space:pre-wrap;\">"
                + value.getPath() + "</div>");
    }

    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent,
            DiskResource value, NativeEvent event, ValueUpdater<DiskResource> valueUpdater) {
        if (value == null) {
            return;
        }

        // Call the super handler, which handlers the enter key.
        super.onBrowserEvent(context, parent, value, event, valueUpdater);

        if ("click".equals(event.getType())) {
            EventBus.getInstance().fireEvent(new DataSearchPathSelectedEvent(value));
        }
    }

}
