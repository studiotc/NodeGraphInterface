package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Static Variable Input (no connected values).
 * @author Tom
 */
public class StaticVariableInput extends NodeInput{
    
    public StaticVariableInput (Node parent, String name, String description) {
        super(parent, name, DataType.VARIABLE, InputDataRequirements.NOT_NULL_STATIC, description);
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

    
    

