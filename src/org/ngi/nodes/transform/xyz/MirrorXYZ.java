package org.ngi.nodes.transform.xyz;

import org.ngi.nodes.transform.BaseXYZTransform;





/**
 * Mirror transform using  x,y, and z values for input.
 * Mirrors the geometry across the vector.
 * @author Tom
 */
public class MirrorXYZ extends BaseXYZTransform {
    
    
    public MirrorXYZ() {
        super("MirrorXYZ", "mirror", "Mirror geometry with given x, y, and z values for mirror vector.");
 
    }
    
    @Override
    protected void init() {
        super.init();
        
        _inExpX.setStaticValue("0");
        _inExpY.setStaticValue("0");
        _inExpZ.setStaticValue("0");        
        
        
    }
    
}
