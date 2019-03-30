
package org.ngi.nodes.conditionals;

import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.AnyInput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.OperationOutput;

/**
 * If statement for branching a block of statements.  Uses an expression input to test.
 * Allows for any input for the then or else portions.  the else part is an optional input.
 * @author Tom
 */
public class If extends Node {
    
    private NodeOutput _outOpp;
    
    protected NodeInput _inExpTest;
    protected NodeInput _inThenOpp;
     private NodeInput _inElseOpp;
    
    public If() {
        super("If", "Conditional if statement.");
        
    }
    
    
    @Override
    protected void init() {
        
        _inExpTest = new ExpressionInput(this,"C", "Conditional test.");
        
        _inThenOpp = new AnyInput(this,"T", "Then case of if statement.");
        
        _inElseOpp = null;
        
        _outOpp = new OperationOutput(this,"O", "Conditional result block.");
        
        _inExpTest.setStaticValue("1=1");
       
        
        addInput(_inExpTest);
        addInput(_inThenOpp);


        addOutput(_outOpp); 
        
        
    }
    
    @Override
    public boolean evaluate() {
        
        String testExp = _inExpTest.getInputValue();
        String thenStr = _inThenOpp.getInputValue();
        String elseStr = "";
        
        String outStr = "";
        
        thenStr = InputUtilities.indentText(thenStr);
        
         outStr = "if(" + testExp + ") {\n";        
        outStr += thenStr;
        outStr += "}\n";
        
        //check for else statement
        if(hasElseInput()) {
            elseStr = getElse();
            elseStr = InputUtilities.indentText(elseStr);
            
            outStr += "else {\n";
            outStr += elseStr;
            outStr += "}\n";
            
        }
        
        _outOpp.setOutputValue(outStr);
        
        return true;
    }
    
    @Override
    public boolean allowOptionalInputs() {
        return !hasElseInput();
    }
    
    @Override
    public void addOptionalInput() {
        
        if(!hasElseInput()) {
            
            _inElseOpp = new AnyInput(this, "E", "Else case of the if statement");
            _inElseOpp.setAsOptional();
            addInput(_inElseOpp);            
            
        }    
        
    }   
    
    /**
     * Intercept remove input and flag our 
     * @param input NodeINput to remove
     */
    @Override
    public void removeInput(NodeInput input) {
        
        super.removeInput(input);
        
        if(input.equals(_inElseOpp)) {
            _inElseOpp  = null;          
        }
        
        
    }
    
    /**
     * Is there an else input?
     * @return True if else input is present.
     */
    protected boolean hasElseInput() {
        return _inElseOpp != null;
    }    
    
    
    protected String getElse() {
        String elseStr = "";
        
        if(hasElseInput() ) {
            
            elseStr = _inElseOpp.getInputValue();
        }
        
        return elseStr;
    }
    
}
