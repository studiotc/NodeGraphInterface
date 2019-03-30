
package org.ngi.nodes.language;

import java.util.ArrayList;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.nodeinputs.ExpressionInput;



/**
 *  Base class for nodes (Function/Module - define/call) that
 *  that allows for optional parameters.  This class provides all the optional inputs
 *  to be used for Function or Module arguments.
 * @author Tom
 */
public class BaseParameters extends Node {


    private ArrayList<NodeInput> _arguments;
    
    public BaseParameters(String name, String description) {
        super( name, description);
  
    }    
    
    @Override
    protected void init() {
        _arguments = new ArrayList<>(); 
    }

    
    @Override
    public boolean evaluate() { 
        return false;
    }    
    
    /**
     * Get the parameters in comma delineated string form.
     * @return Formated list of arguments.
     */
    protected String getParameters() {
        String argStr = "";
        
        int alen = _arguments.size();
        for(int i = 0;i < alen; i++) {
            NodeInput input = _arguments.get(i);
            argStr += input.getInputValue();
            
            if(i < alen - 1) {
                argStr += ", ";
            }
            
        }        
        
        return argStr;
    }

    @Override
    public boolean allowOptionalInputs() {
        return true;
    }
    
    @Override
    public void addOptionalInput() {
        
        NodeInput a = new ExpressionInput(this, "A", "Argument expression.");
        a.setAsOptional();
        addInput(a);
        
        _arguments.add(a);
        
    }    
    
    @Override
    public void removeInput(NodeInput input) {
        
        super.removeInput(input);
        //remove from arguments
        _arguments.remove(input);
 
    }    
    
}
