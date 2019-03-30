package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Static Boolean Input (No connected values).
 * @author Tom
 */
public class StaticBooleanInput extends NodeInput{
    
    public StaticBooleanInput (Node parent, String name, String description) {
        super(parent, name, DataType.BOOLEAN, InputDataRequirements.NOT_NULL_STATIC, description);
        init();
    }
    
    private void init() {
        this.setStaticValue("true");
    }    
    
    @Override
    public boolean isOutputCompatible(DataType dtOut){
        return dtOut == DataType.BOOLEAN;
    } 
    
    
}

    
    

