package org.iplantc.core.uidiskresource.client.search.views;

import com.google.gwt.event.shared.HandlerRegistration;

import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.HasCollapseHandlers;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.HasExpandHandlers;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.form.TriggerField;

import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler;

/**
 * This class is a clone-and-own of {@link DateField}.
 * 
 * @author jstroot
 * 
 */
public class DiskResourceSearchField extends TriggerField<String> implements HasExpandHandlers, HasCollapseHandlers {

    /**
     * Creates a new iPlant Search field.
     */
    public DiskResourceSearchField() {
        this(new DiskResourceSearchCell());
    }

    /**
     * Creates a new iPlant Search field.
     * 
     * @param cell the search cell
     */
    public DiskResourceSearchField(DiskResourceSearchCell cell) {
        this(cell, null);
    }

    /**
     * Creates a new iPlant Search field.
     * 
     * @param cell the search cell
     * @param propertyEditor the property editor
     */
    public DiskResourceSearchField(DiskResourceSearchCell cell, PropertyEditor<String> propertyEditor) {
        super(cell);
        setPropertyEditor(propertyEditor);
        redraw();
    }

    public HandlerRegistration addSaveDiskResourceQueryTemplateEventHandler(SaveDiskResourceQueryEventHandler handler) {
        return getCell().addHandler(handler, SaveDiskResourceQueryEvent.TYPE);
    }

    public HandlerRegistration addSubmitDiskResourceQueryEventHandler(SubmitDiskResourceQueryEventHandler handler) {
        return getCell().addHandler(handler, SubmitDiskResourceQueryEvent.TYPE);
    }

    @Override
    public DiskResourceSearchCell getCell() {
        return (DiskResourceSearchCell)super.getCell();
    }


    protected void expand() {
        getCell().expand(createContext(), getElement(), getValue(), valueUpdater);
    }

    @Override
    protected void onCellParseError(ParseErrorEvent event) {
        super.onCellParseError(event);
        /*String value = event.getException().getMessage();
        String f = getPropertyEditor().getFormat().getPattern();
        String msg = DefaultMessages.getMessages().dateField_invalidText(value, f);
        parseError = msg;*/
        // TODO Update parse error message
        String msg = "Default message";
        forceInvalid(msg);
    }

    @Override
    public HandlerRegistration addExpandHandler(ExpandHandler handler) {
        return getCell().addHandler(handler, ExpandEvent.getType());
    }

    @Override
    public HandlerRegistration addCollapseHandler(CollapseHandler handler) {
        return getCell().addHandler(handler, CollapseEvent.getType());
    }

}
