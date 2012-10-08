package org.iplantc.core.uidiskresource.client.views.widgets;

import com.google.gwt.user.client.ui.IsWidget;

public interface DiskResourceViewToolbar extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {

    }

    void setPresenter(Presenter presenter);
}
