package org.iplantc.core.uidiskresource.client.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uidiskresource.client.I18N;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceProperties;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

public class DiskResourceColumnModel extends ColumnModel<DiskResource> {

    public DiskResourceColumnModel() {
        super(createColumnConfigList());
    }

    public static List<ColumnConfig<DiskResource, ?>> createColumnConfigList() {
        List<ColumnConfig<DiskResource, ?>> list = new ArrayList<ColumnConfig<DiskResource, ?>>();

        DiskResourceProperties props = GWT.create(DiskResourceProperties.class);

        ColumnConfig<DiskResource, String> name = new ColumnConfig<DiskResource, String>(props.name(),
                130, I18N.DISPLAY.name());

        list.add(name);

        // TODO JDS TO BE IMPLEMENTED
        return list;
    }
}
