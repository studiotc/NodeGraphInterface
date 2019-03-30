package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Basic Boolean Input
 * @author Tom
 */
public class BooleanInput extends NodeInput{
    
    public BooleanInput (Node parent, String name, String description) {
        super(parent, name, DataType.BOOLEAN, InputDataRequirements.NOT_NULL_ANY, description);
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

    
    

