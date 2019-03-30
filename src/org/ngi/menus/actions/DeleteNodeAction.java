package org.ngi.menus.actions;



import java.awt.event.ActionEvent;
import org.ngi.NodeGraph;
import org.ngi.Node;

import javax.swing.AbstractAction;


/**
 * Action to call deleteNode on the NodeGraph.   The action contains the Node to apply the call to.
 * @author Tom
 */
public class DeleteNodeAction extends AbstractAction {
    
   protected NodeGraph dnaNodeGraph;
    private Node dnaNode;
    
    public DeleteNodeAction(NodeGraph nodeGraph) {
        super("Delete");
        dnaNodeGraph = nodeGraph;
        dnaNode = null;
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       //delete the node
        dnaNodeGraph.deleteNode(dnaNode);
        
    }
    
    public void setNode(Node node) {
        dnaNode = node;
    }
   
    public Node getNode() {
        return dnaNode ;
    }     
    
}
