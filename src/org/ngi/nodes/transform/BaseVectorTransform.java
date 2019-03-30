package org.ngi.nodes.transform;



import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.OperationInput;
import org.ngi.nodeinputs.VectorInput;
import org.ngi.nodeoutputs.OperationOutput;



/**
 *  Base class for all transforms taking a vector for their input.
 *  An arguments mechanism is provided for subclasses - currently only Resize uses it for the auto argument.
 * @author Tom
 */
public class BaseVectorTransform extends Node {
    
    private NodeOutput _outOpp;
    
    protected NodeInput _inOpp;
    protected NodeInput _inVec;
 
     private String _transform;

     private String _arguments;
    
     
     /**
      * Constructor, initializes a transform node.
      * 
      * @param name  Name of Transform.
      * @param transform  OpenSCAD transform command as it would appear in OpenSCAD.
      * @param description Description of the transform.
      */
    public BaseVectorTransform( String name, String transform, String description) {
        super( name, description);
        
        _transform = transform;
        
        _arguments = "";
        
    }
    
    @Override
    protected void init() {
        
        _inOpp = new OperationInput(this,"G", "Geometry to apply transform to.");
        _inVec = new VectorInput(this,"V", "Vector with x,y,z values for transform.");
 
        
        _outOpp = new OperationOutput(this,"O", "Geometry with transform applied.");
        
        
        addInput(_inOpp);
        addInput(_inVec);
        addOutput(_outOpp); 
        
        
    }

    @Override
    public boolean evaluate() {
        
        String outStr = "";
        String base = _inOpp.getInputValue();
 
        String vec = _inVec.getInputValue();
         
        String args = "";
        if(!_arguments.isEmpty()) {
            args = ", " + _arguments;
        }        
        
        base = InputUtilities.indentText(base);
        outStr = _transform + " (" + vec +  args + ") {\n";
        outStr += base;
        outStr += "}\n";
        
        _outOpp.setOutputValue(outStr);
        
        return true;
    }

     protected final void setArguments(String args) {
        _arguments = args;
    }
    
}
