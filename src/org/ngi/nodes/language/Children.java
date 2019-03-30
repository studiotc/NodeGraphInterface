
package org.ngi.nodes.language;

import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ChildrenInput;
import org.ngi.nodeoutputs.OperationOutput;



/**
 * Node for children() reference.  Supports simple children() reference or
 * can take an optional Input to specify an index, range, or vector for
 * children selection.
 * @author Tom
 */
public class Children extends BaseParameters {

    private NodeOutput _outOpp;
    private NodeInput _inIndex; 

    
    public Children() {
        super("Children", "Access child objects.");
  
    }    
    
    @Override
    protected void init() {
        super.init();
        
        _inIndex = null;
        
        _outOpp = new OperationOutput(this,"O", "Children statement.");
        

        
        addOutput(_outOpp);   
        
        
    }

    @Override
    public boolean evaluate() {
       
        
        String indexStr = getIndex();
        String outStr = "";

        outStr += "children(" + indexStr + ");";
        
        _outOpp.setOutputValue(outStr);
        
        return true;
    }
    
    
    @Override
    public boolean allowOptionalInputs() {
        return !hasIndex();
    }
    
    @Override
    public void addOptionalInput() {
        
        if(!hasIndex()) {
            
            _inIndex = new ChildrenInput(this, "I", "Index, Range, or Vector for child selection.");
            _inIndex.setAsOptional();
            addInput(_inIndex);            
            
        }    
        
    }   
    
    /**
     * Intercept remove input and flag our single optional input.
     * @param input NodeInput to remove
     */
    @Override
    public void removeInput(NodeInput input) {
        
        super.removeInput(input);
        
        if(input.equals(_inIndex)) {
            _inIndex  = null;          
        }
        
        
    }
    
    /**
     * Is there an index provided?
     * @return True if fragment specification is available.
     */
    protected boolean hasIndex() {
        return _inIndex != null;
    }
    
    
    /**
     * Get the index.
     * @return The string containing the index.
     */
    protected String getIndex() {
        String index = "";
        if(hasIndex()) {
           index =  _inIndex.getInputValue(); 
        }
        
        return index;
    }    
    
    
}
