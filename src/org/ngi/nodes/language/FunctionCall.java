
package org.ngi.nodes.language;

import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.VariableInput;
import org.ngi.nodeoutputs.ExpressionOutput;



/**
 * Calls a function.  Allows for additional optional inputs for function arguments.
 * @author Tom
 */
public class FunctionCall extends BaseParameters {

    private NodeOutput _outCall;
    private NodeInput _inName; 

   
    
    public FunctionCall() {
        super("FunctionCall", "Call the specified function.");
 
    }    
    
    @Override
    protected void init() {
        super.init();
        
        _inName = new VariableInput(this,"N", "Name of the function to call.");
        _outCall = new ExpressionOutput(this,"C", "Function call.");
        
        addInput(_inName);
        addOutput(_outCall);   
        
        
    }

    @Override
    public boolean evaluate() {
       
        
        String name = _inName.getInputValue();
        String argStr = getParameters();
        
        String outStr = name + "(";
        outStr += argStr;
        outStr += ")";

        _outCall.setOutputValue(outStr);
        
        return true;
    }
     
    
}
