package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Fragment Spec Input.  Accepts connected Fragment Spec.
 * @author Tom
 */
public class FragmentSpecInput extends NodeInput{
    
    public FragmentSpecInput (Node parent, String name, String description) {
        super(parent, name, DataType.FRAGMENT_SPEC, InputDataRequirements.NOT_NULL_CONN, description);
        
    }
    
   
    
    @Override
    public boolean isOutputCompatible(DataType dtOut){
        return dtOut == DataType.FRAGMENT_SPEC;
    } 
    
    
}

    
    

