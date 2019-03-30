package org.ngi.nodes.modeling;



import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeinputs.OperationInput;
import org.ngi.nodeinputs.VectorInput;
import org.ngi.nodeoutputs.OperationOutput;



/**
 *
 * @author Tom
 */
public class Color extends Node {
    
    private NodeOutput _outOpp;
    
    protected NodeInput _inOpp;
    protected NodeInput _inClr;
    protected NodeInput _inAlpha;
 

    
    public Color( ) {
        super( "Color", "Color statment - set the current color"); 
    }
    
    @Override
    protected void init() {
        
        _inOpp = new OperationInput(this,"G", "Operation (geometry or code block) to apply color to.");
        _inClr = new VectorInput(this,"V", "Vector containing r,g,b values of 0-1");
 
        _inAlpha = new ExpressionInput(this,"A", "Alpha value 0-1.");
        _inAlpha.setStaticValue("1");
        
        _outOpp = new OperationOutput(this,"O", "Color block.");
        
        
        addInput(_inOpp);
        addInput(_inClr);
        addInput(_inAlpha);
        addOutput(_outOpp); 
        
        
    }

    @Override
    public boolean evaluate() {
        
        String outStr = "";
        String base = _inOpp.getInputValue();
      
        
        String vec = _inClr.getInputValue();
        String alpha = _inAlpha.getInputValue();
        
        
        base = InputUtilities.indentText(base);
        //color( c = [r, g, b], alpha = 1.0 )
        outStr = " color( c = " + vec + ", alpha = "+ alpha +  ") {\n";
        outStr += base;
        outStr += "}\n";
        
        _outOpp.setOutputValue(outStr);
        
        return true;
    }


    
}
