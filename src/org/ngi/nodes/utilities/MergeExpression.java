package org.ngi.nodes.utilities;


import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInputAny;
import org.ngi.nodeoutputs.ExpressionOutput;

/**
 *  This creates a single line of code by appending multiple expressions or smaller atomic values together.
 * @author Tom
 */
public class MergeExpression extends Node{
    
    private NodeOutput _outBlock;
  
    public MergeExpression() {
        super("MergeExp", "Creates a single expression by concatenating multiple expressions, numbers, or variables together.");
       
    }

    
    @Override
    protected void init() {
        
        
        _outBlock = new ExpressionOutput(this, "C", "Concatenated expression.");
        
 
        NodeInput a = new ExpressionInputAny(this, "X", "Expression or number, etc.");
        NodeInput b = new ExpressionInputAny(this, "X", "Expression or number, etc.");
        NodeInput c = new ExpressionInputAny(this, "X", "Expression or number, etc.");
        
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
            outStr += input.getInputValue();
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
        
        NodeInput a = new ExpressionInputAny(this, "X", "Expression or number, etc.");
        a.setAsOptional();
        addInput(a);
        
    }
    
    
    
}
