package org.ngi.nodes.math;


import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.ExpressionOutput;


/**
 * 
 * Base class for Math Function Nodes.  Designed to create typical function calls that use 
 * one to two arguments.  Arguments are expression inputs.
 * 
 * 
 *
 * @author Tom
 */
public class BaseFunction extends Node {

    private NodeOutput _outOpp;

    protected NodeInput _inArgA;
    protected NodeInput _inArgB;

    private boolean _usingB;
    private String _function;
   

    public BaseFunction(String name, String function, boolean hasArgB, String description) {
        super( name, description);

        _function = function;
        _usingB = hasArgB;
        
        if (_usingB) {
           _inArgB = new ExpressionInput(this, "B", "Function argument."); 
           addInput(_inArgB);
        }

    }

    @Override
    protected void init() {

        _inArgA = new ExpressionInput(this, "A", "Function argument.");

        addInput(_inArgA);
        
        //optional argument b
        _inArgB = null;
        

        _outOpp = new ExpressionOutput(this, "X", "Function output");
        addOutput(_outOpp);

    }

    @Override
    public boolean evaluate() {

     
        String argA = _inArgA.getInputValue();
        String argB = "";
        
        if(_usingB) {
            argB = ", " +  _inArgB.getInputValue();
        }
        
        String outStr = _function + "("+ argA + argB + ")";
        _outOpp.setOutputValue(outStr);

        return true;
    }
    
    
     
    
    

}
