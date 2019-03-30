package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Variable Input.  Accepts static and connected values.
 *  This is for passing Variable names.
 * @author Tom
 */
public class VariableInput extends NodeInput{
    
    public VariableInput (Node parent, String name, String description) {
        super(parent, name, DataType.VARIABLE, InputDataRequirements.NOT_NULL_ANY, description);
        init();
 
    }
    
    private void init() {
        this.setStaticValue("i");    
    }    
    
    @Override
    public boolean isOutputCompatible(DataType dtOut){
        return dtOut == DataType.VARIABLE;
    } 
    
    
}

    
    

