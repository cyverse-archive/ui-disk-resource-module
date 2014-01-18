package org.iplantc.core.uidiskresource.client.gin;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import com.sencha.gxt.core.client.XTemplates;

import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;

final class TreeCell extends AbstractCell<Folder> {

    interface Templates extends XTemplates {
        @XTemplate("{name}&nbsp;<span style='cursor: pointer;'>X</span>")
        SafeHtml drqt(String name);

        @XTemplate("*&nbsp;{name}&nbsp;<span style='cursor: pointer;'>X</span>")
        SafeHtml drqtDirty(String name);
    }

    private final Templates templates = GWT.create(Templates.class);

    public TreeCell() {
        super(CLICK);
    }

    @Override
    public void render(Cell.Context context, Folder value, SafeHtmlBuilder sb) {
        if (value instanceof DiskResourceQueryTemplate) {
            if (((DiskResourceQueryTemplate)value).isDirty()) {
                // FIXME JDS This needs to be abstracted into an appearance
                // sb.append(SafeHtmlUtils.fromString("* " + value.getName()));
                sb.append(templates.drqtDirty(value.getName()));

            } else {
                // sb.append(SafeHtmlUtils.fromString(value.getName()));
                sb.append(templates.drqt(value.getName()));

            }
        } else {
            // Normal folder
            sb.append(SafeHtmlUtils.fromString(value.getName()));
        }

    }

    @Override
    public void onBrowserEvent(Context context, Element parent, Folder value, NativeEvent event, ValueUpdater<Folder> valueUpdater) {
        // TODO Auto-generated method stub
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
    }
}