
package org.ngi.nodes.language;


import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.AssignmentInput;
import org.ngi.nodeinputs.BooleanInput;
import org.ngi.nodeinputs.ConnVariableInput;
import org.ngi.nodeoutputs.ExpressionOutput;


/**
 *  Assignment, (=) Operator.  Used to set variable values i.e. a=1, either on a single line or
 *  to set default values in Function or Module definitions: i.e. myModule(radius=3) {...}.
 * The node also has a boolean input to control whether a semi-colon is added to the end or not.
 * 
 * @author Tom
 */
public class Assignment extends Node {
    
    private NodeInput _inVar;
    private NodeInput _inValue;
     private NodeInput _inUseLE;
    private NodeOutput _outExp;
    
    
    /**
     * Constructor
     * 
     */
    public Assignment() {
        super("Assignment", "Assign a value to a variable (=).");
    }
    
    
    @Override
    protected void init() {
        
        _inVar = new ConnVariableInput(this, "V", "Variable name for assignment.");
        _inValue = new AssignmentInput(this, "X", "Expression value for variable.");
        _inUseLE = new BooleanInput(this, "SC", "Include a semi-colon (;) at end of output.");
        _outExp = new ExpressionOutput(this, "O", "Variable assignment statement.");
        
        //_inUseLE.setStaticValue("false");
        
        addInput(_inVar);
        addInput(_inValue);
        addInput(_inUseLE);
        addOutput(_outExp);        
        
    }    
      
   @Override
    public boolean evaluate() {
        
        String varName = _inVar.getInputValue();
        String varExp = _inValue.getInputValue();

        String leBool = _inUseLE.getInputValue();
        boolean useLE = leBool.equals("true") ? true : false;
        
        String outStr = varName + " = " + varExp;
        //use line ending
        if(useLE)outStr += ";";
        
        _outExp.setOutputValue(outStr);
        
         return true;
         
        
    }    
    
}
