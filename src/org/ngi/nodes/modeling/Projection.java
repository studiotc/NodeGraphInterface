
package org.ngi.nodes.modeling;

import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.BooleanInput;
import org.ngi.nodeinputs.OperationInput;
import org.ngi.nodeoutputs.OperationOutput;

/**
 *
 * @author Tom
 */
public class Projection extends Node {
    
    private NodeInput _inOpp;
    private NodeInput _inCut;
    
    private NodeOutput _outOpp;
    
   
    
    public Projection() {
        super("Projection", "Create 2d projection or section of geometry.");
  
    }
    
    @Override
    protected void init() {
        
        _inOpp = new OperationInput(this,"G", "Geometry to project.");
        _inCut = new BooleanInput(this,"C", "Cut geometry (section instead of projection).");

        _inCut.setStaticValue("false");
        
        _outOpp = new OperationOutput(this,"O", "Projected geometry.");
        

        
        addInput(_inOpp);
        addInput(_inCut);

        addOutput(_outOpp); 
        
        
    }

    @Override
    public boolean evaluate() {
        
        String outStr = "";
        String base = _inOpp.getInputValue();
        String cutStr = _inCut.getInputValue().trim();
  

            //indent base
            base = InputUtilities.indentText(base);
            outStr = "projection( cut = " + cutStr + " ) {\n";
            outStr += base;
            outStr += "}\n";            
            

 
        _outOpp.setOutputValue(outStr);
        
        return true;
    }
    
}
