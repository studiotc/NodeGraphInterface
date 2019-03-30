package org.ngi.nodes.transform.xyz;


import org.ngi.NodeInput;
import org.ngi.nodeinputs.BooleanInput;
import org.ngi.nodes.transform.BaseXYZTransform;


/**
 * Resize transform using x, y, and z values for input.
 * Performs a resize on the geometry.  An Input is added for a simple auto value.
 * @author Tom
 */
public class ResizeXYZ extends BaseXYZTransform {
    
    private NodeInput _inAuto;

    public ResizeXYZ() {
        super( "ResizeXYZ", "resize", "Resize geometry using x,y, and z values.");
        setArguments("auto=true");
    }
    
    
   @Override
    public void init() {
        super.init();
        
        _inAuto = new BooleanInput(this,"A", "Use auto for 0 values.");
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
