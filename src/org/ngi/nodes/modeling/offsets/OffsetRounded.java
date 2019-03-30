package org.ngi.nodes.modeling.offsets;



import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeinputs.OperationInput;
import org.ngi.nodeoutputs.OperationOutput;



/**
 *  Offset a curve using the "r" parameter - creating rounded corners.
 * @author Tom
 */
public class OffsetRounded extends Node {
    
    private NodeOutput _outCrv;
    
    protected NodeInput _inOpp;
    protected NodeInput _inRad;

    public OffsetRounded() {
        super( "OffsetRounded", "Offset a curve using rounded corners");   
    }
    
    @Override
    protected void init() {
        
        _inOpp = new OperationInput(this,"G", "Geometry (curve) to offset.");
        _inRad = new ExpressionInput(this,"R", "Distance to offset - also used as corner radius.");

        
        _outCrv = new OperationOutput(this,"C", "Offest Curve.");
        
        _inRad.setStaticValue("1");
       
        
        addInput(_inOpp);
        addInput(_inRad);

        addOutput(_outCrv); 
        
        
    }

    @Override
    public boolean evaluate() {
        
        String outStr = "";
        String base = _inOpp.getInputValue();
        String radius = _inRad.getInputValue();


        //indent the body
        base = InputUtilities.indentText(base);
        outStr = "offset( r = " + radius + ") {\n";
        outStr += base;
        outStr += "}\n";
        
        _outCrv.setOutputValue(outStr);
        
        return true;
    }


    
}
