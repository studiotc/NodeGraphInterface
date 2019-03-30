
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
public class Render extends Node {
    
    private NodeInput _inOpp;
    private NodeInput _inEnable;
    
    private NodeOutput _outOpp;
    
   
    
    public Render() {
        super("Render", "Forces a render of the geometry  (can be disabled to bypass)");
  
    }
    
    @Override
    protected void init() {
        
        _inOpp = new OperationInput(this,"G", "Geometry to render.");
        _inEnable = new BooleanInput(this,"E", "Enable render, False = bypass statement (no render() output).");

        
        
        _outOpp = new OperationOutput(this,"O", "Render block.");
        

        
        addInput(_inOpp);
        addInput(_inEnable);

        addOutput(_outOpp); 
        
        
    }

    @Override
    public boolean evaluate() {
        
        String outStr = "";
        String base = _inOpp.getInputValue();
        String enableStr = _inEnable.getInputValue().trim();
  
        //is enabled?
        if(enableStr.equals("true")) {
            
            //indent base
           // base = "\t" + base.replace("\n", "\n\t");
            base = InputUtilities.indentText(base);
            outStr = "render() {\n";
            outStr += base;
            outStr += "}\n";            
            
        } else { 
            //just pass it along if render not enabled
            outStr = base;
            
        }
 
        _outOpp.setOutputValue(outStr);
        
        return true;
    }
    
}
