package org.ngi.nodes.modeling.extrudes;

import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeinputs.OperationInput;
import org.ngi.nodeoutputs.OperationOutput;

/**
 *
 * @author Tom
 */
public class RotateExtrude extends Node {
    

    private NodeInput _inOpp;
    private NodeInput _inAngle;
    private NodeInput _inConvex;
    
    private NodeOutput _outObj;
    
    public RotateExtrude () {
        super( "RotateExtrude", "Performs a rotational extrude on the specified curves.");
    }
    
    @Override
    public void init() {
       
        
        _inOpp = new OperationInput(this,"G", "Geometry to extrude.");
        
        _inAngle = new ExpressionInput(this,"A", "Angle of extrusion.");
        _inConvex = new ExpressionInput(this,"C", "Convexity");
        
        _inAngle.setStaticValue("360");
        _inConvex.setStaticValue("2");
        
        _outObj = new OperationOutput(this, "O", "Extruded geometry.");
        
        
        addInput(_inOpp);
        addInput(_inAngle);
        addInput(_inConvex);
        
        addOutput(_outObj);
        
        
    }
    
    @Override
    public boolean evaluate() {
        
        String outStr = "";
       
        String base = _inOpp.getInputValue();
      
        
        String angle = _inAngle.getInputValue();
        String convex = _inConvex.getInputValue();

        
        base = InputUtilities.indentText(base);
        outStr = "rotate_extrude( angle = " + angle + ", convexity  = " + convex  + ") {\n";
        outStr += base;
        outStr += "}\n";
        
        _outObj.setOutputValue(outStr);
        
        
          return true;   
    }    

    
 
 
    
    
}
