package org.ngi.nodes.transform.xyz;

import org.ngi.nodes.transform.BaseXYZTransform;



/**
 * Translate transform using using x, y, and z values for input.
 * Translates (moves) the geometry.
 * @author Tom
 */
public class TranslateXYZ extends BaseXYZTransform {
    
    
    public TranslateXYZ() {
        super("TranslateXYZ", "translate", "Translate geometry with given x, y, and z values.");
 
    }
    
    @Override
    protected void init() {
        super.init();
        
        _inExpX.setStaticValue("0");
        _inExpY.setStaticValue("0");
        _inExpZ.setStaticValue("0");        
        
        
    }
    
}
