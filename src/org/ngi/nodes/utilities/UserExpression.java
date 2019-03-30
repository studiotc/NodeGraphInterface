package org.ngi.nodes.utilities;


import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.StaticExpressionInput;
import org.ngi.nodeoutputs.ExpressionOutput;


/**
 *  This is a user defined expression or single line of code.  It was designed 
 * for inputing raw code without the use of Nodes.
 * 
 * @author Tom
 */
public class UserExpression extends Node  {
    
    private NodeInput _inExp;
    private NodeOutput _outExp;
  
    public UserExpression() {
        super( "UserExpression", "User specified expression (any valid OpenSCAD code).");
    }

    
    @Override
    protected void init() {
        

        _inExp = new StaticExpressionInput(this,"x", "Static expression.");
        _outExp = new ExpressionOutput(this, "X", "Expression value.");
        

        addInput(_inExp);
        addOutput(_outExp); 
        
    }    
    
    @Override
    public boolean evaluate() {
        
        String outStr = _inExp.getInputValue();
        _outExp.setOutputValue(outStr);
        
        _inExp.setDisplayName(outStr);
        
        return true;

        
    }    
    
    
    @Override
    protected void setErrorState(String msg) {
        super.setErrorState(msg); 
        
        String nStr = _inExp.getInputValue();
        //show the expression value even if error
        _inExp.setDisplayName(nStr);
        
    }     

    
    
}
