/**
 * 
 */
package org.iplantc.core.uidiskresource.client.sharing.views;

import java.util.List;

import org.iplantc.core.uicommons.client.models.collaborators.Collaborator;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.Permissions;
import org.iplantc.core.uidiskresource.client.sharing.models.DataSharing;
import org.iplantc.core.uidiskresource.client.sharing.models.DataSharing.TYPE;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.shared.FastMap;

/**
 * @author sriram
 *
 */
public interface DataSharingView extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {
        void loadCollaborators();

        void loadDiskResources();

        void loadPermissions();

        TYPE getSharingResourceType(String path);

        void addDataSharing(FastMap<DataSharing> smap);

        Permissions getDefaultPermissions();

        void processRequest();
    }

    void addShareWidget(Widget widget);

    void setPresenter(Presenter dataSharingPresenter);

    void setCollaborators(List<Collaborator> models);

    void setSelectedDiskResources(List<DiskResource> models);


}
