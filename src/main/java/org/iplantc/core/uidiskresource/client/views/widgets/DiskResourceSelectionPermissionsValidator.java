package org.iplantc.core.uidiskresource.client.views.widgets;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResource;

import com.google.common.collect.Lists;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;


/**
 * A validator to validate permissions on disk resource selection
 * 
 * @author sriram
 *
 * @param <R>
 */
public class DiskResourceSelectionPermissionsValidator <R extends DiskResource> implements Validator<String>{

    private R diskResource;

    /**
     * @return the diskResource
     */
    public R getDiskResource() {
        return diskResource;
    }

    /**
     * @param diskResource the diskResource to set
     */
    public void setDiskResource(R diskResource) {
        this.diskResource = diskResource;
    }

    @Override
    public List<EditorError> validate(Editor<String> editor, String value) {
        List<EditorError> errors = Lists.newArrayList();
        errors.addAll(validatePermission(editor, value));
        return errors;
    }

    private List<EditorError> validatePermission(Editor<String> editor, String value) {
        List<EditorError> errors = Lists.newArrayList();
        if (diskResource == null) {
            errors.add(new DefaultEditorError(editor, I18N.DISPLAY.permissionSelectErrorMessage(),
                    value));
            return errors;
        }

        if (!(diskResource.getPermissions().isWritable() || diskResource.getPermissions().isOwner())) {
            errors.add(new DefaultEditorError(editor, I18N.DISPLAY.permissionSelectErrorMessage(),
                    value));
        }

        return errors;

    }
}
