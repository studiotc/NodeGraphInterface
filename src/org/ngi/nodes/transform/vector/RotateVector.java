package org.ngi.nodes.transform.vector;

import org.ngi.nodes.transform.BaseVectorTransform;




/**
 * Rotate transform using a vector for input.
 * Rotates the geometry.
 * @author Tom
 */
public class RotateVector extends BaseVectorTransform {
    

    public RotateVector() {
        super("RotateVector", "rotate", "Rotate geometry using vector input for x,y,z values..");
    }
    
}//end class
