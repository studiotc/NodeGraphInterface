package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Basic Operation Input.  Accepts connected values.
 * @author Tom
 */
public class OperationInput extends NodeInput{
    
    public OperationInput (Node parent, String name, String description) {
        super(parent, name, DataType.OPERATION, InputDataRequirements.NOT_NULL_CONN, description);
        
    }
    
    @Override
    public boolean isOutputCompatible(DataType dtOut){
        return dtOut == DataType.OPERATION;
    } 
    
    
}

    
    

