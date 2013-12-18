package org.iplantc.core.uidiskresource.client.search.views;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;

import org.iplantc.core.uicommons.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.core.uicommons.client.models.search.SearchAutoBeanFactory;
import org.iplantc.core.uicommons.client.widgets.IPlantAnchor;
import org.iplantc.core.uidiskresource.client.search.events.SaveDiskResourceQueryEvent;
import org.iplantc.core.uidiskresource.client.search.events.SubmitDiskResourceQueryEvent;

import java.util.List;

/**
 * This form is used to construct, edit and/or save "search filters".
 * 
 * <p>
 * This form may be constructed with or without an existing query template. If a query template is
 * supplied to the constructor, the form will be initialized with given query template. If the default
 * constructor is used, a new template will be created.
 * 
 * <p>
 * When the user clicks the "Search" button;
 * <ol>
 * <li>The form will be validated
 * <ol>
 * <li>If the form is <b>invalid</b>, the validation errors will appear in the form and no other action
 * will occur.</li>
 * <li>Else, a {@link SubmitDiskResourceQueryEvent} will be fired with the form's current query template,
 * and this form will be hidden.</li>
 * </ol>
 * </li>
 * </ol>
 * 
 * <p>
 * When the user clicks the "" hyperlink;
 * <ol>
 * <li>The form will be validated
 * <ol>
 * <li>If the form is <b>invalid</b>, the validation errors will appear in the form and not other action
 * will occur.</li>
 * <li>Else, the user will be presented with a text field allowing them to set a name. Then, if the user
 * clicks "Save", a {@link SaveDiskResourceQueryEvent} will be fired with the form's current query
 * template and this form will be hidden.</li>
 * </ol>
 * </li>
 * </ol>
 * 
 * 
 * @author jstroot
 * 
 */
public class DiskResourceQueryForm extends Composite implements Editor<DiskResourceQueryTemplate> {

    @UiTemplate("DiskResourceQueryForm.ui.xml")
    interface DiskResourceQueryFormUiBinder extends UiBinder<Widget, DiskResourceQueryForm> {}

    interface SearchFormEditorDriver extends SimpleBeanEditorDriver<DiskResourceQueryTemplate, DiskResourceQueryForm> {}

    private final DiskResourceQueryFormUiBinder uiBinder = GWT.create(DiskResourceQueryFormUiBinder.class);

    private static DiskResourceQueryTemplate createDefaultFilter() {
        SearchAutoBeanFactory factory = SearchAutoBeanFactory.INSTANCE;
        AutoBean<DiskResourceQueryTemplate> dataSearchFilter = factory.dataSearchFilter();
        dataSearchFilter.as().setCreatedWithin(factory.dateInterval().as());
        dataSearchFilter.as().setModifiedWithin(factory.dateInterval().as());
        dataSearchFilter.as().setFileSizeRange(factory.fileSizeRange().as());

        return dataSearchFilter.as();
    }

    // TODO Any ignored field needs to be handled

    protected BaseEventPreview eventPreview;

    @Ignore
    @UiField
    VerticalLayoutContainer con;

    @UiField
    TextField createdBy;

    @Ignore
    @UiField(provided = true)
    SimpleComboBox<String> createdWithinCombo;

    @UiField
    IPlantAnchor createFilterLink;

    @UiField
    TextField fileQuery;
    
    @Path("fileSizeRange.min")
    @UiField(provided = true)
    NumberField<Double> fileSizeGreaterThan;
    
    @Path("fileSizeRange.max")
    @UiField(provided = true)
    NumberField<Double> fileSizeLessThan;

    @Ignore
    @UiField(provided = true)
    SimpleComboBox<String> greaterThanComboBox;

    @Ignore
    @UiField(provided = true)
    SimpleComboBox<String> lessThanComboBox;

    @UiField
    TextField metadataQuery;

    @Ignore
    @UiField(provided = true)
    SimpleComboBox<String> modifiedWithinCombo;

    @Ignore
    DiskResourceQueryFormNamePrompt namePrompt;


    @UiField
    TextField negatedFileQuery;

    @UiField
    TextField negatedMetadataQuery;


    @UiField 
    TextField sharedWith;

    final SearchFormEditorDriver editorDriver = GWT.create(SearchFormEditorDriver.class);

    private final List<String> fileSizeUnits = Lists.newArrayList("KB", "MB");

    private boolean showing;

    private final List<String> timeIntervals = Lists.newArrayList("---", "1 day", "3 days", "1 week", "2 weeks", "1 month", "2 months", "6 months", "1 year");

    /**
     * Creates the form with a new filter.
     * 
     * @param searchService
     */
    public DiskResourceQueryForm() {
        this(createDefaultFilter());
    }

    /**
     * @param filter
     */
    public DiskResourceQueryForm(final DiskResourceQueryTemplate filter) {
        namePrompt = new DiskResourceQueryFormNamePrompt();
        initProvidedUiFields();
        Widget createAndBindUi = uiBinder.createAndBindUi(this);

        initWidget(createAndBindUi);
        getElement().getStyle().setBackgroundColor("white");
        setSize("330", "-1");
        editorDriver.initialize(this);
        editorDriver.edit(filter);

        eventPreview = new BaseEventPreview() {

            @Override
            protected boolean onPreview(NativePreviewEvent pe) {
                DiskResourceQueryForm.this.onPreviewEvent(pe);
                return super.onPreview(pe);
            }

            @Override
            protected void onPreviewKeyPress(NativePreviewEvent pe) {
                super.onPreviewKeyPress(pe);
                onEscape(pe);
            }

        };
        eventPreview.getIgnoreList().add(getElement());
        eventPreview.setAutoHide(false);
        addStyleName("x-ignore");
    }

    @Override
    public void hide() {
        if (showing) {
            onHide();
            RootPanel.get().remove(this);
            eventPreview.remove();
            showing = false;
            hidden = true;
            fireEvent(new HideEvent());
        }
    }

    public void show(Element parent, AnchorAlignment anchorAlignment) {
        getElement().makePositionable(true);
        RootPanel.get().add(this);
        onShow();
        getElement().updateZIndex(0);

        showing = true;
        // doAutoSize();

        // getElement().setWidth(parent.getOffsetWidth());
        getElement().alignTo(parent, anchorAlignment, new int[] {0, 0});
        // if (enableScrolling) {
        // constrainScroll(getElement().getY());
        // }

        getElement().show();
        eventPreview.add();

        focus();
        fireEvent(new ShowEvent());

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sencha.gxt.widget.core.client.menu.Menu#onHide()
     * 
     * When this container becomes hidden, ensure that save filter container is hidden as well.
     * 
     * Additionally, this will perform any desired animations when this form is hidden.
     */
    @Override
    protected void onHide() {
        namePrompt.hide();
        super.onHide();
    }

    protected void onPreviewEvent(NativePreviewEvent pe) {

        int type = pe.getTypeInt();
        switch (type) {
            case Event.ONMOUSEDOWN:
            case Event.ONMOUSEWHEEL:
            case Event.ONSCROLL:
            case Event.ONKEYPRESS:
                XElement target = pe.getNativeEvent().getEventTarget().cast();

                // ignore targets within a parent with x-ignore, such as the listview in
                // a combo
                if (target.findParent(".x-ignore", 10) != null) {
                    return;
                }

                if (!getElement().isOrHasChild(target) && !namePrompt.getElement().isOrHasChild(target)) {
                    hide();
                    return;
                }
        }
        return;

    }

    @UiFactory
    IPlantAnchor createAnchor() {
        IPlantAnchor anchor = new IPlantAnchor("Create filter with this search...", -1);
        return anchor;
    }

    @UiHandler("createFilterLink")
    void onCreateQueryTemplateClicked(@SuppressWarnings("unused") ClickEvent event) {
        // Flush to perform local validations
        DiskResourceQueryTemplate flushedFilter = editorDriver.flush();
        if (editorDriver.hasErrors()) {
            return;
        }
        showNamePrompt(flushedFilter);
    }


    @UiHandler("searchButton")
    void onSearchBtnSelected(@SuppressWarnings("unused") SelectEvent event) {
        // Flush to perform local validations
        DiskResourceQueryTemplate flushedQueryTemplate = editorDriver.flush();
        if (editorDriver.hasErrors()) {
            return;
        }
        // Fire event and pass flushed query
        fireEvent(new SubmitDiskResourceQueryEvent(flushedQueryTemplate));
        hide();

    }

    void showNamePrompt(DiskResourceQueryTemplate filter) {
        namePrompt.show(filter, getElement(), new AnchorAlignment(Anchor.BOTTOM_LEFT, Anchor.BOTTOM_LEFT, true));
    }

    private void initProvidedUiFields() {
        // Data range combos
        StringLabelProvider<String> stringLabelProvider = new StringLabelProvider<String>();
        createdWithinCombo = new SimpleComboBox<String>(stringLabelProvider);
        modifiedWithinCombo = new SimpleComboBox<String>(stringLabelProvider);
        createdWithinCombo.add(timeIntervals);
        modifiedWithinCombo.add(timeIntervals);
        createdWithinCombo.setValue(timeIntervals.get(0));
        modifiedWithinCombo.setValue(timeIntervals.get(0));
        
        // File Size Number fields
        NumberPropertyEditor.DoublePropertyEditor doublePropertyEditor = new NumberPropertyEditor.DoublePropertyEditor();
        fileSizeGreaterThan = new NumberField<Double>(doublePropertyEditor);
        fileSizeLessThan = new NumberField<Double>(doublePropertyEditor);

        // File Size ComboBoxes
        greaterThanComboBox = new SimpleComboBox<String>(stringLabelProvider);
        lessThanComboBox = new SimpleComboBox<String>(stringLabelProvider);
        greaterThanComboBox.add(fileSizeUnits);
        lessThanComboBox.add(fileSizeUnits);
        greaterThanComboBox.setValue(fileSizeUnits.get(0));
        lessThanComboBox.setValue(fileSizeUnits.get(0));
    }

    private void onEscape(NativePreviewEvent pe) {
        if (pe.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            pe.getNativeEvent().preventDefault();
            pe.getNativeEvent().stopPropagation();
            hide();
        }
    }

}
