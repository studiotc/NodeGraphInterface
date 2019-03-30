package org.ngi.nodes.geometry;

import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.BooleanInput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.OperationOutput;


/**
 * Creates a Cube.  Uses inputs for width, length, and height of cube.  
 * It also provides a boolean input to create the cube centered.
 * 
 * @author Tom
 */
public class Cube extends BaseMesh {
    
    private NodeInput _inX;
    private NodeInput _inY;
    private NodeInput _inZ;
    private NodeInput _inCen;
    
    private NodeOutput _outCube;
    
    public Cube( ) {
        super("Cube", "Cube geometry using width, length, and height.");
    }
    
    @Override
    public void init() {
        super.init();
        
        _inX = new ExpressionInput(this,"W", "Width of the cube.");
        _inY = new ExpressionInput(this,"L", "Length of the cube.");
        _inZ = new ExpressionInput(this,"H", "Height of the cube.");
        _inCen = new BooleanInput(this,"C", "Center the cube at origin.");
        
        _outCube = new OperationOutput(this,"C", "Cube value.");
        
        _inX.setStaticValue("10");
        _inY.setStaticValue("10");
        _inZ.setStaticValue("10");
        _inCen.setStaticValue("true");
        
        addInput(_inX);
        addInput(_inY);
        addInput(_inZ);
        addInput(_inCen);
        
        addOutput(_outCube);
        
    }
    
    @Override
    public boolean evaluate() {
        
        String xStr = _inX.getInputValue();
        String yStr = _inY.getInputValue();
        String zStr = _inZ.getInputValue();
        String cenStr = _inCen.getInputValue();

        String sizeStr = "[" + xStr + ", " + yStr + ", " + zStr + "]";
        String outStr = "cube(" + sizeStr + ", center = " + cenStr + " " + getFragementSpec() + ");";
        _outCube.setOutputValue(outStr);

        return true;        
        
    }    
    
}
