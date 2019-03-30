package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 * Child Input, accepts any connected number, variable, expression, range or vector.
 * Used only to set the selector/index when using the children() statement.
 *
 * @author Tom
 */
public class ChildrenInput extends NodeInput {

    public ChildrenInput(Node parent, String name, String description) {
        super(parent, name, DataType.SET, InputDataRequirements.NOT_NULL_CONN, description);

    }

    @Override
    public boolean isOutputCompatible(DataType dtOut) {
        boolean result = false;
        
        switch (dtOut) {
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
