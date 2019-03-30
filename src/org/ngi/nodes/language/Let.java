
package org.ngi.nodes.language;

import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.ExpressionOutput;



/**
 * Sets temporary values for variables (Let statement).  Use with an assignment Node, each assignment is a parameter here.
 * Allows for additional optional inputs for function arguments.
 * @author Tom
 */
public class Let extends BaseParameters {

    private NodeInput _inExp; 
    private NodeOutput _outExp;
 

    public Let() {
        super("Let", "Set temporary variable assignments within the current scope.");
 
    }    
    
    @Override
    protected void init() {
        super.init();
        
        _inExp = new ExpressionInput(this,"X", "Expression using the variables");
        _outExp = new ExpressionOutput(this,"C", "Function call.");
        
        addInput(_inExp);
        addOutput(_outExp);   
        
        
        //add one parameter
        addOptionalInput();
        
    }

    @Override
    public boolean evaluate() {
       
        String expStr = _inExp.getInputValue();
        String argStr = getParameters();
        
        String outStr = "let (";
        outStr += argStr;
        outStr += ") " + expStr;

        _outExp.setOutputValue(outStr);
        
        return true;
    }
     
    
}
