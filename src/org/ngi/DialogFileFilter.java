package org.ngi;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Dialog file filter class for use with standard swing dialogs.
 *
 * @author Tom
 */
public class DialogFileFilter extends FileFilter {

    private String _extension;
    private String _description;

    /**
     * Construct a dialog file filter .
     * @param extension  Extension of the filter.
     * @param description Description of the filter.
     */
    public DialogFileFilter(String extension, String description) {

        _extension = extension;
        _description = description;
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String fileName = file.getName();

        if (fileName.endsWith(_extension)) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public String getDescription() {
        return _description;
    }

}
