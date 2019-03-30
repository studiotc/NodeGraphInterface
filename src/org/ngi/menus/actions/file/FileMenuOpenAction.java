package org.ngi.menus.actions.file;

import java.awt.event.ActionEvent;
import org.ngi.FileHandler;

/**
 * Action to call openFile on the FileHandler.
 * @author Tom
 */
public class FileMenuOpenAction extends FileMenuAction {
    
    public FileMenuOpenAction( FileHandler fileHandler) {
        super(fileHandler, "Open");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       
        if(_fileHandler != null) {
            //_nodeGraph.deserializeFile();
            _fileHandler.openFile();
        }
        
    }    
    
    
}
