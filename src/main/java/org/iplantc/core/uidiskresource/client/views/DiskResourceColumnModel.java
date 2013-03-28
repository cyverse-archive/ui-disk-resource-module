package org.iplantc.core.uidiskresource.client.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.DiskResourceProperties;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.views.cells.DiskResourceNameCell;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

public class DiskResourceColumnModel extends ColumnModel<DiskResource> {

    private static CheckBoxSelectionModel<DiskResource> sm;

    public DiskResourceColumnModel() {
        super(createColumnConfigList());
    }

    public static List<ColumnConfig<DiskResource, ?>> createColumnConfigList() {
        List<ColumnConfig<DiskResource, ?>> list = new ArrayList<ColumnConfig<DiskResource, ?>>();

        DiskResourceProperties props = GWT.create(DiskResourceProperties.class);

        sm = new CheckBoxSelectionModel<DiskResource>(new IdentityValueProvider<DiskResource>());
        ColumnConfig<DiskResource, DiskResource> name = new ColumnConfig<DiskResource, DiskResource>(
                new IdentityValueProvider<DiskResource>(),
                100, I18N.DISPLAY.name());
        name.setCell(new DiskResourceNameCell(DiskResourceNameCell.CALLER_TAG.DATA));

        ColumnConfig<DiskResource, Date> lastModified = new ColumnConfig<DiskResource, Date>(
                props.lastModified(), 120, I18N.DISPLAY.lastModified());
        lastModified.setCell(new DateCell(DateTimeFormat
                .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM)));
        ColumnConfig<DiskResource, String> size = new ColumnConfig<DiskResource, String>(
                new DiskResourceSizeValueProvider(),
                50, I18N.DISPLAY.size());

        list.add(sm.getColumn());
        list.add(name);
        list.add(lastModified);
        list.add(size);

        return list;
    }

    public CheckBoxSelectionModel<DiskResource> getSelectionModel() {
        return sm;
    }

    /**
     * This is a value provider class which returns the size of any <code>DiskResource</code>.
     * If the <code>DiskResource</code> is a <code>Folder</code>, this provider will return null. If it
     * is a <code>File</code>, then it returns the value of the {@link File#getSize()} method.
     *
     * @author jstroot
     *
     */
    private static final class DiskResourceSizeValueProvider implements
            ValueProvider<DiskResource, String> {
        @Override
        public String getValue(DiskResource object) {
            if (object instanceof File) {
                // FIXME JDS This value provider is returning the un-converted file size in bytes.
                return ((File)object).getSize();
            } else {
                return null;
            }
        }

        @Override
        public void setValue(DiskResource object, String value) {
        }

        @Override
        public String getPath() {
            return "";
        }
    }
}
