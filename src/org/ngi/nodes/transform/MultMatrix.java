package org.ngi.nodes.transform;



/**
 *  Multiply Matrix transform with vector input.  transforms geometry with the give 4x4 matrix.
 * Vector is in the form of: [[a,b,c,d],[a,b,c,d],[a,b,c,d],[a,b,c,d]].
 * @author Tom
 */
public class MultMatrix extends BaseVectorTransform {
    
    
    public MultMatrix() {
        super("MultMatrix", "multmatrix", "Transform geometry using vector input as a transformation matrix.");
 
    }
    
    
}
