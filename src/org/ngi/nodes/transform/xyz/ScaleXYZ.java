package org.ngi.nodes.transform.xyz;

import org.ngi.nodes.transform.BaseXYZTransform;



/**
 * Scale transform using using x, y, and z values for input.
 * Scales the geometry.
 * @author Tom
 */
public class ScaleXYZ extends BaseXYZTransform {
    
    public ScaleXYZ() {
        super("ScaleXYZ", "scale" , "Scale geometry with given x, y, and z values.");
    }
    
    @Override
    protected void init() {
        super.init();
        
        _inExpX.setStaticValue("1");
        _inExpY.setStaticValue("1");
        _inExpZ.setStaticValue("1");        
        
        
    }    
    
}
