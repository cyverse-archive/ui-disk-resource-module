package org.iplantc.core.uidiskresource.client.metadata.view;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.iplantc.core.resources.client.IplantResources;
import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceAutoBeanFactory;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceMetadata;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceMetadataList;
import org.iplantc.core.uicommons.client.models.diskresources.MetadataTemplateAttribute;
import org.iplantc.core.uicommons.client.models.diskresources.MetadataTemplateInfo;
import org.iplantc.core.uicommons.client.validators.UrlValidator;
import org.iplantc.core.uidiskresource.client.models.DiskResourceMetadataProperties;
import org.iplantc.core.uidiskresource.client.services.callbacks.DiskResourceMetadataUpdateCallback;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ValidEvent;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.IntegerPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class DiskResourceMetadataView implements IsWidget {

	public interface Presenter extends
			org.iplantc.core.uicommons.client.presenter.Presenter {
		/**
		 * Retrieves a collection of metadata for the given resource.
		 *
		 * @param resource
		 * @param callback
		 * @return a collection of the given resource's metadata.
		 */
		void getDiskResourceMetadata(AsyncCallback<String> callback);

		void setDiskResourceMetaData(
				Set<DiskResourceMetadata> metadataToAdd,
				Set<DiskResourceMetadata> metadataToDelete,
				DiskResourceMetadataUpdateCallback diskResourceMetadataUpdateCallback);

		DiskResource getSelectedResource();

		void getTemplates();

		void onTemplateSelected(String templateId);

	}

    private static final String IPC_METADATA_TEMPLATE_ATTR = "ipc-metadata-template"; //$NON-NLS-1$
    private static final String USER_UNIT_TAG = "ipc_user_unit_tag"; //$NON-NLS-1$

	@UiTemplate("DiskResourceMetadataEditorPanel.ui.xml")
	interface DiskResourceMetadataEditorPanelUiBinder extends
			UiBinder<Widget, DiskResourceMetadataView> {
	}

	private static DiskResourceMetadataEditorPanelUiBinder uiBinder = GWT
			.create(DiskResourceMetadataEditorPanelUiBinder.class);

	@UiField
	BorderLayoutContainer con;

	@UiField
	ToolBar toolbar;

	@UiField
	TextButton addMetadataButton;

	@UiField
	TextButton deleteMetadataButton;

	private Grid<DiskResourceMetadata> grid;

	private ListStore<DiskResourceMetadata> listStore;

	@UiField
	ComboBox<MetadataTemplateInfo> templateCombo;

	private VerticalLayoutContainer templateContainer;

	private ContentPanel templateForm;

	private int attrib_counter;

	private final Widget widget;

	private final Set<DiskResourceMetadata> toBeDeleted = Sets.newHashSet();

	private GridInlineEditing<DiskResourceMetadata> gridInlineEditing;

	private MetadataCell metadataCell;

	private final DiskResourceAutoBeanFactory autoBeanFactory = GWT
			.create(DiskResourceAutoBeanFactory.class);

	private Presenter presenter;

	private boolean valid;

	private ListStore<MetadataTemplateInfo> templateStore;

	private final AccordionLayoutContainer alc;

	private final AccordionLayoutAppearance appearance;

	private ContentPanel userMetadataPanel;

	private final VerticalLayoutContainer centerPanel;

    private final FastMap<Field<?>> templateAttrFieldMap = new FastMap<Field<?>>();

    private final FastMap<DiskResourceMetadata> attrAvuMap = new FastMap<DiskResourceMetadata>();

	public DiskResourceMetadataView(DiskResource dr) {
		widget = uiBinder.createAndBindUi(this);
		alc = new AccordionLayoutContainer();
		centerPanel = new VerticalLayoutContainer();
		con.setCenterWidget(centerPanel);
		appearance = GWT
				.<AccordionLayoutAppearance> create(AccordionLayoutAppearance.class);
		initGridEditing();
		grid.getSelectionModel().addSelectionChangedHandler(
				new MetadataSelectionChangedListener(deleteMetadataButton, grid
						.getSelectionModel(), gridInlineEditing));
		addMetadataButton.setEnabled(dr.getPermissions().isWritable());
		deleteMetadataButton.setEnabled(dr.getPermissions().isWritable());
	}

	public void setPresenter(Presenter p) {
		this.presenter = p;
	}

	@UiFactory
	ComboBox<MetadataTemplateInfo> buildTemplateCombo() {
		templateStore = new ListStore<MetadataTemplateInfo>(
				new ModelKeyProvider<MetadataTemplateInfo>() {

					@Override
					public String getKey(MetadataTemplateInfo item) {
						return item.getId();
					}
				});

		templateCombo = new ComboBox<MetadataTemplateInfo>(templateStore,
				new LabelProvider<MetadataTemplateInfo>() {

					@Override
					public String getLabel(MetadataTemplateInfo item) {
						return item.getName();
					}
				});
		templateCombo.setEmptyText("Select a template...");
		templateCombo.setTypeAhead(true);
		templateCombo.addSelectionHandler(new SelectionHandler<MetadataTemplateInfo>() {

			@Override
			public void onSelection(SelectionEvent<MetadataTemplateInfo> event) {
				onTemplateSelected(event.getSelectedItem());
			}
		});
		return templateCombo;
	}

    private void onTemplateSelected(MetadataTemplateInfo templateInfo) {
        presenter.onTemplateSelected(templateInfo.getId());
        buildTemplateContainer();
        alc.mask();
    }

    public void loadTemplateAttributes(List<MetadataTemplateAttribute> attributes) {
        templateAttrFieldMap.clear();
		TextButton rmvBtn = buildRemoveTemplateButton();
		templateContainer.add(rmvBtn,new VerticalLayoutData(1, -1));
		for (MetadataTemplateAttribute attribute : attributes) {
            Field<?> field = getAttributeValueWidget(attribute);
            if (field != null) {
                AttributeValidationHandler validationHandler = new AttributeValidationHandler();
                field.addValidHandler(validationHandler);
                field.addInvalidHandler(validationHandler);
                templateAttrFieldMap.put(attribute.getName(), field);
				templateContainer.add(
                        buildFieldLabel(field, attribute.getName(), attribute.getDescription(),
								!attribute.isRequired()),
						new VerticalLayoutData(1, -1));
			}
		}
		alc.forceLayout();
		alc.unmask();
	}

	private TextButton buildRemoveTemplateButton() {
		TextButton removeBtn = new TextButton(I18N.DISPLAY.remove(),IplantResources.RESOURCES.deleteIcon());

		return removeBtn;
	}

    private TextField buildTextField(MetadataTemplateAttribute attribute) {
		TextField fld = new TextField();
        fld.setAllowBlank(!attribute.isRequired());

        DiskResourceMetadata avu = attrAvuMap.get(attribute.getName());
        if (avu != null) {
            fld.setValue(avu.getValue());
        }

        return fld;
	}

	private NumberField<Integer> buildIntegerField(MetadataTemplateAttribute attribute) {
        NumberField<Integer> nf = new NumberField<Integer>(new IntegerPropertyEditor());
        nf.setAllowBlank(!attribute.isRequired());
		nf.setAllowDecimals(false);
		nf.setAllowNegative(true);

        DiskResourceMetadata avu = attrAvuMap.get(attribute.getName());
        if (avu != null) {
            nf.setValue(new Integer(avu.getValue()));
        }

        return nf;
	}

    private NumberField<Double> buildNumberField(MetadataTemplateAttribute attribute) {
        NumberField<Double> nf = new NumberField<Double>(new DoublePropertyEditor());
        nf.setAllowBlank(!attribute.isRequired());
		nf.setAllowDecimals(true);
		nf.setAllowNegative(true);

        DiskResourceMetadata avu = attrAvuMap.get(attribute.getName());
        if (avu != null) {
            nf.setValue(new Double(avu.getValue()));
        }

		return nf;
	}

	private FieldLabel buildFieldLabel(IsWidget widget, String lbl, String description,
			boolean allowBlank) {
		FieldLabel fl = new FieldLabel(widget);

		if (!allowBlank) {
			fl.setHTML(buildRequiredFieldLabel(buildLabelWithDescription(lbl, description)));
		} else {
			fl.setHTML(buildLabelWithDescription(lbl, description));
		}
		fl.setLabelAlign(LabelAlign.TOP);
		return fl;
	}

    private TextArea buildTextArea(MetadataTemplateAttribute attribute) {
		TextArea area = new TextArea();
        area.setAllowBlank(!attribute.isRequired());
		area.setHeight(200);

        DiskResourceMetadata avu = attrAvuMap.get(attribute.getName());
        if (avu != null) {
            area.setValue(avu.getValue());
        }

		return area;
	}

    private CheckBox buildBooleanField(MetadataTemplateAttribute attribute) {
		CheckBox cb = new CheckBox();

        DiskResourceMetadata avu = attrAvuMap.get(attribute.getName());
        if (avu != null) {
            cb.setValue(new Boolean(avu.getValue()));
        }

		return cb;
	}

    private TextField buildURLField(MetadataTemplateAttribute attribute) {
        TextField tf = buildTextField(attribute);
		tf.addValidator(new UrlValidator());
		return tf;
	}

    private TextField buildDateField(MetadataTemplateAttribute attribute) {
        final DateTimeFormat format = DateTimeFormat
                .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT);
        final TextField tf = buildTextField(attribute);
        tf.setEmptyText(format.format(new Date()));
        tf.addValidator(new Validator<String>() {

            @Override
            public List<EditorError> validate(Editor<String> editor, String value) {
                try {
                    tf.clearInvalid();
                    format.parse(value);
                    return null;
                } catch (Exception e) {
                    GWT.log(value, e);
                    EditorError dee = new DefaultEditorError(editor, "Invalid date / time. Please use: "
                            + format.getPattern(), value);
                    return Arrays.asList(dee);
                }

            }
        });

        DiskResourceMetadata avu = attrAvuMap.get(attribute.getName());
        if (avu != null) {
            tf.setValue(avu.getValue());
        }

		return tf;
	}

    /**
     * @param attribute
     * @return Field based on MetadataTemplateAttribute type.
     */
    private Field<?> getAttributeValueWidget(MetadataTemplateAttribute attribute) {
        String type = attribute.getType();
        if (type.equalsIgnoreCase("timestamp")) { //$NON-NLS-1$
            return buildDateField(attribute);
        } else if (type.equalsIgnoreCase("boolean")) { //$NON-NLS-1$
            return buildBooleanField(attribute);
        } else if (type.equalsIgnoreCase("number")) { //$NON-NLS-1$
            return buildNumberField(attribute);
        } else if (type.equalsIgnoreCase("integer")) { //$NON-NLS-1$
            return buildIntegerField(attribute);
        } else if (type.equalsIgnoreCase("string")) { //$NON-NLS-1$
            return buildTextField(attribute);
        } else if (type.equalsIgnoreCase("multiline text")) { //$NON-NLS-1$
            return buildTextArea(attribute);
        } else if (type.equalsIgnoreCase("URL/URI")) { //$NON-NLS-1$
            return buildURLField(attribute);
        } else {
            return null;
        }
    }

	private String buildLabelWithDescription(final String label, final String description) {
		return "<span title='" + description + "' style='pointer:cursor;'>" + label + "</span>";
	}

	private String buildRequiredFieldLabel(final String label) {
		if (label == null) {
			return null;
		}

		return "<span style='color:red; top:-5px;' >*</span> " + label; //$NON-NLS-1$
	}

	public void populateTemplates(List<MetadataTemplateInfo> templates) {
		templateStore.clear();
		templateStore.addAll(templates);
	}

	private void initGridEditing() {
		buildUserMetadataPanel();
		grid = new Grid<DiskResourceMetadata>(createListStore(),
				createColumnModel());
		userMetadataPanel.add(grid);
		centerPanel.add(userMetadataPanel);

		gridInlineEditing = new GridInlineEditing<DiskResourceMetadata>(grid);
		gridInlineEditing.setClicksToEdit(ClicksToEdit.TWO);
		ColumnConfig<DiskResourceMetadata, String> column1 = grid
				.getColumnModel().getColumn(0);
		ColumnConfig<DiskResourceMetadata, String> column2 = grid
				.getColumnModel().getColumn(1);

		TextField field1 = new TextField();
		TextField field2 = new TextField();

		field1.setAutoValidate(true);
		field2.setAutoValidate(true);

		field1.setAllowBlank(false);
		field2.setAllowBlank(false);

		AttributeValidationHandler validationHandler = new AttributeValidationHandler();
		field1.addInvalidHandler(validationHandler);
		field1.addValidHandler(validationHandler);

		gridInlineEditing.addEditor(column1, field1);
		gridInlineEditing.addEditor(column2, field2);
		gridInlineEditing
				.addCompleteEditHandler(new CompleteEditHandler<DiskResourceMetadata>() {

					@Override
					public void onCompleteEdit(
							CompleteEditEvent<DiskResourceMetadata> event) {
						listStore.commitChanges();
					}
				});

	}

	private void buildUserMetadataPanel() {
		userMetadataPanel = new ContentPanel(appearance);
		userMetadataPanel.setSize("575","370");
		userMetadataPanel.setCollapsible(true);
		userMetadataPanel.getHeader().addStyleName(ThemeStyles.getStyle().borderTop());

		userMetadataPanel.setHeadingText("User Metadata");
	}

	private ListStore<DiskResourceMetadata> createListStore() {
		listStore = new ListStore<DiskResourceMetadata>(
				new ModelKeyProvider<DiskResourceMetadata>() {

					@Override
					public String getKey(DiskResourceMetadata item) {
						return item.getAttribute() + "-" + item.getValue()
								+ "-" + item.getUnit();
					}
				});

		return listStore;
	}

	@UiHandler("deleteMetadataButton")
	void onDeleteMetadataSelected(SelectEvent event) {
		for (DiskResourceMetadata md : grid.getSelectionModel()
				.getSelectedItems()) {
			toBeDeleted.add(md);
			listStore.remove(md);
		}
	}

	@UiHandler("addMetadataButton")
	void onAddMetadataSelected(SelectEvent event) {
		DiskResourceMetadata md = autoBeanFactory.metadata().as();
		md.setAttribute("New Attribute " + attrib_counter++);
		md.setValue("New Value");
		md.setUnit(USER_UNIT_TAG);
		listStore.add(0, md);
		gridInlineEditing.startEditing(new GridCell(0, 0));
		gridInlineEditing.getEditor(grid.getColumnModel().getColumn(0))
				.validate();
	}

	ColumnModel<MetadataTemplateAttribute> createTemplateColumnModel() {
		List<ColumnConfig<MetadataTemplateAttribute, ?>> columns = Lists
				.newArrayList();

		ColumnModel<MetadataTemplateAttribute> cm = new ColumnModel<MetadataTemplateAttribute>(
				columns);
		return cm;
	}

	private ColumnModel<DiskResourceMetadata> createColumnModel() {
		List<ColumnConfig<DiskResourceMetadata, ?>> columns = Lists
				.newArrayList();
		DiskResourceMetadataProperties props = GWT
				.create(DiskResourceMetadataProperties.class);
		ColumnConfig<DiskResourceMetadata, String> attributeColumn = new ColumnConfig<DiskResourceMetadata, String>(
				props.attribute(), 150, "Attribute");
		ColumnConfig<DiskResourceMetadata, String> valueColumn = new ColumnConfig<DiskResourceMetadata, String>(
				props.value(), 150, "Value");

		metadataCell = new MetadataCell();
		attributeColumn.setCell(metadataCell);
		valueColumn.setCell(metadataCell);
		columns.add(attributeColumn);
		columns.add(valueColumn);

		ColumnModel<DiskResourceMetadata> cm = new ColumnModel<DiskResourceMetadata>(
				columns);
		return cm;
	}

	public Set<DiskResourceMetadata> getMetadataToDelete() {
		return toBeDeleted;
	}

	public Set<DiskResourceMetadata> getMetadataToAdd() {
        HashSet<DiskResourceMetadata> metaDataToAdd = Sets.newHashSet();

        for (String attr : templateAttrFieldMap.keySet()) {
            Field<?> field = templateAttrFieldMap.get(attr);
            if (field.isValid() && field.getValue() != null && !field.getValue().toString().isEmpty()) {
                DiskResourceMetadata avu = autoBeanFactory.metadata().as();
                avu.setAttribute(attr);
                avu.setValue(field.getValue().toString());
                avu.setUnit(""); //$NON-NLS-1$

                metaDataToAdd.add(avu);
                listStore.remove(attrAvuMap.get(attr));
            }
        }

        metaDataToAdd.addAll(listStore.getAll());

        return metaDataToAdd;
	}

	private final class MetadataCell extends AbstractCell<String> {

		@Override
		public void render(Context context, String value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}

			sb.append(SafeHtmlUtils.fromSafeConstant("<span title='" + value
					+ "'>"));
			sb.append(SafeHtmlUtils.fromString(value));
			sb.append(SafeHtmlUtils.fromSafeConstant("</span>"));
		}
	}

	private final class MetadataSelectionChangedListener implements
			SelectionChangedHandler<DiskResourceMetadata> {

		private final TextButton deleteButton;
		private final GridSelectionModel<DiskResourceMetadata> sm;
		private final GridInlineEditing<DiskResourceMetadata> editing;

		public MetadataSelectionChangedListener(final TextButton deleteButton,
				final GridSelectionModel<DiskResourceMetadata> sm,
				final GridInlineEditing<DiskResourceMetadata> editing) {
			this.deleteButton = deleteButton;
			this.sm = sm;
			this.editing = editing;
		}

		@Override
		public void onSelectionChanged(
				SelectionChangedEvent<DiskResourceMetadata> event) {
			deleteButton.setEnabled(!sm.getSelectedItems().isEmpty());
			editing.completeEditing();
		}
	}

	private final class AttributeValidationHandler implements ValidHandler,
			InvalidHandler {

		public AttributeValidationHandler() {
		}

		@Override
		public void onValid(ValidEvent event) {
			valid = true;
		}

		@Override
		public void onInvalid(InvalidEvent event) {
			valid = false;
		}
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void loadMetadata(DiskResourceMetadataList metadataList) {
        List<DiskResourceMetadata> metadata = metadataList.getMetadata();

        listStore.clear();
        listStore.addAll(metadata);

        MetadataTemplateInfo selectedTemplateInfo = null;
        attrAvuMap.clear();
        for (DiskResourceMetadata avu : metadata) {
            String attribute = avu.getAttribute();
            attrAvuMap.put(attribute, avu);

            if (attribute.equalsIgnoreCase(IPC_METADATA_TEMPLATE_ATTR)) {
                selectedTemplateInfo = templateStore.findModelWithKey(avu.getValue());
            }
        }

        if (selectedTemplateInfo != null) {
            templateCombo.setValue(selectedTemplateInfo);
            onTemplateSelected(selectedTemplateInfo);
        }
	}

	public boolean isValid() {
		return valid;
	}

	private void buildTemplateContainer() {
		centerPanel.clear();
		alc.clear();
		alc.setExpandMode(ExpandMode.SINGLE);
		buildTemplatePanel();
		buildUserMetadataPanel();
		//must re add the grid
		userMetadataPanel.add(grid);
		alc.add(templateForm);
		alc.add(userMetadataPanel);
        alc.setActiveWidget(templateForm);
		centerPanel.add(alc);

	}

	private void buildTemplatePanel() {
		templateForm = new ContentPanel(appearance);
		templateForm.setSize("575","370");
		templateForm.setHeadingText(templateCombo.getCurrentValue().getName());
		templateForm.getHeader().addStyleName(ThemeStyles.getStyle().borderTop());
		templateContainer = new VerticalLayoutContainer();
		templateContainer.setScrollMode(ScrollMode.AUTOY);
		templateForm.add(templateContainer);
		//need this to be set manually to avoid renderer assertion error
		templateForm.setCollapsible(true);
		//end temp fix
	}

}
