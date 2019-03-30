
package org.ngi.nodes.utilities;


import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.ExpressionOutput;




/**
 *  Simple Node to add a semi-colon to the end of an expression. 
 * @author Tom
 */
public class SemiColon extends Node {
    
    private NodeInput _inValue;
    private NodeOutput _outExp;
    
    
    /**
     * Constructor
     * 
     */
    public SemiColon() {
        super("SemiColon", "Adds a semi-colon to the end of a line (;).");
    }
    
    
    @Override
    protected void init() {
        
  
        _inValue = new ExpressionInput(this, "X", "Expression to add semi-colon to.");
        _outExp = new ExpressionOutput(this, "O", "Expression with a semi-colon appended.");
        
        _inValue.setStaticValue("a=1");
        addInput(_inValue);
        addOutput(_outExp);        
        
    }    
      
   @Override
    public boolean evaluate() {
        
  
        String varExp = _inValue.getInputValue();

        String outStr = varExp + ";";
        _outExp.setOutputValue(outStr);
        
         return true;
         
        
    }    
    
}
