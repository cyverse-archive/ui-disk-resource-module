package org.iplantc.core.uidiskresource.client.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

import com.sencha.gxt.theme.base.client.field.DateCellDefaultAppearance;
import com.sencha.gxt.theme.base.client.field.TriggerFieldDefaultAppearance;

import org.iplantc.core.uidiskresource.client.search.views.DiskResourceSearchCell.DiskResourceSearchCellAppearance;

/**
 * This class is a clone-and-own of {@link DateCellDefaultAppearance}.
 * 
 * @author jstroot
 * 
 */
public class DiskResourceSearchCellDefaultAppearance extends TriggerFieldDefaultAppearance implements DiskResourceSearchCellAppearance {

    public interface DiskResourceSearchCellResources extends TriggerFieldResources {

        @Override
        @Source({"com/sencha/gxt/theme/base/client/field/ValueBaseField.css", "com/sencha/gxt/theme/base/client/field/TextField.css", "com/sencha/gxt/theme/base/client/field/TriggerField.css"})
        DiskResourceSearchCellStyle css();
        
        @Override
        @Source("funnel-icon.png")
        ImageResource triggerArrow();

        @Override
        @Source("funnel-icon.png")
        ImageResource triggerArrowOver();

        // TODO Override images
        /*@ImageOptions(repeatStyle = RepeatStyle.Horizontal)
        ImageResource textBackground();



        @Source("dateArrowClick.png")
        ImageResource triggerArrowClick();

        @Source("dateArrowFocus.png")
        ImageResource triggerArrowFocus();

        ImageResource triggerArrowFocusOver();

        ImageResource triggerArrowFocusClick();*/
    }

    public interface DiskResourceSearchCellStyle extends TriggerFieldStyle {

    }

    public DiskResourceSearchCellDefaultAppearance() {
        this(GWT.<DiskResourceSearchCellResources> create(DiskResourceSearchCellResources.class));
    }

    public DiskResourceSearchCellDefaultAppearance(DiskResourceSearchCellResources resources) {
        super(resources);
    }

}
