package org.ngi.nodes.modeling.offsets;



import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.BooleanInput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeinputs.OperationInput;
import org.ngi.nodeoutputs.OperationOutput;



/**
 * Offset a curve using the "delta" parameter - creating straight or chamfered corners (if chafer = true).
 * @author Tom
 */
public class OffsetLinear extends Node {
    
    private NodeOutput _outCrv;
    
    protected NodeInput _inOpp;
    protected NodeInput _inDist;
    protected NodeInput _inChamfer;

    public OffsetLinear() {
        super( "OffsetLinear", "Offset a curve using straight or chamfered corners");   
    }
    
    @Override
    protected void init() {
        
        _inOpp = new OperationInput(this,"G", "Geometry (curve) to offset.");
        _inDist = new ExpressionInput(this,"R", "Distance to offset - also used as corner radius.");

        _inChamfer = new BooleanInput(this,"C","Chamfer corners.");
        
        _outCrv = new OperationOutput(this,"C", "Offest Curve.");
        
        _inDist.setStaticValue("1");
       _inChamfer.setStaticValue("false");
        
        addInput(_inOpp);
        addInput(_inDist);
        addInput(_inChamfer);
        
        addOutput(_outCrv); 
        
        
    }

    @Override
    public boolean evaluate() {
        
        String outStr = "";
        String base = _inOpp.getInputValue();
        String dist = _inDist.getInputValue();
        String chamfer = _inChamfer.getInputValue();

        //indent the body
        base = InputUtilities.indentText(base);
        outStr = "offset( delta = " + dist + ", chamfer = " + chamfer + ") {\n";
        outStr += base;
        outStr += "}\n";
        
        _outCrv.setOutputValue(outStr);
        
        return true;
    }


    
}
