package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Basic Number Input
 * @author Tom
 */
public class NumberInput extends NodeInput{
    
    public NumberInput (Node parent, String name, String description) {
        super(parent, name, DataType.NUMBER, InputDataRequirements.NOT_NULL_ANY, description);
        
    }
    
    @Override
    public boolean isOutputCompatible(DataType dtOut){
        return dtOut == DataType.NUMBER;
    } 
    
    
}

    
    

