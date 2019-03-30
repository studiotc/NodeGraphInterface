
package org.ngi.nodes.modeling.extrudes;

import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.BooleanInput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeinputs.OperationInput;
import org.ngi.nodeoutputs.OperationOutput;

/**
 *
 * @author Tom
 */
public class LinearExtrude extends Node {
    

 
    private NodeInput _inOpp;
    private NodeInput _inHeight;
    
     private NodeInput _inTwist;
     
     private NodeInput _inSlice;
     
     private NodeInput _inScale;
    
    private NodeInput _inCen;
    
    private NodeOutput _outObj;
    
    public LinearExtrude () {
        super( "LinearExtrude", "Performs a linear extrude on the specified curves.");
    }
    
    @Override
    public void init() {

        
        _inOpp = new OperationInput(this,"G", "Geometry to extrude.");
        
        _inHeight = new ExpressionInput(this,"H", "Height of extrusion.");
        _inTwist = new ExpressionInput(this, "T", "Twist value.");
        _inSlice = new ExpressionInput(this, "L", "Number of slices.");
        _inScale = new ExpressionInput(this, "S", "Curve scale over height.");
        _inCen = new BooleanInput(this,"C", "Center the height: half height +/-, False = height +.");
        
        _inScale.setStaticValue("1");
        
        _outObj = new OperationOutput(this, "O", "Extruded geometry.");
        
        
        
        addInput(_inOpp);
        addInput(_inHeight);
        addInput(_inTwist);
        addInput(_inSlice);
        addInput(_inScale);
        addInput(_inCen);
        
        addOutput(_outObj);
        
        
    }
    
    @Override
    public boolean evaluate() {
        
        String outStr = "";
       
        String hStr = _inHeight.getInputValue();
        String base = _inOpp.getInputValue();
      
        
        String height = _inHeight.getInputValue();
        String cen = _inCen.getInputValue();
        String twist = _inTwist.getInputValue();
        String slice = _inSlice.getInputValue();
        String scale = _inScale.getInputValue();
        
        base = InputUtilities.indentText(base);
        outStr = "linear_extrude( height = " + height + ", center = " + cen + ", twist = " + twist + ", slices = " + slice + ", scale = " + scale +") {\n";
        outStr += base;
        outStr += "}\n";
        
        _outObj.setOutputValue(outStr);
        
        
          return true;   
    }    

    
}
