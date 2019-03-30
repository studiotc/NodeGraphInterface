
package org.ngi.nodes.geometry;


import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.StaticNumberInput;
import org.ngi.nodeoutputs.FragmentSpecOutput;



/**
 *  Node for specifying fragments during mesh creation (or curves). 
 * Has input for number ($fn), angle ($fa), and size($fs).
 * @author Tom
 */
public class FragmentSpec extends Node {
    
    private NodeInput _inFN;
    private NodeInput _inFA;
    private NodeInput _inFS;

    
    private NodeOutput _outSpec;
    
    public FragmentSpec( ) {
        super("Fragment Spec", "Fragement specification for controlling how geometry is meshed.");
    }
    
    @Override
    public void init() {
        
        _inFN = new StaticNumberInput(this,"$fn", "Number of divisions.");
        _inFA = new StaticNumberInput(this,"$fa", "Minimum angle.");
        _inFS = new StaticNumberInput(this,"$fs", "Minimum size.");
        
        _outSpec = new FragmentSpecOutput(this,"FS", "Fragment specification for meshing control.");
        
        _inFN.setStaticValue("0");
        _inFA.setStaticValue("12");
        _inFS.setStaticValue("2");
        
        addInput(_inFN);
        addInput(_inFA);
        addInput(_inFS);
 
        
        addOutput(_outSpec);
        
    }
    
    @Override
    public boolean evaluate() {
        
        String fn = _inFN.getInputValue();
        String fa = _inFA.getInputValue();
        String fs = _inFS.getInputValue();
       
        

        String outStr = "$fn = " + fn + ", $fa = " + fa + ", $fs = " + fs;
        
        _outSpec.setOutputValue(outStr);

        return true;        
        
    }    
    
    
    /**
     * Helper function to convert string to integer.  This values has already been checked as a number.
     * @param str  String to convert to an integer.
     * @return The integer value.
     */
    private int getInt(String str){
        
        
        int result = 0;
        
        
        try {
           float fValue = Float.parseFloat(str); 
            result = Math.round(fValue);
        } catch(NumberFormatException ex) {
            result = 0;
        }
        
        return result;
        
    }
    
    
}
