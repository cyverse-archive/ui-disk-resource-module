package org.iplantc.core.uidiskresource.client.search.views;

import com.google.common.base.Strings;
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
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
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
 * As user makes changes to the form, a query will be built and submitted to the service. The results
 * will be used to update the form with the number of results for the current query. Will have to handle
 * ValueChanged events within the editor.
 * 
 * TODO Search form should have the ability to be shown at a position relative to a given widget
 * 
 * 
 * @author jstroot
 * 
 */
public class DiskResourceQueryForm extends Menu implements Editor<DiskResourceQueryTemplate> {

    interface SearchFormEditorDriver extends SimpleBeanEditorDriver<DiskResourceQueryTemplate, DiskResourceQueryForm> {}

    @UiTemplate("DiskResourceQueryForm.ui.xml")
    interface DiskResourceQueryFormUiBinder extends UiBinder<Widget, DiskResourceQueryForm> {}

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
    TextField negatedFileQuery;

    @UiField
    TextField negatedMetadataQuery;

    @UiField 
    TextField sharedWith;

    @UiField
    IPlantAnchor createFilterLink;

    @Ignore
    @UiField
    Label saveLabel;

    @Ignore
    @UiField
    TextButton saveFilterBtn;

    @Ignore
    @UiField
    TextButton cancelSaveFilterBtn;


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

    @UiFactory
    IPlantAnchor createAnchor() {
        IPlantAnchor anchor = new IPlantAnchor("Create filter with this search...", -1);
        return anchor;
    }

    @UiHandler("cancelSaveFilterBtn")
    void onCancelSaveFilter(SelectEvent event) {

        cp.getElement().<FxElement> cast().slideOut(Direction.DOWN);
        // cp.getElement().<FxElement> cast().fadeToggle(new Fx(500));
    }

    @UiHandler("saveFilterBtn")
    void onSaveFilterSelected(SelectEvent event) {
        cp.getElement().<FxElement> cast().slideOut(Direction.DOWN);
        // cp.getElement().<FxElement> cast().fadeToggle(new Fx(500));

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
        getElement().<FxElement> cast().fadeToggle(new Fx(500));
        // hide();

        // Transform into query
        // String query = new DataSearchQueryBuilder(flushedQueryTemplate).buildFullQuery();
    }

    @UiHandler("createFilterLink")
    void onCreateQueryTemplateClicked(@SuppressWarnings("unused") ClickEvent event) {
        // Flush to perform local validations
        final DiskResourceQueryTemplate flushedQueryTemplate = editorDriver.flush();
        if (editorDriver.hasErrors()) {
            return;
        }

        if (cp.getElement().getZIndex() < XDOM.getTopZIndex()) {
            cp.getElement().updateZIndex(1);
        }

        if (!cp.isVisible()) {
            // cp.setVisible(true);
            cp.getElement().<FxElement> cast().fadeToggle(new Fx(500));
            // cp.getElement().<FxElement> cast().slideIn(Direction.UP, new Fx(300));
        }
        cp.forceLayout();

        final PromptMessageBox filterNamePrompt = new PromptMessageBox("Save Filter", "Filter name");
        filterNamePrompt.setHideOnButtonClick(false);
        filterNamePrompt.addHideHandler(new HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                if (filterNamePrompt.getHideButton() == filterNamePrompt.getButtonById(PredefinedButton.OK.name())) {
                    // perform OK action

                    TextField textField = filterNamePrompt.getTextField();
                    // If there are no errors
                    if (textField.getErrors().isEmpty() && !Strings.isNullOrEmpty(textField.getCurrentValue())) {
                        flushedQueryTemplate.setName(textField.getCurrentValue());

                        // Fire event and pass flushed query
                        DiskResourceQueryForm.this.fireEvent(new SaveDiskResourceQueryEvent(flushedQueryTemplate));
                        filterNamePrompt.hide();
                        DiskResourceQueryForm.this.hide();
                    }
                } else if (filterNamePrompt.getHideButton() == filterNamePrompt.getButtonById(PredefinedButton.CANCEL.name())) {
                    // perform CANCEL action
                    filterNamePrompt.hide();
                }
            }
        });
        // filterNamePrompt.show();
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

    @Override
    public void focus() {
        super.focus();
        getChildren().get(0).getElement().focus();
    }

    @Override
    protected void onShow() {
        super.onShow();
        Scheduler.get().scheduleFinally(new ScheduledCommand() {

            @Override
            public void execute() {
                // cp.setVisible(false);
                cp.getElement().<FxElement> cast().fadeToggle(new Fx(500));
            }
        });
    }

}
