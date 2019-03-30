
package org.ngi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import org.ngi.enums.HighlightMode;
import org.ngi.enums.NodeError;

/**
 *  Connection between a NodeInput and NodeOutput.  This class maintains the link between outputs and inputs
 * and handles highlighting.
 * @author Tom
 */
public class NodeConnection implements Serializable {

    private NodeOutput _output;
    private NodeInput _input;
    private CubicCurve2D _connCurve;

    private HighlightMode _highlight;
    private boolean _isHighlighted;
    private boolean _hasError;
    
    private NodeError _error;

    
    /**
     * Constructor
     * @param output NodeOutput - connection source.
     * @param input NodeINput - connection target.
     */
    public NodeConnection(NodeOutput output, NodeInput input) {

        _output = output;
        _input = input;

        _highlight = HighlightMode.NONE;
        _isHighlighted = false;
        _hasError = false;
        
        _error = NodeError.NONE;
        
        _connCurve = new CubicCurve2D.Float(0, 0, 1, 0, 0, 1, 1, 1);

   
        //set the initial curve
        updateCurve();
        
    }
    


    final public void connect() {
        
        _input.setInputConnection(this);
        _output.addConnection(this);
        
        //let error be overwriten by upstream...
        if(_output.hasError()) {
            setError();
        }
        //check for upstream
        if(_output.hasUpstreamError()) {
            setUpstreamError();
        }

        

        
    }
    
    /**
     * Disconnect the Input and Output Nodes.
     */
    final public void disconnect() {
        
        _input.removeInputConnection();
        _output.removeConnection(this);

        
    }
    
    
    /**
     * Update the connection curve.  Call after moving a node.
     */
    final public void updateCurve() {

        
        Point2D.Float p1 = _output.getConnectionPoint();
        Point2D.Float c1 = new Point2D.Float();
        Point2D.Float p2 = _input.getConnectionPoint();
        Point2D.Float c2 = new Point2D.Float();

        //control point offset factor for tangent points
        float dx = Math.abs(p2.x - p1.x) * 0.75f;
        //hold minimum
        dx = Math.max(20, dx);
        
        //set control points
        c1.setLocation(p1.x + dx, p1.y);
        c2.setLocation(p2.x - dx, p2.y);

        _connCurve.setCurve(p1, c1, c2, p2);

    }
    
    /**
     * Sets the highlight on for hover, etc.
     */
    public void setHighlight() {
        _highlight = HighlightMode.NORMAL;
        
        _input.setHighlightIntern();
        _output.setHighlightIntern();
 
        _isHighlighted = true;
        
    }
    
   
    
    /**
     * Clear the highlights
     */
    public void clearHighlight() {
        if(_hasError) {
            _highlight = HighlightMode.ERROR; 
        } else {
            _highlight = HighlightMode.NONE;
        }
        
        _isHighlighted = false;
        
        _input.clearHighlightIntern();
        _output.clearHighlightIntern();        
        
    }
    
    /**
     * Test if connection is highlighted (mouse over).
     * @return True if highlighted, false otherwise.
     */
    public boolean isHighlighted() {
        return _isHighlighted;
    }

    
    /**
     * Sets the color to error mode.
     */
    public void setError() {
        _hasError = true;
        _highlight = HighlightMode.ERROR;
        _error = NodeError.ERROR;
    }
    
   public void setUpstreamError() {
        _hasError = true;
        _highlight = HighlightMode.WARN;
        _error = NodeError.UPSTREAM;
    }    

    /**
     * Clear the error flag (used for display).
     */
    public void clearError() {
        _hasError = false;
        _highlight = HighlightMode.NORMAL;
        _error = NodeError.NONE;
        
    }
    
    public NodeOutput getNodeOutput() {
        return _output;
    }
    
    public NodeInput getNodeInput() {
        return _input;
    }    
    
    /**
     * Get the output value from the connections NodeOutput.
     * @return Output value of the connection.
     */
    public String getOutputValue() {
//        System.out.println("geting output value from connection");
//        System.out.println("out:" + ncOutput.getName() + "  in:" + ncInput.getName() );
        return _output.getOutputValue();
    }
    
    /**
     * Paint the connection curve.
     * @param g2 Graphics Object
     */
    public void paintConnection(Graphics2D g2) {
        Color connClr = Node.NODE_FOREGROUND;
        boolean dashed = false;
        
        
        switch(_error) {
            
            case NONE:
                connClr = _isHighlighted ? Node.NODE_HIGHLIGHT : Node.NODE_FOREGROUND;
                break;
                
                
            case ERROR:
                
                connClr = _isHighlighted ? Node.NODE_ERROR_HL : Node.NODE_ERROR;
                dashed = !_isHighlighted;
                break;
                
                
            case UPSTREAM:
                
                connClr = _isHighlighted ? Node.NODE_WARN_HL : Node.NODE_WARN;
                dashed = !_isHighlighted;               
                
                break;
 
        }//endswitch
        
        
        BasicStroke stroke;
        if(dashed) {
            stroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[]{7,7}, 0);
            
        } else {
            stroke = new BasicStroke(3);            
        }

        g2.setStroke(stroke);
            g2.setColor(connClr);
            g2.draw(_connCurve);
             
    
    }


}
