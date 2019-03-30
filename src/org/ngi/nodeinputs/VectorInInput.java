package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Vector In Input.  Allows connected or static values.
 *  Accepts any vector component (number, variable, other vector, etc).
 * @author Tom
 */
public class VectorInInput extends NodeInput{
    
    public VectorInInput (Node parent, String name, String description) {
        super(parent, name, DataType.VECTOR, InputDataRequirements.NOT_NULL_ANY, description);
        init();
    }
    
    private void init() {
        this.setStaticValue("1");    
    }    
    
    @Override
    public boolean isOutputCompatible(DataType dtOut){
        boolean result = false;
            switch(dtOut) {
                case BOOLEAN:
                case NUMBER:
                case VARIABLE:
                case EXPRESSION:
                case VECTOR:
                    result = true;
                    break;
            }
        
        return result;
    } 
    
    
}

    
    

