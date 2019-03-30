package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Static File Name Input (No connected values).
 * This is for the output or import files.
 * @author Tom
 */
public class StaticFileNameInput extends NodeInput{
    
    public StaticFileNameInput (Node parent, String name, String description) {
        super(parent, name, DataType.FILE_NAME, InputDataRequirements.NOT_NULL_STATIC, description);
        
    }
    
    
    
    @Override
    public boolean isOutputCompatible(DataType dtOut){
        return dtOut == DataType.FILE_NAME;
    } 
    
    
}

    
    

