package org.ngi.menus.actions;



import java.awt.event.ActionEvent;
import org.ngi.NodeGraph;
import org.ngi.Node;

import javax.swing.AbstractAction;

/**
 *  Action to call duplicateNode on the NodeGraph.  The actions contains the NOde to apply the call to.
 * @author Tom
 */
public class DuplicateNodeAction  extends AbstractAction{
  protected NodeGraph _nodeGraph;
    private Node _node;
    
    public DuplicateNodeAction(NodeGraph nodeGraph) {
        super("Duplicate");
        _nodeGraph = nodeGraph;
        _node = null;
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       //duplicate the node
        _nodeGraph.duplicateNode(_node);
        
    }
    
    public void setNode(Node node) {
        _node = node;
    }
   
    public Node getNode() {
        return _node ;
    }     
}
