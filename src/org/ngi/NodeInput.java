package org.ngi;


import java.io.Serializable;

import java.util.ArrayList;
import org.ngi.enums.TileType;
import org.ngi.enums.DataType;
import org.ngi.enums.InputDataRequirements;

/**
 *  Base Class for all Node Inputs.  This is the Node Input tile that handles connections, 
 * static values, and compatibility testing with NodeOutputs.
 * 
 * @author Tom
 */
public class NodeInput extends NodeTile implements Serializable {

    private NodeConnection _connection;

    private DataType _dataType;
    private boolean _hasConnection;

    private String niStaticValue;


   private InputDataRequirements _inputDataReq;
    
    private Node _parentNode;
    
    private boolean _isOptional;

    
 
    
    public NodeInput(Node parent, String name, DataType dataType, InputDataRequirements dataReq, String description) {
        super(name, new Bounds(0, 0, 1, 1), description);

        _parentNode = parent;
        _connection = null;

        _hasConnection = false;
        _dataType = dataType;

        _inputDataReq = dataReq; 
        
        niStaticValue = "";
       
        _isOptional = false;

    }

    @Override
    public TileType getTileType() {
        return TileType.INPUT;
    }

    @Override
    public DataType getDataType() {
        return _dataType;
    }

    public boolean isOptional() {
        return _isOptional;
    }
    
    public void setAsOptional() {
        _isOptional = true;
    }
    
    @Override
    public void translate(float x, float y) {
        super.translate(x, y);
        
        if(_hasConnection) {
            if(_connection != null) {
                _connection.updateCurve();
            }
        }
        
    }
    
    
    @Override
    public void setHighlight() {
        super.setHighlight();
        
        if(_hasConnection) {
            if(_connection != null) {
                _connection.setHighlight();
            }
        }
        
    }
    
    
    @Override
    public void clearHighlight() {
        
        super.clearHighlight();
        if(_hasConnection) {
            if(_connection != null) {
                _connection.clearHighlight();
            }
        }                
        
    }    
    
    
    public InputDataRequirements getDataRequirements() {
        return _inputDataReq;
    }
    

    
    public void setStaticValue(String value) {

        niStaticValue = value;

    }

   public String getStaticValue() {

       return niStaticValue;

    }
   
    public boolean hasConnection() {

        return _hasConnection;
    }

    /**
     * Get the Input value, either from the Connection or Static Value if no
     * connection is present
     *
     * @return String the value of the Input
     */
    public String getInputValue() {
        if (_hasConnection) {
            return _connection.getOutputValue();
           // return niOutput.getOutputValue(); // 
        } else {
            return niStaticValue;
        }
    }

    
   /**
     * Get the Parent of this Input node.
     * @return Node Parent of this node.
     */
    public Node getParent() {
        return _parentNode;
    }    
    
 
    
    
    public void setInputConnection(NodeConnection conn) {
        
        _connection = conn;
        _hasConnection = true;
        
    }

    public void removeInputConnection() {
        //niOutput = null;
        _hasConnection = false;
        _connection = null;

    }
    

    public NodeConnection getConnection() {
        return _connection;
    }
    
    /**
     * Test only the data type here.  This should be overridden in subclasses.  It returns false here.
     *
     * @param dtOut DataType DataType of output
     * @return boolean true if Output and Input data types are compatible, false otherwise.
     */
    public boolean isOutputCompatible(DataType dtOut) {
        return false;
    }

    /**
     * Main compatibility Test
     * @param output NodeOutput to test for connection compatibility.
     * @return true if NodeOutput can be connected to this NodeInput, false if incompatible.
     */
    public boolean isOutputCompatible(NodeOutput output) {
        
        
        //basic nodes don't allow input connections
        //not input is cmpatible since it is not allowed
        if(_inputDataReq == InputDataRequirements.NOT_NULL_STATIC) {
            return false;
            //check for same node
        } else if (_parentNode == output.getParent()) {
            //can't be in the same Node!
            return false;
        } else {
            //get the input data type
            DataType dtOut = output.getDataType();
            return isOutputCompatible(dtOut);
        }  
  
   
    }
    
    /**
     * Check for Circular reference.  This has to be called before a connection is attempted.
     * @param output  NodeOutput to test.
     * @return True if the proposed connection will create a circular reference, false otherwise.
     */
    public boolean isCircularReference(NodeOutput output) {
      
        boolean result = false;
        //get the output parent
        Node outP = output.getParent();
        //get parent inputs
        ArrayList<NodeInput> inputs = outP.getInputs();
        String thisId = this.getId();
        for(NodeInput ni : inputs) {
            String inId = ni.getId();
            if(inId.equals( thisId )) {
                result = true;
                break;
            }
            
            if(ni.hasConnection()) {
                
                NodeConnection conn = ni.getConnection();
                NodeOutput subOutput = conn.getNodeOutput(); //  ni.getNodeOutput();
                result = isCircularReference(subOutput);
                //found this id in chain?
                if(result == true) {
                    break;
                }
            }
                
            
            
            
        }//end for
        
        return result;
    }
    
}
