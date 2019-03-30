package org.ngi.nodes.datatypes;

import java.util.ArrayList;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.VectorInInput;
import org.ngi.nodeoutputs.VectorOutput;


/**
 * Vector Data type.  Creates a vector that can be made any size.  This node allows for any number of 
 * optional inputs - each one being a member of the vector list.
 * @author Tom
 */
public class Vector extends Node {
    
   
    private NodeOutput _outVec;
    
    public Vector() {
        super("Vector", "Vector of variable size.");
    }
    
    
    @Override
    protected void init() {
        

        VectorInInput nia = new VectorInInput(this, "a", "Vector component.");
        VectorInInput nib = new VectorInInput(this, "b", "Vector component.");
        VectorInInput nic = new VectorInInput(this, "c", "Vector component.");        
        
        _outVec = new VectorOutput(this, "V", "Vector value.");
        
        nia.setAsOptional();
        nib.setAsOptional();
        nic.setAsOptional();
        
        addInput(nia);
        addInput(nib);
        addInput(nic);
        
        
        addOutput(_outVec);        
        
    }    
      
   @Override
    public boolean evaluate() {
        
        String outStr = "[";
        
        ArrayList<NodeInput> inputs = this.getInputs();
        int icnt = inputs.size();
        
        for(int i =0; i < icnt; i++){
            NodeInput input = inputs.get(i);
            String curVal = input.getInputValue();
            if( i < icnt -1) {
                curVal += ", ";
            }
            outStr += curVal;
            
        }
        
        outStr += "]";
        
        _outVec.setOutputValue(outStr);
        return true; 
              
        
    }    
    
    
    @Override
    public boolean allowOptionalInputs() {
        return true;
    }
    
    @Override
    public void addOptionalInput() {
        
        NodeInput ni = new VectorInInput(this, "d", "Vector component.");
        ni.setAsOptional();
        addInput(ni);
        
    }    
    
    
    
}
