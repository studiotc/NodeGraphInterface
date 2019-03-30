package org.ngi.nodes.geometry;

import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.OperationOutput;


/**
 * Creates a sphere from a single radius input.
 * @author Tom
 */
public class Sphere extends BaseMesh {
    
    private NodeInput _inRad;
    private NodeOutput _outSphere;
    
    public Sphere( ) {
        super("Sphere", "Sphere geometry using radius.");
    }
    
    @Override
    public void init() {
        
      
        _inRad = new ExpressionInput(this,"R", "Radius of the sphere.");
        _outSphere = new OperationOutput(this,"S", "Sphere value.");
        
        _inRad.setStaticValue("10");
        
        addInput(_inRad);
        addOutput(_outSphere);
        
    }
    
    @Override
    public boolean evaluate() {
        
        String radius = _inRad.getInputValue();
        String outStr = "sphere(r = " + radius + getFragementSpec() + ");";
        _outSphere.setOutputValue(outStr);
        
        return true;        
        
    }    
    
}
