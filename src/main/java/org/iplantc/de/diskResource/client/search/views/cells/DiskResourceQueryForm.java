package org.iplantc.de.diskResource.client.search.views.cells;

import org.iplantc.de.client.models.search.DateInterval;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.search.FileSizeRange.FileSizeUnit;
import org.iplantc.de.client.models.search.SearchAutoBeanFactory;
import org.iplantc.de.client.models.search.SearchModelUtils;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.widgets.IPlantAnchor;
import org.iplantc.de.diskResource.client.search.events.SaveDiskResourceQueryEvent;
import org.iplantc.de.diskResource.client.search.events.SubmitDiskResourceQueryEvent;
import org.iplantc.de.diskResource.client.search.events.SubmitDiskResourceQueryEvent.HasSubmitDiskResourceQueryEventHandlers;
import org.iplantc.de.diskResource.client.search.events.SubmitDiskResourceQueryEvent.SubmitDiskResourceQueryEventHandler;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
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

import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;

import java.util.Date;
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
 * clicks "Save", a {@link org.iplantc.de.diskResource.client.search.events.SaveDiskResourceQueryEvent} will be fired with the form's current query
 * template and this form will be hidden.</li>
 * </ol>
 * </li>
 * </ol>
 * 
 * 
 * @author jstroot
 * 
 */
public class DiskResourceQueryForm extends Composite implements Editor<DiskResourceQueryTemplate>, SaveDiskResourceQueryEvent.HasSaveDiskResourceQueryEventHandlers, HasSubmitDiskResourceQueryEventHandlers, SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler {

    @UiTemplate("DiskResourceQueryForm.ui.xml")
    interface DiskResourceQueryFormUiBinder extends UiBinder<Widget, DiskResourceQueryForm> {}

    interface SearchFormEditorDriver extends SimpleBeanEditorDriver<DiskResourceQueryTemplate, DiskResourceQueryForm> {}

    protected BaseEventPreview eventPreview;

    @Ignore
    @UiField
    VerticalLayoutContainer con;

    @UiField
    TextField ownedBy;

    @Path("createdWithin")
    @UiField(provided = true)
    SimpleComboBox<DateInterval> createdWithinCombo;

    @UiField
    IPlantAnchor createFilterLink;

    final SearchFormEditorDriver editorDriver = GWT.create(SearchFormEditorDriver.class);

    @UiField
    TextField fileQuery;
    
    @Path("fileSizeRange.min")
    @UiField(provided = true)
    NumberField<Double> fileSizeGreaterThan;
    
    @Path("fileSizeRange.max")
    @UiField(provided = true)
    NumberField<Double> fileSizeLessThan;

    @Path("fileSizeRange.minUnit")
    @UiField(provided = true)
    SimpleComboBox<FileSizeUnit> greaterThanComboBox;

    @Path("fileSizeRange.maxUnit")
    @UiField(provided = true)
    SimpleComboBox<FileSizeUnit> lessThanComboBox;

    @UiField
    TextField metadataAttributeQuery;

    @Path("modifiedWithin")
    @UiField(provided = true)
    SimpleComboBox<DateInterval> modifiedWithinCombo;

    @Ignore
    DiskResourceQueryFormNamePrompt namePrompt;

    @UiField
    TextField negatedFileQuery;

    @UiField
    TextField metadataValueQuery;

    @UiField 
    TextField sharedWith;

    @UiField
    CheckBox includeTrashItems;

    private boolean showing;

    private final DiskResourceQueryFormUiBinder uiBinder = GWT.create(DiskResourceQueryFormUiBinder.class);

    private final SearchAutoBeanFactory factory = GWT.create(SearchAutoBeanFactory.class);

    /**
     * Creates the form with a new filter.
     * 
     * @param searchService
     */
    public DiskResourceQueryForm() {
        this(SearchModelUtils.createDefaultFilter());
    }

    /**
     * @param filter
     */
    public DiskResourceQueryForm(final DiskResourceQueryTemplate filter) {
        init(new DiskResourceQueryFormNamePrompt());
        Widget createAndBindUi = uiBinder.createAndBindUi(this);

        initWidget(createAndBindUi);
        getElement().getStyle().setBackgroundColor("white");
        getElement().getStyle().setOutlineWidth(0, Unit.PX);
        getElement().getStyle().setPaddingTop(5, Unit.PX);
        getElement().getStyle().setPaddingBottom(5, Unit.PX);
        setSize("330", "250");
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
        con.setScrollMode(ScrollMode.AUTOY);
        con.setBorders(true);
         // JDS Small trial to correct placement of form in constrained views.
        this.ensureVisibilityOnSizing = true;
    }

    @Override
    public HandlerRegistration addSaveDiskResourceQueryEventHandler(SaveDiskResourceQueryEvent.SaveDiskResourceQueryEventHandler handler) {
        return addHandler(handler, SaveDiskResourceQueryEvent.TYPE);
    }

    @Override
    public HandlerRegistration addSubmitDiskResourceQueryEventHandler(SubmitDiskResourceQueryEventHandler handler) {
        return addHandler(handler, SubmitDiskResourceQueryEvent.TYPE);
    }

    /**
     * Clears search form by binding it to a new default query template
     */
    public void clearSearch() {
        editorDriver.edit(SearchModelUtils.createDefaultFilter());
    }

    @Override
    public void doSaveDiskResourceQueryTemplate(SaveDiskResourceQueryEvent event) {
        // Re-fire event
        fireEvent(event);
    }

    public void edit(DiskResourceQueryTemplate queryTemplate) {
        editorDriver.edit(queryTemplate);
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

        getElement().alignTo(parent, anchorAlignment, new int[] {0, 0});

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

    void init(DiskResourceQueryFormNamePrompt namePrompt) {
        this.namePrompt = namePrompt;
        this.namePrompt.addSaveDiskResourceQueryEventHandler(this);
        initDateRangeCombos();
        initFileSizeNumberFields();
        initFileSizeComboBoxes();
    }

    @UiHandler("createFilterLink")
    void onCreateQueryTemplateClicked(ClickEvent event) {
        // Flush to perform local validations
        DiskResourceQueryTemplate flushedFilter = editorDriver.flush();
        if (editorDriver.hasErrors()) {
            return;
        }
        showNamePrompt(flushedFilter);
    }

    @UiHandler("searchButton")
    void onSearchBtnSelected(SelectEvent event) {
        // Flush to perform local validations
        DiskResourceQueryTemplate flushedQueryTemplate = editorDriver.flush();
        if (editorDriver.hasErrors() || isEmptyQuery(flushedQueryTemplate)) {
            return;
        }

        // Fire event and pass flushed query
        fireEvent(new SubmitDiskResourceQueryEvent(flushedQueryTemplate));
        hide();
    }

    static boolean isEmptyQuery(DiskResourceQueryTemplate template){
        if (Strings.isNullOrEmpty(template.getOwnedBy())
                && Strings.isNullOrEmpty(template.getFileQuery())
                && Strings.isNullOrEmpty(template.getMetadataAttributeQuery())
                && Strings.isNullOrEmpty(template.getMetadataValueQuery())
                && Strings.isNullOrEmpty(template.getNegatedFileQuery())
                && Strings.isNullOrEmpty(template.getSharedWith())
                && (template.getDateCreated() == null)
                && (template.getLastModified() == null)
                && ((template.getCreatedWithin() == null) || (template.getCreatedWithin().getFrom() == null && template.getCreatedWithin().getTo() == null))
                && ((template.getModifiedWithin() == null) || (template.getModifiedWithin().getFrom() == null && template.getModifiedWithin().getTo() == null))
                && ((template.getFileSizeRange() == null) || (template.getFileSizeRange().getMax() == null && template.getFileSizeRange().getMin() == null))){
            // TODO Implement user error feedback
            IplantAnnouncer.getInstance().schedule(new ErrorAnnouncementConfig("You must select at least one filter."));
            return true;
        }
        return false;
    }

    void showNamePrompt(DiskResourceQueryTemplate filter) {
        namePrompt.show(filter, getElement(), new AnchorAlignment(Anchor.BOTTOM_LEFT, Anchor.BOTTOM_LEFT, true));
    }

    private void initDateRangeCombos() {
        List<DateInterval> timeIntervals = Lists.newArrayList();
        Date now = new Date();

        DateInterval interval = createDateInterval(null, null, "---");
        timeIntervals.add(interval);

        final DateWrapper dateWrapper = new DateWrapper(now).clearTime();
        interval = createDateInterval(dateWrapper.addDays(-1).asDate(), now, "1 day");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addDays(-3).asDate(), now, "3 days");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addDays(-7).asDate(), now, "1 week");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addDays(-14).asDate(), now, "2 weeks");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addMonths(-1).asDate(), now, "1 month");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addMonths(-2).asDate(), now, "2 months");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addMonths(-6).asDate(), now, "6 months");
        timeIntervals.add(interval);

        interval = createDateInterval(dateWrapper.addYears(-1).asDate(), now, "1 year");
        timeIntervals.add(interval);

        // Data range combos
        LabelProvider<DateInterval> dateIntervalLabelProvider = new LabelProvider<DateInterval>() {

            @Override
            public String getLabel(DateInterval item) {
                return item.getLabel();
            }
        };
        createdWithinCombo = new SimpleComboBox<DateInterval>(dateIntervalLabelProvider);
        modifiedWithinCombo = new SimpleComboBox<DateInterval>(dateIntervalLabelProvider);
        createdWithinCombo.add(timeIntervals);
        modifiedWithinCombo.add(timeIntervals);
        createdWithinCombo.setEmptyText("---");
        modifiedWithinCombo.setEmptyText("---");
    }

    DateInterval createDateInterval(Date from, Date to, String label) {
        DateInterval ret = factory.dateInterval().as();
        ret.setFrom(from);
        ret.setTo(to);
        ret.setLabel(label);
        return ret;
    }

    private void initFileSizeComboBoxes() {
        // File Size ComboBoxes
        LabelProvider<FileSizeUnit> fileSizeUnitLabelProvider = new LabelProvider<FileSizeUnit>() {

            @Override
            public String getLabel(FileSizeUnit item) {
                return item.getLabel();
            }

        };
        greaterThanComboBox = new SimpleComboBox<FileSizeUnit>(fileSizeUnitLabelProvider);
        lessThanComboBox = new SimpleComboBox<FileSizeUnit>(fileSizeUnitLabelProvider);

        List<FileSizeUnit> fileSizeUnitList = createFileSizeUnits();
        greaterThanComboBox.add(fileSizeUnitList);
        lessThanComboBox.add(fileSizeUnitList);
    }

    List<FileSizeUnit> createFileSizeUnits() {
        return SearchModelUtils.createFileSizeUnits();
    }

    private void initFileSizeNumberFields() {
        // File Size Number fields
        NumberPropertyEditor.DoublePropertyEditor doublePropertyEditor = new NumberPropertyEditor.DoublePropertyEditor();
        fileSizeGreaterThan = new NumberField<Double>(doublePropertyEditor);
        fileSizeLessThan = new NumberField<Double>(doublePropertyEditor);
        fileSizeGreaterThan.setAllowNegative(false);
        fileSizeLessThan.setAllowNegative(false);
    }

    private void onEscape(NativePreviewEvent pe) {
        if (pe.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            pe.getNativeEvent().preventDefault();
            pe.getNativeEvent().stopPropagation();
            hide();
        }
    }

}
