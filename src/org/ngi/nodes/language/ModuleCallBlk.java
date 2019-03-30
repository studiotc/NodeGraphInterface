
package org.ngi.nodes.language;

import org.ngi.InputUtilities;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.OperationInput;
import org.ngi.nodeinputs.VariableInput;
import org.ngi.nodeoutputs.OperationOutput;



/**
 * Calls a module that wraps a block of code instead of a simple module call.  Allows for additional optional inputs for module arguments.
 * @author Tom
 */
public class ModuleCallBlk extends BaseParameters {

    private NodeOutput _outCall;
    private NodeInput _inName; 
    private NodeInput _inBlock;
   
    
    public ModuleCallBlk() {
        super("ModuleCallBlk", "Call a module and apply it to a block.");
  
    }    
    
    @Override
    protected void init() {
        super.init();
        
        _inName = new VariableInput(this,"N", "Name of the function or module to call.");
        
        _inBlock = new OperationInput(this,"B", "Block to apply module to.");
        
        _outCall = new OperationOutput(this,"M", "Block with applied module call.");
        
        addInput(_inName);
        addInput(_inBlock);
        addOutput(_outCall);   
        
        
    }

    @Override
    public boolean evaluate() {
       
        
        String name = _inName.getInputValue();
        String blkStr = _inBlock.getInputValue();
        //get parameters
        String argStr = getParameters();
        
        //indent block
        blkStr = InputUtilities.indentText(blkStr);
        
        String outStr = name + "(" + argStr + ") {\n";
        outStr += blkStr;
        outStr += "}\n";

        _outCall.setOutputValue(outStr);
        
        return true;
    }
     
    
}
