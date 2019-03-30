package org.ngi.menus.actions;


import java.awt.event.ActionEvent;
import org.ngi.NodeGraph;
import org.ngi.Node;
import javax.swing.AbstractAction;


/**
 * Action to call addOptionalInput on NodeGraph.  The action contains the Node to apply the call to.
 *
 * @author Tom
 */
public class AddOptionalInputAction  extends AbstractAction {
    
    protected NodeGraph _nodeGraph;
    private Node _node;
    
    
    public AddOptionalInputAction(NodeGraph nodeGraph) {
        super("Add Input");
        
        _nodeGraph = nodeGraph;
        _node = null;
        
        
        
    }  
    
    @Override
    public void actionPerformed(ActionEvent e) {
      
        _nodeGraph.addOptionalInput(_node);
        
    }    
    
    public void setNode(Node node) {
        _node = node;
    }
   
    public Node getNode() {
        return _node ;
    }       
    
}
