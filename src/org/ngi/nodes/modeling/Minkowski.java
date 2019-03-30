
package org.ngi.nodes.modeling;

import org.ngi.nodes.modeling.booleans.BaseBoolean;



/**
 *  Hull operation.
 * @author Tom
 */
public class Minkowski extends BaseBoolean {
    
    /**
     * Constructor
     */
    public Minkowski() {
        super("Minkowski", "minkowski", "Performs Minkowski sum on the specified geometry.");
    }
    
    @Override
    protected String descObjA() {
        return "Minkowski base geometry."; 
    }
    
    @Override
    protected String descObjB() {
        return "Minkowski operation geometry."; 
    } 
    
    @Override
    protected String descObjOut() {
        return "Minkowski result geometry." ;       
    }    
    
}
