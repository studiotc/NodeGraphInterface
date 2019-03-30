
package org.ngi.menus.actions.file;


import java.awt.event.ActionEvent;
import org.ngi.FileHandler;


/**
 * Action to call newFile on the FileHandler.
 * @author Tom
 */
public class FileMenuNewAction extends FileMenuAction {
  
    public FileMenuNewAction(FileHandler fileHandler) {
        super(fileHandler, "New");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       
        if(_fileHandler != null) {
             //start new file
             _fileHandler.newFile();
        }
        
    } 
    
}
