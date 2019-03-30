package org.ngi.nodes.transform.vector;

import org.ngi.nodes.transform.BaseVectorTransform;





/**
 *  Translate transform using a vector for input.
 * Translates (moves) the geometry.
 * @author Tom
 */
public class TranslateVector extends BaseVectorTransform {
    
    
    public TranslateVector() {
        super("TranslateVector", "translate", "Translate geometry using vector input for x,y,z values.");
 
    }
    
    
}
