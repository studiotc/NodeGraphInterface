package org.ngi.nodes.datatypes;



import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.StaticNumberInput;
import org.ngi.nodeoutputs.NumberOutput;




/**
 *  Number data type.  Number can be an integer of float.
 * @author Tom
 */
public class Number extends Node {
        
    private NodeInput _inNum;
    private NodeOutput _outNum;
  
    public Number() {
        super( "Number" , "A number, float or integer.");
       
    }

    
    @Override
    protected void init() {
        

        _inNum = new StaticNumberInput(this,"n", "Static number value.");
        _outNum = new NumberOutput(this, "N", "Number value.");
        
        addInput(_inNum);
        addOutput(_outNum); 
        
    }    
    
    @Override
    public boolean evaluate() {
        
        String nStr = _inNum.getInputValue();
        _outNum.setOutputValue(nStr); 
        
        //show the number value
        _inNum.setDisplayName(nStr);
        
        return true;
                
        
    }
    
    
  

    @Override
    protected void setErrorState(String msg) {
        super.setErrorState(msg); //To change body of generated methods, choose Tools | Templates.
        
        String nStr = _inNum.getInputValue();
        //show the number value
        _inNum.setDisplayName(nStr);
        
    }

    
    
 
    
    
}
