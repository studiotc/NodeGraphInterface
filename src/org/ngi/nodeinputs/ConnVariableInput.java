package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Connected Variable Input.  Accepts connected Variables.
 * @author Tom
 */
public class ConnVariableInput extends NodeInput{
    
    public ConnVariableInput (Node parent, String name, String description) {
        super(parent, name, DataType.VARIABLE, InputDataRequirements.NOT_NULL_CONN, description);
   
    }
    
    @Override
    public boolean isOutputCompatible(DataType dtOut){
        return dtOut == DataType.VARIABLE;
    } 
    
    
}

    
    

