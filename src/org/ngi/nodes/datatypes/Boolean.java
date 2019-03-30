package org.ngi.nodes.datatypes;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.StaticBooleanInput;
import org.ngi.nodeoutputs.BooleanOutput;

/**
 *  Boolean data type. Creates "true" or "false".
 * @author Tom
 */
public class Boolean extends Node {
        
    private NodeInput _inBool;
    private NodeOutput _outBool;
  
    public Boolean() {
        super( "Boolean", "Boolean data type: true or false.");
       
    }

    
    @Override
    protected void init() {
        
        _inBool = new StaticBooleanInput(this,"b","Static boolean value.");
        _outBool = new BooleanOutput(this, "B", "Boolean value.");
        
        _inBool.setStaticValue("true");
        //_inNum.disallowInput();
        addInput(_inBool);
        addOutput(_outBool); 
        
    }    
    
    @Override
    public boolean evaluate() {
        
        String nStr = _inBool.getInputValue();
        _outBool.setOutputValue(nStr); 
        
        //show the number value
        _inBool.setDisplayName(nStr);
        
        return true;
                
        
    }
    
    
 
    
    
}