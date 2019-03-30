package org.ngi.nodes.utilities;


import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.AnyInput;
import org.ngi.nodeoutputs.OperationOutput;

/**
 *  This creates a multi-line block of code by appending the Input blocks.  This was designed for cases
 * when multiple Nodes need to be outputted to OpenSCAD.  I.E.:  Declaring one or more variables ahead
 * of any modeling operations.
 * @author Tom
 */
public class MergeBlock extends Node{
    
    private NodeOutput _outBlock;
  
    public MergeBlock() {
        super("MergeBlock", "Creates a multi-line block of text by appending lines or blocks of text together.");
       
    }

    
    @Override
    protected void init() {
        
        
        _outBlock = new OperationOutput(this, "M", "Merged blocks.");
        
 
        NodeInput a = new AnyInput(this, "B", "Block or expression.");
        NodeInput b = new AnyInput(this, "B", "Block or expression.");
        NodeInput c = new AnyInput(this, "B", "Block or expression.");
        
        a.setAsOptional();
        b.setAsOptional();
        c.setAsOptional();
        
        
        addInput(a);
        addInput(b);
        addInput(c);
        
        addOutput(_outBlock); 
        
    }    
    
    @Override
    public boolean evaluate() {
        
        String outStr = "";
        
        for(NodeInput input : this.getInputs()) {
            String curBlk = input.getInputValue();
            
            outStr += curBlk + "\n"; 
                        
            
        }
        
        _outBlock.setOutputValue(outStr);
        return true;
        
        
    }   
    
    
    @Override
    public boolean allowOptionalInputs() {
        return true;
    }
    
    @Override
    public void addOptionalInput() {
        
        NodeInput a = new AnyInput(this, "B", "Block or expression.");
        a.setAsOptional();
        addInput(a);
        
    }
    
    
    
}
