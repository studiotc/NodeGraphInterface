package org.ngi.nodes.transform.vector;

import org.ngi.nodes.transform.BaseVectorTransform;


/**
 * Scale transform using a vector for input.
 * Scales the geometry.
 * @author Tom
 */
public class ScaleVector extends BaseVectorTransform {
    
    public ScaleVector() {
        super("ScaleVector", "scale", "Scale geometry using vector input for x,y,z values.");
    }
    
    
}
