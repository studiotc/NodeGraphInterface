package org.ngi.nodeinputs;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 * Set Input, accepts any connected Range or Vector.
 *
 * @author Tom
 */
public class SetInput extends NodeInput {

    public SetInput(Node parent, String name, String description) {
        super(parent, name, DataType.SET, InputDataRequirements.NOT_NULL_CONN, description);

    }

    @Override
    public boolean isOutputCompatible(DataType dtOut) {
        boolean result = false;
        
        switch (dtOut) {
            case RANGE:
            case VECTOR:
                result = true;
                break;
        }

        return result;
    }

}
