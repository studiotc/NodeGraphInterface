package org.ngi.nodes.transform.xyz;

import org.ngi.nodes.transform.BaseXYZTransform;



/**
 * Rotate transform using x, y, and z values for input.
 * Rotates the geometry.
 * @author Tom
 */
public class RotateXYZ extends BaseXYZTransform {
    

    public RotateXYZ() {
        super("RotateXYZ", "rotate", "Rotate geometry with given x, y, and z values.");
    }
    
}//end class
