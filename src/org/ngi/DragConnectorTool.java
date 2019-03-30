package org.ngi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import org.ngi.enums.DataType;
import org.ngi.enums.FocusType;
import org.ngi.enums.InputDataRequirements;
import org.ngi.enums.TileType;

/**
 *  Drag connector tool for handling click and drag connection operations between nodes.
 * @author Tom
 */
public class DragConnectorTool {

    private enum ConnectionTarget {
        NONE, INPUT, OUTPUT;
    }


    private NodeInput _inputNode;
    private NodeOutput _outputNode;

    private NodeGraph _nodeGraph;

    private ConnectionTarget _connTarget;
    //private ConnectionTarget _connSource;

    private boolean _hasTarget;
    
    private boolean _hasInvalidTarget;

    private CubicCurve2D _dragConnCurve;

    private float _lastX;
    private float _lastY;    
    
    /**
     * Construct the drag connection tool.
     * @param nodeGraph Reference to the NodeGraph.
     */
    public DragConnectorTool(NodeGraph nodeGraph) {

        _nodeGraph = nodeGraph;

        reset();

    }

    /**
     * Reset the tool to it's initial state.
     */
    public final void reset() {

        _lastX = 0;
        _lastY = 0;
        
        _hasTarget = false;
        _hasInvalidTarget = false;

        _inputNode = null;
        _outputNode = null;

        _connTarget = ConnectionTarget.NONE;

        //default curve - no null
        _dragConnCurve = new CubicCurve2D.Float(0, 0, 1, 0, 0, 1, 1, 1);

    }

    
    
    /**
     * Set the source for the drag connector tool. This is the NodeTile that the
     * mouse is pressed on.
     *
     * @param mf The MouseFocus object from the mouse press event.
     * @return True if source is valid and accepted, false otherwise.
     */
    public boolean setSource(MouseFocus mf) {

        boolean sourceOk = false;
        
        if (mf.getFocusType() == FocusType.TILE) {

            NodeTile nt = mf.getFocusTile();
            Node parent = mf.getFocusNode();

            //source is input or output
            if (nt.getTileType() == TileType.INPUT) {

                //set input node and flags
                NodeInput tmpInput = (NodeInput) nt;
                InputDataRequirements idr = tmpInput.getDataRequirements();
                //check for static only input - no connection allowed
                if (idr != InputDataRequirements.NOT_NULL_STATIC) {
                    _inputNode = tmpInput;
                    _connTarget = ConnectionTarget.OUTPUT;
                    sourceOk = true;
                } 

            } else {
                //set output node and flags
                _outputNode = (NodeOutput) nt;
                _connTarget = ConnectionTarget.INPUT;
                 sourceOk = true;

            }

        }
        
        return sourceOk;

    }

    /**
     * Update the Drag Connector Tool. Called during mouse drag event. Checks to
     * see if mouse focus is viable target This will validate without doing a
     * circular reference check.
     *
     * @param mf  MouseFOucs object.
     * @param x  Current x value of the mouse.
     * @param y  Current y value of the mouse.
     */
    public void update(MouseFocus mf, float x, float y) {

        _hasTarget = false;
        
        //make sure there is a valid source
        if (_connTarget != ConnectionTarget.NONE) {

            if (mf.getFocusType() == FocusType.TILE) {

                _hasInvalidTarget = true;
                
                NodeTile nt = mf.getFocusTile();

                switch (_connTarget) {

                    case INPUT:

                        if (nt.getTileType() == TileType.INPUT) {

                            NodeInput input = (NodeInput) nt;
                            //DataType outDT = _outputNode.getDataType();

                            if (input.isOutputCompatible(_outputNode)) {
                                //don't link to same node....
                                //if (input.getParent() != _outputNode.getParent()) {

                                    //compatible match
                                    _inputNode = input;
                                    _hasTarget = true;
                                    _hasInvalidTarget = false;
                                    
                                    Point2D.Float cp = _inputNode.getConnectionPoint();
                                    x = cp.x;
                                    y = cp.y;

                                //}

                            }

                        }

                        break;

                    case OUTPUT:

                        if (nt.getTileType() == TileType.OUTPUT) {
                            NodeOutput output = (NodeOutput) nt;
                            //DataType outDT = output.getDataType();

                            if (_inputNode.isOutputCompatible(output)) {
                                //don't link to same node....
                                //if (_inputNode.getParent() != output.getParent()) {

                                    //compatible match
                                    _outputNode = output;
                                    _hasTarget = true;
                                    _hasInvalidTarget = false;
                                    
                                    Point2D.Float cp = _outputNode.getConnectionPoint();
                                    x = cp.x;
                                    y = cp.y;                                    

                                //}

                            }

                        }//end if output
                        break;

                }//end switch

            } else {

                _hasInvalidTarget = false;

            }//end if focus is tile    

        }//end if valid source

        _lastX = x;
        _lastY = y;
        //update the curve
        updateCurve(x,y);
        
    }//end update

    /**
     * Make the connection if source and target are available. This will be
     * called on mouseReleased.
     */
    public void makeConnection() {

        //no connection source or target selected
        if (_connTarget == ConnectionTarget.NONE || !_hasTarget) {
            return;
        }

        //check?...
        if (_outputNode != null && _inputNode != null) {
            
            _nodeGraph.createConnection(_outputNode, _inputNode);
            
        }
       

        this.reset();
        
        
    }//end make connection

    /**
     * Update the display curve
     * @param x  Current x value of curve target.
     * @param y  Current y value of curve target.
     */
    public void updateCurve(float x, float y) {

        final float VLEN = 100;
        Point2D.Float cp = new Point2D.Float();
        Point2D.Float p1 = new Point2D.Float();
        Point2D.Float c1 = new Point2D.Float();
        Point2D.Float c2 = new Point2D.Float();
        Point2D.Float p2 = new Point2D.Float();

        switch (_connTarget) {
            
            //if no source, no curve
            case NONE:
                return;

            case INPUT:

                if(_outputNode == null) return;
                //output looking for input
                cp = _outputNode.getConnectionPoint();

                p1.setLocation(cp.x, cp.y);
                p2.setLocation(x, y);

                break;

            case OUTPUT:

                if(_inputNode == null) return;
                //input look for output
                cp = _inputNode.getConnectionPoint();

                p1.setLocation(x, y);
                p2.setLocation(cp.x, cp.y);
               
                break;

        }//end switch
        
        float dx = Math.abs(p2.x - p1.x) * 0.75f;

        c1.setLocation(p1.x + dx, p1.y);
        c2.setLocation(p2.x - dx, p2.y);
        //update curve
        _dragConnCurve.setCurve(p1, c1, c2, p2);
        
        
    }//end updateCurve

    /**
     * Paint the Connector Line.
     * @param g2 Graphics object to paint to.
     */
    public void paintConnector(Graphics2D g2) {

        //bail if noting is set...
        if (_connTarget == ConnectionTarget.NONE) return;
        
            Stroke solid = new BasicStroke(3);
            Color lineColor = Color.WHITE;
            g2.setStroke(solid);
            
        if(_hasTarget) {
            
             //set color to white
            g2.setColor(Color.WHITE);
            
            //draw the curve
            g2.draw(_dragConnCurve);            
            
        } else {
            

            if(_hasInvalidTarget) {
                
                g2.setColor(Node.NODE_ERROR);
                //g2.setStroke(new BasicStroke(5));

                int len = 7;
                Line2D.Float la = new Line2D.Float(_lastX - len,_lastY - len, _lastX + len, _lastY + len);
                Line2D.Float lb = new Line2D.Float(_lastX + len,_lastY - len, _lastX - len,_lastY + len);

                g2.draw(la);
                g2.draw(lb);                
                
            }  else {
                
                g2.setColor(lineColor);
                int ow = 4;
                g2.fillOval((int) _lastX - ow, (int) _lastY - ow, ow * 2, ow * 2);
            }
            
            Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[]{9,5}, 0);
            g2.setStroke(dashed);  
            
            //draw teh curve
            g2.draw(_dragConnCurve);             

            
            
        }//end if has target
        

    }//end paintConn

}//end class
