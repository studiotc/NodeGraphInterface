package org.ngi.nodeoutputs;

import org.ngi.Node;
import org.ngi.NodeOutput;
import org.ngi.enums.DataType;

/**
 *  Vector output.  
 *  To keep the names pretty as a result of sub-classing inputs.
 *  Not really used for anything else...
 *  @author Tom
 */
public class VectorOutput extends NodeOutput {
    
    public VectorOutput(Node parent, String name, String description) {
        super( parent,  name, DataType.VECTOR, description);
    }
    
}
