package org.iplantc.core.uidiskresource.client.models;

/**
 * Models the metadata for a file identifier.
 * 
 * A file identifier names three parts of information:
 * <ul>
 * <li>parent-id: the identifier of the disk resource parent</li>
 * <li>file-id: the identifier of the file resource</li>
 * <li>filename: the name of the file resource</li>
 * </ul>
 */
public class FileIdentifier {
    protected final String idParent;
    protected final String idFile;
    protected final String filename;

    /**
     * Instantiate from file name, parent id, and file id.
     * 
     * @param filename name of file.
     * @param idParent unique id of parent folder.
     * @param idFile unique file id.
     */
    public FileIdentifier(final String filename, final String idParent, final String idFile) {
        this.filename = filename;
        this.idParent = idParent;
        this.idFile = idFile;
    }

    /**
     * Retrieve parent folder id.
     * 
     * @return id of parent folder.
     */
    public String getParentId() {
        return idParent;
    }

    /**
     * Retrieve file id.
     * 
     * @return unique file id.
     */
    public String getFileId() {
        return idFile;
    }

    /**
     * Retrieve file name.
     * 
     * @return file name.
     */
    public String getFilename() {
        return filename;
    }
}
