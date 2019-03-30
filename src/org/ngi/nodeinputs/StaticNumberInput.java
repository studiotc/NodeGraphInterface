package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Static Number Input (no connected values)
 * @author Tom
 */
public class StaticNumberInput extends NodeInput{
    
    public StaticNumberInput (Node parent, String name, String description) {
        super(parent, name, DataType.NUMBER, InputDataRequirements.NOT_NULL_STATIC, description);
        init();
    }
    
    private void init() {
        this.setStaticValue("1");    
    }    
    
    @Override
    public boolean isOutputCompatible(DataType dtOut){
        return dtOut == DataType.NUMBER;
    } 
    
    
}

    
    

