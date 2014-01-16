package org.iplantc.core.uidiskresource.client.search.views;

import com.google.gwt.event.shared.HandlerRegistration;

import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.HasCollapseHandlers;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.HasExpandHandlers;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.form.TriggerField;

import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.services.impl.DiskResourceQueryTemplateBuilder;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent.HasSaveDiskResourceQueryEventHandlers;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent.HasSubmitDiskResourceQueryEventHandlers;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler;
import org.iplantc.core.uidiskresource.client.search.views.cells.DiskResourceSearchCell;

import java.text.ParseException;

/**
 * This class is a clone-and-own of {@link DateField}.
 * 
 * @author jstroot
 * 
 */
public class DiskResourceSearchField extends TriggerField<String> implements HasExpandHandlers, HasCollapseHandlers, HasSaveDiskResourceQueryEventHandlers, HasSubmitDiskResourceQueryEventHandlers {

    public final class QueryStringPropertyEditor extends PropertyEditor<String> {
        @Override
        public String parse(CharSequence text) throws ParseException {
            DiskResourceQueryTemplate parsedTemplate = new DiskResourceQueryTemplateBuilder(text.toString()).build();
            edit(parsedTemplate);

            clearInvalid();

            return text.toString();
        }

        @Override
        public String render(String object) {
            return object;
        }
    }

    /**
     * Creates a new iPlant Search field.
     */
    public DiskResourceSearchField() {
        super(new DiskResourceSearchCell());

        setPropertyEditor(new QueryStringPropertyEditor());
    }

    @Override
    public HandlerRegistration addCollapseHandler(CollapseHandler handler) {
        return getCell().addCollapseHandler(handler);
    }

    @Override
    public HandlerRegistration addExpandHandler(ExpandHandler handler) {
        return getCell().addExpandHandler(handler);
    }

    @Override
    public HandlerRegistration addSaveDiskResourceQueryEventHandler(SaveDiskResourceQueryEventHandler handler) {
        return getCell().addSaveDiskResourceQueryEventHandler(handler);
    }

    @Override
    public HandlerRegistration addSubmitDiskResourceQueryEventHandler(SubmitDiskResourceQueryEventHandler handler) {
        return getCell().addSubmitDiskResourceQueryEventHandler(handler);
    }

    public void clearSearch() {
        // Forward clear call to searchForm
        getCell().getSearchForm().clearSearch();
        clearInvalid();
    }

    public void edit(DiskResourceQueryTemplate queryTemplate) {
        // Forward edit call to searchForm
        getCell().getSearchForm().edit(queryTemplate);
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
        /*
         * String value = event.getException().getMessage();
         * String f = getPropertyEditor().getFormat().getPattern();
         * String msg = DefaultMessages.getMessages().dateField_invalidText(value, f);
         * parseError = msg;
         */
        // TODO Update parse error message
        String msg = "Default message";
        forceInvalid(msg);
    }

}
