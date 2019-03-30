
package org.ngi.nodes.modeling.booleans;

import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.OperationInput;
import org.ngi.nodeoutputs.OperationOutput;


/**
 *  Generic Boolean Node does all the basic work of any boolean node.
 * Allows for adding inputs, but preserves the basic two.
 * @author Tom
 */
public class BaseBoolean extends Node {
    
    private NodeInput _inA;
    private NodeInput _inB;
    private NodeOutput _outBool;    
    
    private String _operation;
      /**
     * Constructor
     *
     * 
     * @param name Name of the node.
     * @param operation  Boolean operation to perform
     * @param description Description of the Node.
     */
    public BaseBoolean(  String name, String operation, String description) {
        super( name, description);

        _operation = operation;

    }  
    
   @Override
    protected void init() {

        _inA = new OperationInput(this, "A", descObjA());
        _inB = new OperationInput(this, "B", descObjB());
        _outBool = new OperationOutput(this, "O", descObjOut() );


        addInput(_inA);
        addInput(_inB);
        addOutput(_outBool);

    }

    @Override
    public boolean evaluate() {


        String outStr = _operation + "(){\n";
        String body = "";
       
       for(NodeInput input : this.getInputs()) {
            String curBlk = input.getInputValue();
            curBlk = InputUtilities.indentText(curBlk);
            body += curBlk;// + "\n";
        }
        

       //merge & close the braces
        outStr += body + "}\n";        

        
        _outBool.setOutputValue(outStr);

        return true;

    }   
    
    
    @Override
    public boolean allowOptionalInputs() {
        return true;
    }
    
    @Override
    public void addOptionalInput() {
        
        NodeInput ni = new OperationInput(this, "C", descObjB() );
        ni.setAsOptional();
        addInput(ni);
        
    }    
    
    /**
     * Method for Input descriptions so that the messages 
     * can be changed in subclasses to match that class.
     * @return The description of the Input.
     */
    protected String descObjA() {
        return "Boolean base geometry."; 
    }
    
   /**
     * Method for Input descriptions so that the messages 
     * can be changed in subclasses to match that class.
     * @return The description of the Input.
     */    
    protected String descObjB() {
        return "Boolean operation geometry."; 
    } 
    
   /**
     * Method for Input descriptions so that the messages 
     * can be changed in subclasses to match that class.
     * @return The description of the Input.
     */    
    protected String descObjOut() {
        return "Boolean geometry." ;       
    }
    
}
