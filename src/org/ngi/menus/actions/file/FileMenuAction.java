package org.ngi.menus.actions.file;

import java.awt.event.ActionEvent;
import org.ngi.FileHandler;
import javax.swing.AbstractAction;

/**
 *  Base Class for File Menu Actions.  requires a reference to the FileHandler object and a name to present.
 * @author Tom
 */
public class FileMenuAction extends AbstractAction {
    
   // protected NodeGraph _nodeGraph;
    protected FileHandler _fileHandler;
    
    public FileMenuAction(FileHandler fileHandler, String name) {
        super(name);
        
        _fileHandler = fileHandler;
        //_nodeGraph = nodeGraph;
    }

    /**
     * Override this in Subclasses to call NodeGraph File operations
     * @param e ActionEvent  The Action Event called from UI Element (Menu)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
       
        //testing...
        String cmd = e.getActionCommand();
        System.out.println("File menu Command: " + cmd);
        
    }
    
    
    
}
