package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Expression Input for MergeExpression - takes normal expressions (number, variables, etc) along with booleans, ranges, etc.
 *  Accept almost anything except Operations, etc.
 * @author Tom
 */
public class ExpressionInputAny extends NodeInput {

    public ExpressionInputAny(Node parent, String name, String description) {
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
            case BOOLEAN:
            case NUMBER:
            case VARIABLE:
            case EXPRESSION:
            case RANGE:
            case VECTOR:
                result = true;
                break;
        }

        return result;
    }

}
