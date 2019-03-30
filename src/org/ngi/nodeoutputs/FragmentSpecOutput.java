package org.ngi.nodeoutputs;

import org.ngi.Node;
import org.ngi.NodeOutput;
import org.ngi.enums.DataType;

/**
 *  Fragment Specification output.  
 *  To keep the names pretty as a result of sub-classing inputs.
 *  Not really used for anything else...
 *  @author Tom
 */
public class FragmentSpecOutput extends NodeOutput {
    
    public FragmentSpecOutput(Node parent, String name, String description) {
        super( parent,  name, DataType.FRAGMENT_SPEC, description);
    }
    
}
