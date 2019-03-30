package org.ngi.nodes.geometry;

import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.BooleanInput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.OperationOutput;


/**
 * Creates a cylinder.  Uses inputs for top radius, bottom radius, and height.  Cones can be made.
 * It also provides a boolean input to create the cylinder centered (centered z value only).
 * @author Tom
 */
public class Cylinder extends BaseMesh {
    
    private NodeInput _inR1;
    private NodeInput _inR2;
    private NodeInput _inH;
    private NodeInput _inCen;
    
    private NodeOutput _outCylinder;
    
    public Cylinder( ) {
        super("Cylinder", "Cylinder geometry using radius for top and bottom (for cones) and height.");
    }
    
    @Override
    public void init() {
        super.init();
        
        _inR1 = new ExpressionInput(this,"R1", "Bottom radius of cylinder (cone).");
        _inR2 = new ExpressionInput(this,"R2", "Top radius of cylinder (cone).");
        _inH = new ExpressionInput(this,"H", "Height of the cylinder.");     
        _inCen = new BooleanInput(this,"C", "Center the height at origin (z+/z-), False = z+ height.");
 
        _outCylinder = new OperationOutput(this,"C", "Cylinder value.");
        
        _inR1.setStaticValue("5");
        _inR2.setStaticValue("5");
        _inH.setStaticValue("10");
        _inCen.setStaticValue("true");
        
        addInput(_inR1);
        addInput(_inR2);
        addInput(_inH);
        addInput(_inCen);
        
        addOutput(_outCylinder);
        
    }
    
    @Override
    public boolean evaluate() {
        
        String r1Str = _inR1.getInputValue();
        String r2Str = _inR2.getInputValue();
        String hStr = _inH.getInputValue();
       String cenStr = _inCen.getInputValue();

        String sizeStr = "r1 = " + r1Str + ", r2 = " + r2Str + ", h = " + hStr;
        String outStr = "cylinder(" + sizeStr + ", center = " + cenStr + " " + getFragementSpec() + " );";
        _outCylinder.setOutputValue(outStr);

        return true;        
        
    }    
    
}
