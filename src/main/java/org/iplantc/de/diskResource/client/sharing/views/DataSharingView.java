/**
 * 
 */
package org.iplantc.de.diskResource.client.sharing.views;

import java.util.List;

import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;
import org.iplantc.core.uicommons.client.models.diskresources.Permissions;
import org.iplantc.de.diskResource.client.sharing.models.DataSharing.TYPE;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author sriram
 *
 */
public interface DataSharingView extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {

        void loadDiskResources();

        void loadPermissions();

        TYPE getSharingResourceType(String path);

        Permissions getDefaultPermissions();

        void processRequest();

		List<DiskResource> getSelectedResources();
    }

    void addShareWidget(Widget widget);

    void setPresenter(Presenter dataSharingPresenter);

    void setSelectedDiskResources(List<DiskResource> models);


}
