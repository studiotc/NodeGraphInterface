package org.ngi.nodes.transform.vector;


import org.ngi.NodeInput;
import org.ngi.nodeinputs.BooleanInput;
import org.ngi.nodes.transform.BaseVectorTransform;


/**
 * Resize transform using a vector for input.
 * Performs a resize on the geometry.  An Input is added for a simple auto value.
 * @author Tom
 */
public class ResizeVector extends BaseVectorTransform {
    
    private NodeInput _inAuto;

    public ResizeVector() {
        super( "ResizeVector", "resize", "Resize geometry using vector input for x,y,z values.");
        setArguments("auto=true");
    }
    
    
   @Override
    public void init() {
        super.init();
        
        _inAuto = new BooleanInput(this,"A", "Auto scale 0 values.");
         _inAuto.setStaticValue("true");
        addInput(_inAuto);
        
    } 
    
    @Override
    public boolean evaluate() {
        
        String autoStr = "auto = " + _inAuto.getInputValue();
        setArguments(autoStr);
        return super.evaluate();
    }    
    
    
    
}//end class
