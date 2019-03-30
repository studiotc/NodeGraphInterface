package org.ngi.nodes.datatypes;


import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.StaticVariableInput;
import org.ngi.nodeoutputs.VariableOutput;


/**
 * Variable data type.  Creates a variable name, and is also used for  Function names, Module names, and Parameter names.
 * @author Tom
 */
public class Variable extends Node {
    
    private NodeInput _inVar;
    private NodeOutput _outVar;
    
    public Variable() {
        super( "Variable", "Variable name for referencing variables.");
    }
    
    
    @Override
    protected void init() {

        _inVar = new StaticVariableInput(this, "v", "Static variable name.");
        _outVar = new VariableOutput(this, "V", "Variable name value.");
        

     
        addInput(_inVar);
        addOutput(_outVar);        
        
    }    
      
   @Override
    public boolean evaluate() {
        
        String outStr = _inVar.getInputValue();
        String vn = outStr.trim();
        _outVar.setOutputValue(vn);
        _inVar.setDisplayName(vn);            
        return true;
              
        
    }  
    
    @Override
    protected void setErrorState(String msg) {
        super.setErrorState(msg); //To change body of generated methods, choose Tools | Templates.
        
        String nStr = _inVar.getInputValue();
        //show the number value
        _inVar.setDisplayName(nStr);
        
    }    
    
}
