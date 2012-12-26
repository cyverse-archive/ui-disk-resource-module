package org.iplantc.core.uidiskresource.client.views.cells;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
    @Source("file.gif")
    ImageResource file();

    @Source("folder.gif")
    ImageResource folder();
}