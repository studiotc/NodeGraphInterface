package org.ngi.menus.actions;

import java.awt.event.ActionEvent;
import org.ngi.NodeGraph;
import org.ngi.NodeInput;

import javax.swing.AbstractAction;



/**
 * Action to call setStaticValue on the NodeGraph.   The action contains the Node to apply the call to.
 * @author Tom
 */
public class SetStaticValueAction extends AbstractAction{
    protected NodeGraph _nodeGraph;
    private NodeInput _inputNode;
    
    public SetStaticValueAction(NodeGraph nodeGraph) {
        super("Set Static Value");
        _nodeGraph = nodeGraph;
        _inputNode = null;
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       //delete the connection
        _nodeGraph.setStaticValue(_inputNode);
        
    }
    
    public void setInputNode(NodeInput input) {
        _inputNode = input;
    }
   
    public NodeInput getInputNode() {
        return _inputNode ;
    }      
}
