package org.iplantc.core.uidiskresource.client.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import com.sencha.gxt.widget.core.client.Composite;

public class SaveQueryTemplateForm extends Composite {

    private static SaveQueryTemplateFormUiBinder uiBinder = GWT.create(SaveQueryTemplateFormUiBinder.class);

    interface SaveQueryTemplateFormUiBinder extends UiBinder<Widget, SaveQueryTemplateForm> {}

    public SaveQueryTemplateForm() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
