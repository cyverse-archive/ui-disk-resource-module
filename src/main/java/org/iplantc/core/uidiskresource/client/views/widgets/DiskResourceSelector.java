package org.iplantc.core.uidiskresource.client.views.widgets;

import java.util.List;

import com.google.gwt.editor.client.EditorError;

/**
 * An interface for all DiskResource selectors.
 * @author jstroot
 *
 */
public interface DiskResourceSelector {

    List<EditorError> getErrors();

    void setRequired(boolean required);
}
