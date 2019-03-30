package org.ngi.nodes.transform;



import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeinputs.OperationInput;
import org.ngi.nodeoutputs.OperationOutput;



/**
 * Base class for all transforms taking x, y, and z expressions for their input.
 * An arguments mechanism is provided for subclasses - currently only Resize uses it for the auto argument.
 * 
 * @author Tom
 */
public class BaseXYZTransform extends Node {
    
    private NodeOutput _outOpp;
    
    protected NodeInput _inOpp;
    protected NodeInput _inExpX;
    protected NodeInput _inExpY;
    protected NodeInput _inExpZ;
    
     private String _transform;
     private String _arguments;
    
     
     /**
      * Constructor, initializes a transform node.
      * 
      * @param name  Name of Transform.
      * @param transform  OpenSCAD transform command as it would appear in OpenSCAD.
      * @param description Description of the transform.
      */
    public BaseXYZTransform( String name, String transform, String description) {
        super( name, description);
        
        _transform = transform;
        
        _arguments = "";
        
    }
    
    @Override
    protected void init() {
        
        _inOpp = new OperationInput(this,"G", "Geometry to apply transform to.");
        _inExpX = new ExpressionInput(this,"X", "Transform x value.");
        _inExpY = new ExpressionInput(this,"Y", "Transform y value.");
        _inExpZ = new ExpressionInput(this,"Z", "Transform z value.");
        
        _outOpp = new OperationOutput(this,"O", "Geometry with transform applied.");
        
        _inExpX.setStaticValue("0");
        _inExpY.setStaticValue("0");
        _inExpZ.setStaticValue("0");        
        
        addInput(_inOpp);
        addInput(_inExpX);
        addInput(_inExpY);
        addInput(_inExpZ);

        addOutput(_outOpp); 
        
        
    }

    @Override
    public boolean evaluate() {
        
        String outStr = "";
        String base = _inOpp.getInputValue();
      
        
        String x = _inExpX.getInputValue();
        String y = _inExpY.getInputValue();
        String z = _inExpZ.getInputValue();
                
        String args = "";
        if(!_arguments.isEmpty()) {
            args = ", " + _arguments;
        }
        
        base = InputUtilities.indentText(base);
        outStr = _transform + " ([" + x + "," + y + "," + z + "] " + args + ") {\n";
        outStr += base;
        outStr += "}\n";
        
        _outOpp.setOutputValue(outStr);
        
        return true;
    }

    protected final void setArguments(String args) {
        _arguments = args;
    }
    
}
