package org.iplantc.core.uidiskresource.client.search.views.cells;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;

import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent.HasSaveDiskResourceQueryEventHandlers;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler;

public class DiskResourceQueryFormNamePrompt extends Composite implements Editor<DiskResourceQueryTemplate>, HasSaveDiskResourceQueryEventHandlers {

    interface DiskResourceQueryFormNamePromptUiBinder extends UiBinder<Widget, DiskResourceQueryFormNamePrompt> {}

    interface QueryFormNamePromptEditorDriver extends SimpleBeanEditorDriver<DiskResourceQueryTemplate, DiskResourceQueryFormNamePrompt> {}

    private static DiskResourceQueryFormNamePromptUiBinder uiBinder = GWT.create(DiskResourceQueryFormNamePromptUiBinder.class);

    protected BaseEventPreview eventPreview;

    @UiField
    TextField name;

    @Ignore
    @UiField
    TextButton saveFilterBtn;

    @Ignore
    @UiField
    Label saveLabel;

    private final QueryFormNamePromptEditorDriver editorDriver = GWT.create(QueryFormNamePromptEditorDriver.class);
    private boolean showing;

    public DiskResourceQueryFormNamePrompt() {
        initWidget(uiBinder.createAndBindUi(this));
        saveLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
        saveLabel.getElement().getStyle().setMarginLeft(10, Unit.PX);

        editorDriver.initialize(this);
        eventPreview = new BaseEventPreview();
        eventPreview.getIgnoreList().add(getElement());
        eventPreview.setAutoHide(false);
    }

    @Override
    public HandlerRegistration addSaveDiskResourceQueryEventHandler(SaveDiskResourceQueryEventHandler handler) {
        return addHandler(handler, SaveDiskResourceQueryEvent.TYPE);
    }

    @Override
    public void hide() {
        if (showing) {
            editorDriver.flush();
            onHide();
            RootPanel.get().remove(this);
            eventPreview.remove();
            showing = false;
            hidden = true;
        }
    }

    public boolean isShowing() {
        return showing;
    }

    public void show(DiskResourceQueryTemplate filter, Element element, AnchorAlignment alignment) {
        editorDriver.edit(filter);
        show(element, alignment);
    }

    public void show(Element element, AnchorAlignment anchorAlignment) {
        getElement().makePositionable(true);
        RootPanel.get().add(this);
        onShow();
        getElement().updateZIndex(0);

        showing = true;

        getElement().setWidth(element.getOffsetWidth());
        getElement().alignTo(element, anchorAlignment, new int[] {0, 0});

        getElement().show();
        if (!eventPreview.getIgnoreList().contains(element)) {
            eventPreview.getIgnoreList().add(element);
        }
        eventPreview.add();

        focus();
    }

    @UiHandler("cancelSaveFilterBtn")
    void onCancelSaveFilter(@SuppressWarnings("unused") SelectEvent event) {
        // Do not change name of filter
        name.reset();
        hide();
    }

    @UiHandler("saveFilterBtn")
    void onSaveFilterSelected(@SuppressWarnings("unused") SelectEvent event) {
        final DiskResourceQueryTemplate flushedQueryTemplate = editorDriver.flush();
        if (editorDriver.hasErrors()) {
            return;
        }

        // Set the filter name field to allow blank values when hidden.
        fireEvent(new SaveDiskResourceQueryEvent(flushedQueryTemplate));
        hide();
    }

}
