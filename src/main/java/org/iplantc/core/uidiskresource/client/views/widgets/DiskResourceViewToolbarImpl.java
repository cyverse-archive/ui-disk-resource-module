package org.iplantc.core.uidiskresource.client.views.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

public class DiskResourceViewToolbarImpl implements DiskResourceViewToolbar {

    private static DiskResourceViewToolbarUiBinder uiBinder = GWT
            .create(DiskResourceViewToolbarUiBinder.class);

    @UiTemplate("DiskResourceViewToolbar.ui.xml")
    interface DiskResourceViewToolbarUiBinder extends UiBinder<Widget, DiskResourceViewToolbarImpl> {
    }

    private Presenter presenter;
    private final Widget widget;

    public DiskResourceViewToolbarImpl() {
        widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

}
