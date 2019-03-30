package org.ngi.nodes.datatypes;


import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.RangeOutput;



/**
 * Range3 data type.  Creates a range using a start number, increment, and end number.
 * @author Tom
 */
public class Range3 extends Node {
    
    private NodeInput _inStart;
    private NodeInput _inInc;
    private NodeInput _inEnd;
    
    private NodeOutput _outRng;
  
    public Range3() {
        super( "Range3", "Range from start to end with a step increment.");
       
    }

    
    @Override
    protected void init() {
        

        _inStart = new ExpressionInput(this,"S", "Range start value.");
        _inInc = new ExpressionInput(this,"I", "Range increment value."); 
        _inEnd = new ExpressionInput(this,"E", "Range end value.");        
        
        _outRng = new RangeOutput(this, "R", "Range value.");
        
        _inStart.setStaticValue("0");
        _inInc.setStaticValue("1");
        _inEnd.setStaticValue("9");

        
        addInput(_inStart);
        addInput(_inInc);
        addInput(_inEnd);
        
        addOutput(_outRng); 
        
    }    
    
    @Override
    public boolean evaluate() {
        
    
        String sStr = _inStart.getInputValue();
        String iStr = _inInc.getInputValue();
        String eStr = _inEnd.getInputValue();
        
        String outStr = "[" + sStr + " : " + iStr + " : " + eStr + "]";
        
        _outRng.setOutputValue(outStr);
        return true;
        
       
     
        
    }    
    
}
