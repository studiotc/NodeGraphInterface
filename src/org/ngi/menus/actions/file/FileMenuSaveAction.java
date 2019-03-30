
package org.ngi.menus.actions.file;

import java.awt.event.ActionEvent;
import org.ngi.FileHandler;

/**
 *
 * @author Tom
 */
public class FileMenuSaveAction extends FileMenuAction {
    
    public FileMenuSaveAction(FileHandler fileHandler) {
        super(fileHandler, "Save");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       //save the file
        if(_fileHandler != null) {
            _fileHandler.saveFile();
        }
        
    }    
    
    
}
