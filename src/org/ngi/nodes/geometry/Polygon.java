package org.ngi.nodes.geometry;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeinputs.VectorInput;
import org.ngi.nodeoutputs.OperationOutput;

/**
 *  Creates a polygon from a vector of 2d points.  It also has an input for convexity for preview.
 * @author Tom
 */
public class Polygon extends Node {
    
    private NodeInput _inPoints;
     private NodeInput _inPaths;
     
    private NodeInput _inConvex;

    
    private NodeOutput _outPoly;    
    
    public Polygon() {
        super("Polygon", "Polygon geometry.");
    }
    
    
    @Override
    public void init() {
        
        //paths is optional
        _inPaths = null;
        
        _inPoints = new VectorInput(this,"V", "Vector of vertices (2d points).");
        _inConvex = new ExpressionInput(this,"C", "Convexity for preview.");
        
        _outPoly = new OperationOutput(this,"O", "Polygon value.");
        
        
        addInput(_inPoints);
        addInput(_inConvex);
        
        addOutput(_outPoly);
        
    }
    
    @Override
    public boolean evaluate() {
        
        String pStr = _inPoints.getInputValue();
  
        String cStr = _inConvex.getInputValue();
   

        String outStr = "polygon( points = " + pStr + getPathsString() + ", convexity = " + cStr +  ");";
        _outPoly.setOutputValue(outStr);

        return true;        
        
    }    
        
    @Override
    public boolean allowOptionalInputs() {
        return !pathsPresent();
    }
    
    @Override
    public void addOptionalInput() {
        
        if(!pathsPresent()) {
            
            _inPaths = new VectorInput(this,"P", "Vector of paths.");

            _inPaths.setAsOptional();
            addInput(_inPaths);            
            
        }    
        
    }   
    
    /**
     * Intercept remove input and set Paths Input null. 
     * @param input NodeInput to remove (Paths is only option input)
     */
    @Override
    public void removeInput(NodeInput input) {
        
        super.removeInput(input);
        
        if(input.equals(_inPaths)) {
            _inPaths  = null;          
        }
        
        
    }
    
    /**
     * Is there a Paths Input?
     * @return True if Paths Input is present.
     */
    protected boolean pathsPresent() {
        return _inPaths != null;
    }
    
    
    /**
     * Get the Paths Vector.
     * @return The string containing the Paths vector.
     */
    protected String getPathsString() {
        String paths = "";
        if(pathsPresent()) {
            
           paths = ", paths = " + _inPaths.getInputValue();
            
        }
        return paths;
    }    
    
    
    
}