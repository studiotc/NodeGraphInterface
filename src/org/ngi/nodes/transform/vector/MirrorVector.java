package org.ngi.nodes.transform.vector;

import org.ngi.nodes.transform.BaseVectorTransform;



/**
 * Mirror transform using a vector for input.
 * Mirrors the geometry across the vector.
 * @author Tom
 */
public class MirrorVector extends BaseVectorTransform {
    
    
    public MirrorVector() {
        super("MirrorVector", "mirror", "Mirror geometry using vector input for mirror vector.");
 
    }
    
    
}
