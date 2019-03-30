package org.ngi;

import java.io.Serializable;


import org.ngi.enums.TileType;
import org.ngi.enums.DataType;
import java.util.ArrayList;





/**
 *  Base class for all NodeOutputs.  This class manages output connections and value storage and passing.
 * @author Tom
 */
public class NodeOutput extends NodeTile implements Serializable {
    
    private DataType _dataType;
    
    //private ArrayList<NodeInput> noConnections;
    
    private ArrayList<NodeConnection> _nodeConnections;
    
    private String _value;
    
    private Node _parentNode;
    
    public NodeOutput(Node parent, String name, DataType dataType, String description) {
        super(name, new Bounds(0,0,1,1),description );
        
        _parentNode = parent;
        
        //noConnections = new ArrayList<>();
        
        _nodeConnections = new ArrayList<>();

        _value = "";
        
        _dataType = dataType;
        
    }
    
    
    @Override
    public TileType getTileType(){
        return TileType.OUTPUT;
    }
    
    @Override
    public DataType getDataType(){
        return _dataType;
    }

    
    @Override
    public void translate(float x, float y) {
        super.translate(x, y);
        
        //update curves
        for(NodeConnection conn : _nodeConnections ) {
            conn.updateCurve();
        }
        
    }    
    
    @Override
    public void setHighlight() {
        super.setHighlight();
        
        for(NodeConnection conn : _nodeConnections ) {
            conn.setHighlight();
        }
        
    }
    
    
    @Override
    public void clearHighlight() {
        
        super.clearHighlight();
        _nodeConnections.forEach((conn) -> {
            conn.clearHighlight();
        });        
        
    }
    
    /**
     * Get the Parent of this Output node.
     * @return Node Parent of this node.
     */
    public Node getParent() {
        return _parentNode;
    }
    
    
    /**
     * Get the output value
     * @return The oupt value of the NodeOutput.
     */
    public String getOutputValue() {
        return _value;
    }
    
    /**
     * Set the output value.
     * @param output String Output value.
     */
    public void setOutputValue(String output) {
        _value = output;
    }   
    
 
    
    /**
     * Add a connection object
     * @param conn NodeCOnnection to add to the NodeOutput.
     */
    public void addConnection(NodeConnection conn) {
        _nodeConnections.add(conn);
        
    }
 
    

    
    
    /**
     * Remove a connection from list of connections
     * @param conn NodeConnection to remove
     */
    public void removeConnection(NodeConnection conn) {
        
        if(_nodeConnections.contains(conn)) {
            _nodeConnections.remove(conn);
        }
        
        
    }
        
    
//    public ArrayList<NodeInput> getConnections() {
//        return noConnections;
//    }
   
    public ArrayList<NodeConnection> getNodeConnections() {
        return _nodeConnections;
    }    
    
    
}
