
package org.ngi.nodes.geometry;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.nodeinputs.FragmentSpecInput;


/**
 * 
 * SuperClass for 3d Geometry to allow a fragment specification ($fa, $fs and $fn).
 * FragementSpec input is set through an optional input.
 *
 * @author Tom
 */
public class BaseMesh extends Node {
    
    private NodeInput _inFragSpec;
 

    
    public BaseMesh( String name, String desc) {
        super( name, desc);
    }
    
    @Override
    public void init() {
        _inFragSpec = null;
    }
    
    @Override
    public boolean evaluate() {
          return false;   
    }    

    
    @Override
    public boolean allowOptionalInputs() {
        return !hasFragmentSpecification();
    }
    
    @Override
    public void addOptionalInput() {
        
        if(!hasFragmentSpecification()) {
            
            _inFragSpec = new FragmentSpecInput(this, "FS", "Fragment specification for meshing control.");
            _inFragSpec.setAsOptional();
            addInput(_inFragSpec);            
            
        }    
        
    }   
    
    /**
     * Intercept remove input and flag our 
     * @param input NodeINput to remove
     */
    @Override
    public void removeInput(NodeInput input) {
        
        super.removeInput(input);
        
        if(input.equals(_inFragSpec)) {
            _inFragSpec  = null;          
        }
        
        
    }
    
    /**
     * Is there A Fragment Specification?
     * @return True if fragment specification is available.
     */
    protected boolean hasFragmentSpecification() {
        return _inFragSpec != null;
    }
    
    
    /**
     * Get the Fragment Specification.
     * @return The string containing the fragment specification.
     */
    protected String getFragementSpec() {
        String spec = "";
        if(hasFragmentSpecification()) {
            
           spec = ", " + _inFragSpec.getInputValue();
            
        }
        return spec;
    }
    
    
}
