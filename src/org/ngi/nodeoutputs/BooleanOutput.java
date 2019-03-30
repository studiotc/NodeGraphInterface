package org.ngi.nodeoutputs;

import org.ngi.Node;
import org.ngi.NodeOutput;
import org.ngi.enums.DataType;

/**
 *  Boolean output.  
 *  To keep the names pretty as a result of sub-classing inputs.
 *  Not really used for anything else...
 *  @author Tom
 */
public class BooleanOutput extends NodeOutput {
    
    public BooleanOutput(Node parent, String name, String description) {
        super( parent,  name, DataType.BOOLEAN, description);
    }
    
}
