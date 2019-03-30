package org.ngi.menus.actions;


import java.awt.event.ActionEvent;
import org.ngi.NodeConnection;
import org.ngi.NodeGraph;
import org.ngi.NodeInput;

import javax.swing.AbstractAction;



/**
 *  Action to call addOptionalInput on NodeGraph.  The action contains the NodeInput to apply the call to.
 * @author Tom
 */
public class DeleteOptionalInputAction extends AbstractAction {
    
    protected NodeGraph _nodeGraph;
    private NodeInput _inputNode;
    
    public DeleteOptionalInputAction(NodeGraph nodeGraph) {
        super("Delete Input");
        _nodeGraph = nodeGraph;
        _inputNode = null;
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       //delete the optional input
        _nodeGraph.deleteOptionalInput(_inputNode);
        
    }
    
    public void setInputNode(NodeInput input) {
        _inputNode = input;
    }
   
    public NodeInput getInputNode() {
        return _inputNode ;
    }    
    
}
