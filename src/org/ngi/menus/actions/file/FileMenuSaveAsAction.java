
package org.ngi.menus.actions.file;


import java.awt.event.ActionEvent;
import org.ngi.FileHandler;

/**
 * Action to call saveFileAs on the FileHandler.
 * @author Tom
 */
public class FileMenuSaveAsAction extends FileMenuAction {
    
    public FileMenuSaveAsAction(FileHandler fileHandler) {
        super(fileHandler, "SaveAs");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       //file saveas
        if(_fileHandler != null) {
            _fileHandler.saveFileAs();
        }
        
    }    
    
    
}

