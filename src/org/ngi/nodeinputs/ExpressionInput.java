package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 * Basic Expression Input
 *
 * @author Tom
 */
public class ExpressionInput extends NodeInput {

    public ExpressionInput(Node parent, String name, String description) {
        super(parent, name, DataType.EXPRESSION, InputDataRequirements.NOT_NULL_ANY, description);
        init();
    }
    
    private void init() {
        this.setStaticValue("1");
    }    

    @Override
    public boolean isOutputCompatible(DataType dtOut) {
        boolean result = false;
        switch (dtOut) {
            case NUMBER:
            case VARIABLE:
            case EXPRESSION:
                result = true;
                break;
        }

        return result;
    }

}
