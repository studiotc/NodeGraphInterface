package org.ngi.nodes.math.operators;


import org.ngi.NodeInput;


import org.ngi.nodes.math.BaseOperator;

/**
 *  Divide Operator (/).  Divides the first number (A) by the second (B).
 * @author Tom
 */
public class Divide  extends BaseOperator {

    
    
    /**
     * Constructor
     * 
     */
    public Divide() {
        super( "Divide", "/", "Division operator (/)");
    }
    

   @Override
    public boolean evaluate() {
        boolean superEval = super.evaluate();
        
        //divide by zero check
        if(superEval) {
           
            NodeInput inB = getB();
            String inBstr = inB.getInputValue();
            inBstr = inBstr.trim();

            if(inBstr.equals("0")) {
                inB.setError("Divide by Zero.");
                return false;

            }            
            
            
        }
        

        return superEval; 
        
    }     
}
