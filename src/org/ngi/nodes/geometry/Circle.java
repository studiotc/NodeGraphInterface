package org.ngi.nodes.geometry;

import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.OperationOutput;


/**
 *  Creates a 2d circle.  Uses single radius input.
 * @author Tom
 */
public class Circle extends BaseMesh {
    
    private NodeInput _inRad;
    private NodeOutput _outSphere;
    
    public Circle( ) {
        super("Circle", "Circle geometry using radius.");
    }
    
    @Override
    public void init() {
        
      
        _inRad = new ExpressionInput(this,"R", "Radius of the circle.");
        _outSphere = new OperationOutput(this,"C", "Circle value.");
        
        _inRad.setStaticValue("10");
        
        addInput(_inRad);
        addOutput(_outSphere);
        
    }
    
    @Override
    public boolean evaluate() {
        
        String radius = _inRad.getInputValue();
        String outStr = "circle(r = " + radius + getFragementSpec() + ");";
        _outSphere.setOutputValue(outStr);
        
        return true;        
        
    }    
    
}
