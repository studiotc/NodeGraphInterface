package org.ngi.nodes.iteration;

import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.OperationInput;
import org.ngi.nodeinputs.SetInput;
import org.ngi.nodeinputs.VariableInput;
import org.ngi.nodeoutputs.OperationOutput;

/**
 *  For loop Node.  Creates a For loop and iterates based on the provide range or vector.
 *  A variable name is provided to reference during the loop execution.
 * @author Tom
 */
public class For extends Node {
    
    private NodeOutput _outOpp;
    
    private NodeInput _inOpp;
    private NodeInput _inVar;
    private NodeInput _inSet;
   
    
    public For() {
        super("For", "For statement using any range or vector with variable reference.");
  
    }
    
    @Override
    protected void init() {
        
        
        _inVar = new VariableInput(this,"V", "Variable name for item reference.");
        _inSet = new SetInput(this,"S", "Range or vector to iterate over.");
        _inOpp = new OperationInput(this,"G", "Geometry or statement for body of 'for' statement.");
        
        _outOpp = new OperationOutput(this,"O", "For block.");
        
        
        addInput(_inOpp);
        addInput(_inVar);
        addInput(_inSet);
        
        

        addOutput(_outOpp); 
        
        
    }

    @Override
    public boolean evaluate() {
        
        String outStr = "";
        String base = _inOpp.getInputValue();
        
        String var = _inVar.getInputValue();
        String rng = _inSet.getInputValue();
 
        //indent base
        base = InputUtilities.indentText(base);
        outStr = "for( " + var + " = " + rng + " ) {\n";
        outStr += base;
        outStr += "}\n";
        
        _outOpp.setOutputValue(outStr);
        
        return true;
    }
    
}
