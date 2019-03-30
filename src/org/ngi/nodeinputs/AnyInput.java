package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Any Input (Any data type).
 * @author Tom
 */
public class AnyInput extends NodeInput{
    
    public AnyInput (Node parent, String name, String description) {
        super(parent, name, DataType.ANY, InputDataRequirements.NOT_NULL_ANY, description);
        
    }
    
    @Override
    public boolean isOutputCompatible(DataType dtOut){
        return true;
    } 
    
    
}

    
    

