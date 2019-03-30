
package org.ngi.nodes.modeling;

import org.ngi.nodes.modeling.booleans.BaseBoolean;



/**
 *  Hull operation.
 * @author Tom
 */
public class Hull extends BaseBoolean {
    
    /**
     * Constructor
     */
    public Hull() {
        super("Hull", "hull", "Creates a hull around the specified geometry.");
    }
    
    @Override
    protected String descObjA() {
        return "Hull operation geometry."; 
    }
    
    @Override
    protected String descObjB() {
        return "Hull operation geometry."; 
    } 
    
    @Override
    protected String descObjOut() {
        return "Hull result geometry." ;       
    }     
    
    
}
