package org.ngi.nodes.geometry;

import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.BooleanInput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.OperationOutput;


/**
 *  Creates a 2d square (rectangle).  It uses an input for width and one for length.
 * It also has an input for center to created the square centered on the origin.
 * @author Tom
 */
public class Square extends BaseMesh {
    
    private NodeInput _inX;
    private NodeInput _inY;
    private NodeInput _inCen;
    
    private NodeOutput _outCube;
    
    public Square( ) {
        super("Square", "Square geometry using width and length.");
    }
    
    @Override
    public void init() {
        super.init();
        
        _inX = new ExpressionInput(this,"W", "Width of the square.");
        _inY = new ExpressionInput(this,"L", "Length of the square.");
        _inCen = new BooleanInput(this,"C", "Center the square at origin.");
        
        _outCube = new OperationOutput(this,"S", "Square value.");
        
        _inX.setStaticValue("10");
        _inY.setStaticValue("10");
        _inCen.setStaticValue("true");
        
        addInput(_inX);
        addInput(_inY);
        addInput(_inCen);
        
        addOutput(_outCube);
        
    }
    
    @Override
    public boolean evaluate() {
        
        String xStr = _inX.getInputValue();
        String yStr = _inY.getInputValue();
        String cenStr = _inCen.getInputValue();

        String sizeStr = "[" + xStr + ", " + yStr +  "]";
        String outStr = "square(" + sizeStr + ", center = " + cenStr + " " + getFragementSpec() + ");";
        _outCube.setOutputValue(outStr);

        return true;        
        
    }    
    
}
