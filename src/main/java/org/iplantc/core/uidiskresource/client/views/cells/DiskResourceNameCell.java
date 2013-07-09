package org.iplantc.core.uidiskresource.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOUT;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import org.iplantc.core.resources.client.DiskResourceNameCellStyle;
import org.iplantc.core.resources.client.IplantResources;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.File;
import org.iplantc.core.uicommons.client.models.diskresources.Folder;
import org.iplantc.core.uidiskresource.client.events.DataSearchNameSelectedEvent;
import org.iplantc.core.uidiskresource.client.events.DiskResourceSelectedEvent;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.tips.Tip;

/**
 * A cell for displaying the icons and names for <code>DiskResource</code> list items.
 * 
 * TODO JDS Implement preview tooltip.
 * Tooltip will probably have to be {@link Tip}, since this is a cell and not a widget.
 * 
 * @author jstroot
 * 
 */
public class DiskResourceNameCell extends AbstractCell<DiskResource> {

    private static final DiskResourceNameCellStyle CSS = IplantResources.RESOURCES.diskResourceNameCss();

    public static enum CALLER_TAG {
        DATA, SEARCH, SHARING;
    }

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span><span class=\"{0}\"> </span>&nbsp;<span name=\"drName\" class=\"{1}\" >{2}</span></span>")
        SafeHtml cell(String imgClassName, String diskResourceClassName, SafeHtml diskResourceName);
    }

    final Templates templates = GWT.create(Templates.class);
    private final IsWidget caller;
    private boolean previewEnabled = true;

    final CALLER_TAG tag;

    public DiskResourceNameCell(IsWidget caller, CALLER_TAG tag) {
        super(CLICK, MOUSEOVER, MOUSEOUT);

        this.tag = tag;
        this.caller = caller;
        CSS.ensureInjected();
    }

    @Override
    public void render(Cell.Context context, DiskResource value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }

        SafeHtml name = SafeHtmlUtils.fromString(value.getName());
        if (value instanceof File) {
            String nameStyle = previewEnabled ? CSS.nameStyle() : CSS.nameStyleNoPointer();
            sb.append(templates.cell(CSS.drFile(), nameStyle, name));
        } else if (value instanceof Folder) {
            sb.append(templates.cell(CSS.drFolder(), CSS.nameStyle(), name));
        }

    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, DiskResource value,
            NativeEvent event, ValueUpdater<DiskResource> valueUpdater) {
        if (value == null) {
            return;
        }

        Element eventTarget = Element.as(event.getEventTarget());
        if (parent.isOrHasChild(eventTarget)) {

            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    doOnClick(eventTarget, value, valueUpdater);
                    break;
                case Event.ONMOUSEOVER:
                    doOnMouseOver(eventTarget, value);
                    break;
                case Event.ONMOUSEOUT:
                    doOnMouseOut(eventTarget, value);
                    break;
                default:
                    break;
            }
        }
    }

    private void doOnMouseOut(Element eventTarget, DiskResource value) {
        if (!isValidClickTarget(eventTarget, value)) {
            return;
        }

        eventTarget.getStyle().setTextDecoration(TextDecoration.NONE);
    }

    private void doOnMouseOver(Element eventTarget, DiskResource value) {
        if (!isValidClickTarget(eventTarget, value)) {
            return;
        }

        eventTarget.getStyle().setTextDecoration(TextDecoration.UNDERLINE);
    }

    private void doOnClick(Element eventTarget, DiskResource value,
            ValueUpdater<DiskResource> valueUpdater) {

        if (!isValidClickTarget(eventTarget, value)) {
            return;
        }

        if (tag.equals(CALLER_TAG.DATA)) {
            EventBus.getInstance().fireEvent(new DiskResourceSelectedEvent(caller, value));
        } else if (tag.equals(CALLER_TAG.SEARCH)) {
            EventBus.getInstance().fireEvent(new DataSearchNameSelectedEvent(value));
        }
    }

    private boolean isValidClickTarget(Element eventTarget, DiskResource value) {
        return eventTarget.getAttribute("name").equalsIgnoreCase("drName") //$NON-NLS-1$ //$NON-NLS-2$
                && tag != DiskResourceNameCell.CALLER_TAG.SHARING
                && (previewEnabled || !(value instanceof File));
    }

    public void setPreviewEnabled(boolean previewEnabled) {
        this.previewEnabled = previewEnabled;
    }

}
