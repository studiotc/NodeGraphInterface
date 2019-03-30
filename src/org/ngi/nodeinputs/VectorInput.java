package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Vector Input.  Accepts only connected values.
 * @author Tom
 */
public class VectorInput extends NodeInput{
    
    public VectorInput (Node parent, String name, String description) {
        super(parent, name, DataType.VECTOR, InputDataRequirements.NOT_NULL_CONN, description);
    }
    
    @Override
    public boolean isOutputCompatible(DataType dtOut){
        boolean result = false;
            switch(dtOut) {
                case VARIABLE:
                case VECTOR:
                    result = true;
                    break;
            }
        
        return result;
    } 
    
    
}

    
    

