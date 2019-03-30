package org.ngi.nodes.conditionals.logical;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.ExpressionOutput;

/**
 *  Logical Not operator(!).
 * @author Tom
 */
public class Not extends Node {
    
    private NodeOutput _outExp;
    
    protected NodeInput _inExp;

    
    public Not() {
        super("Not", "Logical ! statement (!).");
        
    }   
    
    
    @Override
    public void init() {
        
        _inExp = new ExpressionInput(this,"X", "Expression to negate.");
        
        _outExp = new ExpressionOutput(this,"N", "Negated expression.");
        
        _inExp.setStaticValue("false");
        
        addInput(_inExp);
        addOutput(_outExp);         
        
    }
    
    @Override
    public boolean evaluate() {
        
        String expStr = _inExp.getInputValue();
        String outStr = "!" + expStr;
        
        _outExp.setOutputValue(outStr);
        return true;
        
    }
    
    
}
