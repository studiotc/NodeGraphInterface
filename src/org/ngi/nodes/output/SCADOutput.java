package org.ngi.nodes.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.nodeinputs.StaticFileNameInput;
import org.ngi.nodeinputs.OperationInput;
import javax.swing.JOptionPane;


/**
 * Node that exports an OpenSCAD file (.scad).  This nodes takes the input block and writes it to the text file. 
 * As long as OpenSCAD is open and monitoring this output file for changes, updates should be immediate in OpenSCAD
 * when the graph is updated (any node changes).
 * @author Tom
 */
public class SCADOutput extends Node {
    
    private NodeInput _fileName;
    private NodeInput _data;
    
    public SCADOutput() {
        super("SCADOutput", "Writes the data out to an OpenSCAD file.");
    }
    
    @Override
    protected void init() {
        
        _fileName =  new StaticFileNameInput(this, "f", "Static file path.");
   
        _data = new OperationInput(this, "B", "Block to write out to file.");
      
    
        addInput(_fileName);
        addInput(_data);
        
    }   
    

    @Override
    public boolean evaluate() {
        
        boolean result = false;
        //gather filename
        String fname = _fileName.getInputValue();
        
        //gather input
        String dataStr = _data.getInputValue();
        

        //prep for file write
        File file = new File(fname);
        FileWriter fr = null;
        
        try {
            fr = new FileWriter(file);
            fr.write(dataStr);
            fr.flush();

            result = true;
        } catch (IOException e) {
           String mssg = e.getMessage();
            System.out.println(mssg);
            setErrorState("Error writing file" + fname);
             JOptionPane.showMessageDialog(null, "Error writing OpenSCAD file:" + fname + ":\n " + mssg);
        } finally {
            //close the writer
            try {
                if(fr != null) fr.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
        return result;        
        
    }    
    
    
    
}
