package org.ngi.menus.actions;



import java.awt.event.ActionEvent;
import org.ngi.NodeGraph;
import org.ngi.Node;

import javax.swing.AbstractAction;

/**
 * Action to call renameNode on the NodeGraph.   The action contains the Node to apply the call to.
 * @author Tom
 */
public class RenameNodeAction  extends AbstractAction{
  protected NodeGraph _nodeGraph;
    private Node _node;
    
    public RenameNodeAction(NodeGraph nodeGraph) {
        super("Rename");
        _nodeGraph = nodeGraph;
        _node = null;
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       //delete the node
        _nodeGraph.renameNode(_node);
        
    }
    
    public void setNode(Node node) {
        _node = node;
    }
   
    public Node getNode() {
        return _node ;
    }     
}
