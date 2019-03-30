
package org.ngi.nodes.language;

import java.util.ArrayList;
import org.ngi.InputUtilities;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.OperationInput;
import org.ngi.nodeinputs.VariableInput;
import org.ngi.nodeoutputs.OperationOutput;



/**
 *  Defines a module.   Allows for additional optional inputs to define module arguments.
 * @author Tom
 */
public class ModuleDef extends BaseParameters {

    private NodeOutput _outFunc;
    private NodeInput _inName; 
    private NodeInput _inBody;
    
    private ArrayList<NodeInput> _arguments;
    
    public ModuleDef() {
        super("ModuleDef", "Module definition.");
  
    }    
    
    @Override
    protected void init() {
        super.init();
        
        _inName = new VariableInput(this,"N", "Name of the module");
        _inBody = new OperationInput(this,"B", "Body of the module");
        
        _outFunc = new OperationOutput(this,"M", "Module definition.");
        
        
        addInput(_inBody);
        addInput(_inName);
        
        addOutput(_outFunc);   
        
        
    }

    @Override
    public boolean evaluate() {
       
        
        String name = _inName.getInputValue();
        String body = _inBody.getInputValue();
        String outStr = "";
        
        //get parameters or empty string
        String argStr = getParameters();
        
        //format body
        body = InputUtilities.indentText(body);

        outStr = "module " + name + "(" + argStr + ") {\n"; 
        outStr += body;
        outStr += "}\n";
        
        _outFunc.setOutputValue(outStr);
        
        return true;
    }
    
    
    
    
}
