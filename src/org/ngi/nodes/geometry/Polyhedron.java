package org.ngi.nodes.geometry;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeinputs.VectorInput;
import org.ngi.nodeoutputs.OperationOutput;

/**
 * Creates a Polyhedron from a list of vectors for points, and vectors for face indexes.
 * It also has an input for convexity for preview.
 * @author Tom
 */
public class Polyhedron extends Node {
    
    private NodeInput _inPoints;
    private NodeInput _inFaces;
    private NodeInput _inConvex;

    
    private NodeOutput _outPoly;    
    
    public Polyhedron() {
        super("Polyhedron", "Polyhedron geometry using vectors.");
    }
    
    
    @Override
    public void init() {
        
        _inPoints = new VectorInput(this,"P", "Vector of points.");
        _inFaces = new VectorInput(this,"F", "Vector of faces.");
        _inConvex = new ExpressionInput(this,"C", "Convexity for preview.");
        
        
        _outPoly = new OperationOutput(this,"O", "Polyhedron value.");
        
        
        addInput(_inPoints);
        addInput(_inFaces);
        addInput(_inConvex);
        
        addOutput(_outPoly);
        
    }
    
    @Override
    public boolean evaluate() {
        
        String pStr = _inPoints.getInputValue();
        String fStr = _inFaces.getInputValue();
        String cStr = _inConvex.getInputValue();
   

        String outStr = "polyhedron( points = " + pStr + ", faces = " + fStr + ", convexity = " + cStr +  ");";
        _outPoly.setOutputValue(outStr);

        return true;        
        
    }    
        
    
    
    
    
}
