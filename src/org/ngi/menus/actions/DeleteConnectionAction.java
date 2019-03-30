package org.ngi.menus.actions;


import java.awt.event.ActionEvent;
import org.ngi.NodeConnection;
import org.ngi.NodeGraph;
import org.ngi.NodeInput;

import javax.swing.AbstractAction;



/**
 *  Action to call delete connection on a connection object.  The action contains the NodeInput, and the
 * connection object is referenced from it.
 * @author Tom
 */
public class DeleteConnectionAction extends AbstractAction {
    
    protected NodeGraph _nodeGraph;
    private NodeInput _inputNode;
    
    public DeleteConnectionAction(NodeGraph nodeGraph) {
        super("Delete Connection");
        _nodeGraph = nodeGraph;
        _inputNode = null;
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       //delete the connection
       NodeConnection conn = _inputNode.getConnection();
        _nodeGraph.deleteConnection(conn);
        
    }
    
    public void setInputNode(NodeInput input) {
        _inputNode = input;
    }
   
    public NodeInput getInputNode() {
        return _inputNode ;
    }    
    
}
