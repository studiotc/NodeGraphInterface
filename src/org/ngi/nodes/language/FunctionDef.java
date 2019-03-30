
package org.ngi.nodes.language;

import java.util.ArrayList;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeinputs.VariableInput;
import org.ngi.nodeoutputs.ExpressionOutput;



/**
 * Defines a function.   Allows for additional optional inputs to define function arguments.
 * @author Tom
 */
public class FunctionDef extends BaseParameters {

    private NodeOutput _outFunc;
    private NodeInput _inName; 
    private NodeInput _inExp;
    

    
    public FunctionDef() {
        super("FunctionDef", "Function definition.");
  
    }    
    
    @Override
    protected void init() {
        super.init();
        
        _inName = new VariableInput(this,"N", "Name of the function");
        _inExp = new ExpressionInput(this,"X", "Function expression.");
        
        _outFunc = new ExpressionOutput(this,"F", "Function definition.");
        
        
        addInput(_inExp);
        addInput(_inName);
        
        addOutput(_outFunc);   

        
    }

    @Override
    public boolean evaluate() {
       
        
        String name = _inName.getInputValue();
        String exp = _inExp.getInputValue();
        
        String argStr = getParameters();
  
        String outStr = "function " + name + "(";
        outStr += argStr;
        outStr += ") = " + exp + ";";
                
        
        _outFunc.setOutputValue(outStr);
        
        return true;
    }
    
     
    
}
