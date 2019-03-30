
package org.ngi.nodes.datatypes;


import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.RangeOutput;



/**
 * Range2 data type.  Creates a range using a start and end number.
 * @author Tom
 */
public class Range2 extends Node {
    
    private NodeInput _inStart;
    private NodeInput _inEnd;
    
    private NodeOutput _outRng;
  
    public Range2() {
        super("Range2", "Range from start to end.");
       
    }

    
    @Override
    protected void init() {
        
 
        _inStart = new ExpressionInput(this,"S", "Range start value.");
        _inEnd = new ExpressionInput(this,"E", "Range end value.");
        
        
        _outRng = new RangeOutput(this, "R", "Range value.");
        
        _inStart.setStaticValue("0");
        _inEnd.setStaticValue("9");

        
        addInput(_inStart);
        addInput(_inEnd);
        
        addOutput(_outRng); 
        
    }    
    
    @Override
    public boolean evaluate() {
        
   
        String sStr = _inStart.getInputValue();
        String eStr = _inEnd.getInputValue();

        String outStr = "[" + sStr + " : "  + eStr + "]";
        
        _outRng.setOutputValue(outStr);
        return true;        

     
        
    }    
    
}
