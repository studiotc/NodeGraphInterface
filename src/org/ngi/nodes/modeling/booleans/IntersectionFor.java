package org.ngi.nodes.modeling.booleans;

import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.OperationInput;
import org.ngi.nodeinputs.SetInput;
import org.ngi.nodeinputs.VariableInput;
import org.ngi.nodeoutputs.OperationOutput;

/**
 * Performs a boolean intersection operation the geometry supplied in the for loop.
 * @author Tom
 */
public class IntersectionFor extends Node {
    
    private NodeOutput _outOpp;
    
    private NodeInput _inOpp;
    private NodeInput _inVar;
    private NodeInput _inSet;
   
    
    public IntersectionFor() {
        super("IntersectionFor", "Intersection For statement using any range or vector with variable reference.  Intesects geometry on the stack.");
  
    }
    
    @Override
    protected void init() {
        

        _inVar = new VariableInput(this,"V", "Variable name for item reference.");
        _inSet = new SetInput(this,"S", "Range or vector to iterate over.");
        _inOpp = new OperationInput(this,"G", "Geometry to iterate over and intersect.");
        
        _outOpp = new OperationOutput(this,"O", "For Intersection block.");
        
        _inVar.setStaticValue("i");

       
        
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
        outStr = "intersection_for( " + var + " = " + rng + " ) {\n";
        outStr += base;
        outStr += "}\n";
        
        _outOpp.setOutputValue(outStr);
        
        return true;
    }
    
}
