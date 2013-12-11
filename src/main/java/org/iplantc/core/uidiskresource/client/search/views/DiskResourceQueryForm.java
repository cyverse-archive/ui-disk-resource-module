package org.iplantc.core.uidiskresource.client.search.views;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.sencha.gxt.core.client.Style.Direction;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.fx.client.animation.Fx;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.menu.Menu;

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
public class DiskResourceQueryForm extends Menu implements Editor<DiskResourceQueryTemplate> {

    @UiTemplate("DiskResourceQueryForm.ui.xml")
    interface DiskResourceQueryFormUiBinder extends UiBinder<Widget, DiskResourceQueryForm> {}

    interface SearchFormEditorDriver extends SimpleBeanEditorDriver<DiskResourceQueryTemplate, DiskResourceQueryForm> {}

    private static DiskResourceQueryFormUiBinder uiBinder = GWT.create(DiskResourceQueryFormUiBinder.class);

    private static DiskResourceQueryTemplate createDefaultFilter() {
        SearchAutoBeanFactory factory = SearchAutoBeanFactory.INSTANCE;
        AutoBean<DiskResourceQueryTemplate> dataSearchFilter = factory.dataSearchFilter();
        dataSearchFilter.as().setCreatedWithin(factory.dateInterval().as());
        dataSearchFilter.as().setModifiedWithin(factory.dateInterval().as());
        dataSearchFilter.as().setFileSizeRange(factory.fileSizeRange().as());

        return dataSearchFilter.as();
    }

    // TODO Any ignored field needs to be handled

    @Ignore
    @UiField
    TextButton cancelSaveFilterBtn;

    @Ignore
    @UiField
    VerticalLayoutContainer con;

    @Ignore
    @UiField
    ContentPanel cp;

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

    @UiField
    TextField name;

    @UiField
    TextField negatedFileQuery;

    @UiField
    TextField negatedMetadataQuery;

    @Ignore
    @UiField
    TextButton saveFilterBtn;

    @Ignore
    @UiField
    Label saveLabel;

    @UiField 
    TextField sharedWith;


    private final SearchFormEditorDriver editorDriver = GWT.create(SearchFormEditorDriver.class);

    private final List<String> fileSizeUnits = Lists.newArrayList("KB", "MB");

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
     * Creates the form populated with the given filter.
     * 
     * @param searchService
     * @param filter
     */
    public DiskResourceQueryForm(final DiskResourceQueryTemplate filter) {
        super(new DiskResourceQueryFormMenuAppearance());
        initProvidedUiFields();
        setSize("330", "800");
        Widget createAndBindUi = uiBinder.createAndBindUi(this);
        cp.getElement().getStyle().setPosition(Position.ABSOLUTE);
        cp.setWidth(330);
        cp.getElement().getStyle().setTop(435, Unit.PX);
        saveLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
        saveLabel.getElement().getStyle().setMarginLeft(10, Unit.PX);
        add(createAndBindUi);
        plain = true;
        showSeparator = false;
        setEnableScrolling(false);
        editorDriver.initialize(this);
        editorDriver.edit(filter);
    }

    @Override
    public void focus() {
        super.focus();
        getChildren().get(0).getElement().focus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sencha.gxt.widget.core.client.menu.Menu#onHide()
     * 
     * When this container becomes hidden, ensure that save filter container is hidden as well. This is
     * to ensure that when this container is shown again, the save filter container is hidden by default.
     * 
     * Additionally, this will perform any desired animations when this form is hidden.
     */
    @Override
    protected void onHide() {
        // Ensure that save filter container is hidden
        // hideSaveFilterContainer();

        // Perform hide animations.
        getElement().<FxElement> cast().fadeToggle(new Fx(300));
        super.onHide();
    }

    @Override
    protected void onShow() {
        super.onShow();
        Scheduler.get().scheduleFinally(new ScheduledCommand() {

            @Override
            public void execute() {
                if (cp.isVisible()) {
                    // Ensure that save filter container is hidden on initial show.
                    hideSaveFilterContainerFast();
                    // cp.getElement().<FxElement> cast().fadeToggle(new Fx(300));
                    // cp.setVisible(false);
                }
            }
        });
    }

    @UiFactory
    IPlantAnchor createAnchor() {
        IPlantAnchor anchor = new IPlantAnchor("Create filter with this search...", -1);
        return anchor;
    }

    @UiHandler("cancelSaveFilterBtn")
    void onCancelSaveFilter(@SuppressWarnings("unused") SelectEvent event) {
        // Hide the filter name panel
        hideSaveFilterContainer();

        // Set the filter name field to allow blank values when hidden.
        // TODO JDS Verify that this is necessary. Validations may not be performed on hidden fields.
        name.setAllowBlank(true);
    }

    @UiHandler("createFilterLink")
    void onCreateQueryTemplateClicked(@SuppressWarnings("unused") ClickEvent event) {
        /*
         * Name field cannot be blank, add empty validator. This validator must be removed when the
         * filter name panel is hidden
         */

        name.setAllowBlank(true);
        // Flush to perform local validations
        editorDriver.flush();
        if (editorDriver.hasErrors()) {
            return;
        }
        name.setAllowBlank(false);

        if (cp.getElement().getZIndex() < XDOM.getTopZIndex()) {
            cp.getElement().updateZIndex(1);
        }

        if (!cp.isVisible()) {
            // cp.setVisible(true);
            // cp.getElement().<FxElement> cast().slideIn(Direction.UP);
            cp.getElement().<FxElement> cast().fadeToggle(new Fx(500));
        }
        cp.forceLayout();

    }

    @UiHandler("saveFilterBtn")
    void onSaveFilterSelected(@SuppressWarnings("unused") SelectEvent event) {
        final DiskResourceQueryTemplate flushedQueryTemplate = editorDriver.flush();
        if (editorDriver.hasErrors()) {
            return;
        }

        // Set the filter name field to allow blank values when hidden.
        // TODO JDS Verify that this is necessary. Validations may not be performed on hidden fields.
        name.setAllowBlank(true);

        fireEvent(new SaveDiskResourceQueryEvent(flushedQueryTemplate));
        hide();

    }

    @UiHandler("searchButton")
    void onSearchBtnSelected(@SuppressWarnings("unused") SelectEvent event) {
        name.setAllowBlank(true);
        // Flush to perform local validations
        DiskResourceQueryTemplate flushedQueryTemplate = editorDriver.flush();
        if (editorDriver.hasErrors()) {
            return;
        }
        // Fire event and pass flushed query
        hide();

        fireEvent(new SubmitDiskResourceQueryEvent(flushedQueryTemplate));
    }

    private void hideSaveFilterContainer() {
        cp.getElement().<FxElement> cast().slideOut(Direction.DOWN);
    }

    private void hideSaveFilterContainerFast() {
        cp.getElement().<FxElement> cast().slideOut(Direction.DOWN, new Fx(10));
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

}
