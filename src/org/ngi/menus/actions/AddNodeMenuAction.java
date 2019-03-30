package org.ngi.menus.actions;


import java.awt.event.ActionEvent;
import org.ngi.NodeGraph;

import javax.swing.AbstractAction;


/**
 *  Action to call createNode on the NodeGraph.   The action contains the Node class name to apply the call to.
 * @author Tom
 */
public class AddNodeMenuAction extends AbstractAction {
    
    protected NodeGraph _nodeGraph;
    protected String _type;
   
    
    public AddNodeMenuAction(NodeGraph nodeGraph, String name, String type) {
        super(name);
        
        _nodeGraph = nodeGraph;
        _type = type;
        
        
        
    }  
    
    @Override
    public void actionPerformed(ActionEvent e) {
       
        //add the node
       _nodeGraph.createNode(_type);
        
    }
    
    
}
