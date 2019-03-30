package org.ngi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import java.util.ArrayList;
import org.ngi.enums.DataType;
import org.ngi.enums.HighlightMode;

import java.io.Serializable;
import org.ngi.enums.InputDataRequirements;
import org.ngi.enums.NodeError;

/**
 *  Base class for all Nodes.  This handles all the management of 
 *  Inputs and Outputs and Evaluation.  Graphics functions are also managed in this class.
 * 
 * 
 * @author Tom
 */
public abstract class Node extends Element implements Serializable {

    static Color NODE_BACKGROUND = new Color(0, 0, 0);
    static Color NODE_FOREGROUND = new Color(255, 255, 255);
    static Color NODE_HIGHLIGHT = new Color(192, 0, 192);//new Color(255, 255, 204);//new Color(40, 40, 120); //
    static Color NODE_WARN = new Color(191,64,0); //Color(204, 102, 0);//Color(194, 96, 0); //new Color(255, 85, 0); 255/128/0
    static Color NODE_WARN_HL = new Color(255, 85, 0);; //Color(255, 127, 0); //Color(255, 85, 0);
    static Color NODE_ERROR = new Color(140, 0, 0);
    static Color NODE_ERROR_HL = new Color(218, 0, 0);

    private final int TILE_SIZE = 24;

    private ArrayList<NodeTile> _tiles;

    private ArrayList<NodeOutput> _outputs;

    private ArrayList<NodeInput> _inputs;

    private boolean _highlight;

    private HighlightMode _highlightMode;

    private boolean _hasError;
    private String _errorMessage;

    private boolean _isInvalidated;
    
  

    /**
     *
     * Constructs the base class.  Initializes the Name, Bounds, and Description of the Node (passed to Element).
     *
     *
     * 
     * @param name Name of the Node
     * @param description  Description of the node - this is intended as a short help string.
     */
    public Node(String name, String description) {
        super(name, new Bounds(0, 0, 10, 10), description);

        super.setBounds(new Bounds(0, 0, (TILE_SIZE * 5), TILE_SIZE));

        _highlight = false;
        _highlightMode = HighlightMode.NONE;

        _hasError = false;
        _errorMessage = "";

        _isInvalidated = false;

        //list of all tiles, used for selection and drawing
        _tiles = new ArrayList<>();

        //list used for drawing connections...
        _outputs = new ArrayList<>();
        _inputs = new ArrayList<>();
        

        //call init in subclasses
        init();

    }//end constructor

    /**
     * Override this method to init sub-class inputs/outputs
     */
    protected abstract void init();

    /**
     * Add a NodeInput
     *
     * @param input NodeInput to add.
     */
    public final void addInput(NodeInput input) {

        Bounds b = new Bounds(0, 0, 1, 1);
        //b.set(_bounds.minX(), _input_layout_y, TILE_SIZE * 3, TILE_SIZE );
        b.set(0, 0, TILE_SIZE * 3, TILE_SIZE);

        //set output bounds
        input.setBounds(b);

        //add to tiles
        _tiles.add(input);
        _inputs.add(input);

        //expand bounds
        //_bounds.expandToInclude(b);
        layoutInputsAndOutputs();

    }

    /**
     * Add a NodeOutput.
     *
     * @param output NodeOutput NodeOutput to add.
     */
    protected void addOutput(NodeOutput output) {

        Bounds b = new Bounds(0, 0, 1, 1);
        //b.set(_bounds.maxX() - TILE_SIZE * 2, _output_layout_y, TILE_SIZE * 2, TILE_SIZE );

        b.set(0, 0, TILE_SIZE * 2, TILE_SIZE);

        //set output bounds
        output.setBounds(b);

        //add to tiles
        _tiles.add(output);
        _outputs.add(output);

        //expand bounds
        //_bounds.expandToInclude(b);
        layoutInputsAndOutputs();

    }

    /**
     * Layout the Tiles.
     */
    private void layoutInputsAndOutputs() {

        //starting y for inputs and outputs
        float minY = _bounds.minY() + TILE_SIZE;

        //largest y value - set during layout
        float maxY = minY;

        //intial y layout value
        float curY = minY;

        // x layout for left side
        float curX = _bounds.minX();

        for (NodeInput input : _inputs) {

            Bounds b = input.getBounds();
            float x = b.minX();
            b.setLocation(curX, curY);

            if (b.maxY() >= maxY) {
                maxY = b.maxY();
            }

            //update connection curve if present
            if (input.hasConnection()) {
                input.getConnection().updateCurve();
            }

            curY += TILE_SIZE;

        }

        //reset for right hand side
        curY = minY;
        //x for right hand side
        curX = _bounds.maxX() - (TILE_SIZE * 2);

        for (NodeOutput output : _outputs) {

            Bounds b = output.getBounds();

            b.setLocation(curX, curY);

            if (b.maxY() > maxY) {
                maxY = b.maxY();
            }

            curY += TILE_SIZE;

        }

        float nx = _bounds.minX();
        float ny = _bounds.minY();
        float width = _bounds.width();
        float height = maxY - ny;

        _bounds.set(nx, ny, width, height);

    }

    /**
     * Remove an Input
     *
     * @param input NodeInput to remove.
     */
    public void removeInput(NodeInput input) {

        if (_inputs.contains(input)) {
            _inputs.remove(input);
        }

        if (_tiles.contains(input)) {
            _tiles.remove(input);
        }

        layoutInputsAndOutputs();

    }

    /**
     * Always returns false unless an override is created in sub-class. Use this
     * to allow adding optional inputs.
     *
     * @return True if optional inputs are allowed.
     */
    public boolean allowOptionalInputs() {
        return false;
    }

    /**
     * Empty Method that is not normally used. Override this in sub-class to
     * implement additional optional inputs.
     *
     */
    public void addOptionalInput() {
    }

    /**
     * Get the invalidation state.
     *
     * @return True if the Node is invalidated (upstream has changed).
     */
    public boolean isInvalidated() {
        return _isInvalidated;
    }


    /**
     * Invalidate the node and it's children
     * @param tlist List or terminal nodes to evaluate.
     * @param level Current depth downstream.
     */
    public void invalidate(TerminalNodeList tlist, int level) {

        //System.out.println("--Invalidating Node @" + this.getName() + " :: " + this.getId());
        
        boolean isTerminal = true;
        _isInvalidated = true;

        for (NodeOutput nodeOut : _outputs) {
            //evaluate connected inputs
            ArrayList<NodeConnection> connections = nodeOut.getNodeConnections();
            for (NodeConnection conn : connections) {

                //update connected node
                NodeInput nodeIn = conn.getNodeInput();
                //pop parent and evaluate
                Node parent = nodeIn.getParent();
                parent.invalidate(tlist, level + 1);

                isTerminal = false;
            }//end for inputs

        }//end for outputs     

        //this is a terminal node
        if (isTerminal) {
            tlist.addNode(this, level);
        }

    }

    /**
     * Recursive function looks at all Input Connections.   If they are flagged
     * as Invalidated, it walks upstream until no more invalidated nodes are found on that branch,
     * and then evaluates that upstream Node.  Nodes are evaluated on the walk back down, ensuring all upstream nodes are evaluated
     * without any double evaluations.
     */
    public void evaluateUpstream() {

        //System.out.println("--Evaluating Node @" + this.getName() + " :: " + this.getId());
        // System.out.println("---Evaluating upstream connections ");
         
        for (NodeInput nodeIn : _inputs) {

            boolean connected = nodeIn.hasConnection();
            //check for upstream error
            if (connected) {
                NodeConnection conn = nodeIn.getConnection();
                NodeOutput output = conn.getNodeOutput();
                Node parent = output.getParent();

                //see if that parent node is invalidated
                if (parent.isInvalidated() ) {
                    //evaluate upstream on the parent
                    parent.evaluateUpstream();
                }

            }

        }//end for

         //System.out.println("---Evaluating Inputs" + this.getName() );
        /*
            
           Now that all upstrem connections are resolved,
           evaluate this node.
            
         */
        boolean hasUpstreamError = false;
        boolean hasInputError = false;
        boolean hasEvaluationError = false;

        //check inputs...
        //evalaute all inputs 
        for (NodeInput nodeIn : _inputs) {

            //check the input
            NodeError error = inputIsValid(nodeIn);

            switch (error) {
                case ERROR:
                    hasInputError = true;
                    break;
                case UPSTREAM:
                    hasUpstreamError = true;
                    break;
                case NONE:
                    nodeIn.clearError();
                    break;
            }//end switch

        }//end forinputs      

        //if inputs are ok, evaluate node
        if (!hasInputError && !hasUpstreamError) {

            //System.out.println("---Evaluating Node Final " + this.getName() );
            
            hasEvaluationError = !this.evaluate();
            //evaluate node
            if (!hasEvaluationError) {
                clearErrorState();
            } else {
                setErrorState("Error evaluating " + getName());
            }

        } else if (hasUpstreamError) {
           // System.out.println("---upstream error");
            setUpstreamError();
        }
        
        
        //update output objects
        //evalaute all outputs (should only be one generally...)
        for (NodeOutput nodeOut : _outputs) {

            if (hasUpstreamError) {
                nodeOut.setOutputValue("");
                nodeOut.setUpstreamError();
            } else {
                if ( hasEvaluationError || hasInputError ) {
                    nodeOut.setOutputValue("");
                    nodeOut.setError("Evaluation Error");
                } else {
                    nodeOut.clearError();
                }
            }

            //evaluate connected inputs
            ArrayList<NodeConnection> connections = nodeOut.getNodeConnections();
            for (NodeConnection conn : connections) {

                //set color
                if (hasUpstreamError) {
                    conn.setUpstreamError();
                } else {
                    if (hasEvaluationError || hasInputError) {
                        conn.setError();
                    } else {
                        conn.clearError();
                    }
                }


            }//end for inputs

        }//end for outputs        
        
        /***  Final Step - Clear Invalidated Flag ***/
        _isInvalidated = false;

    }//end evaluate upstream

    
    
    
 
    
    /**
     * Evaluate the Node. Override this method in the subclass to perform
     * operation on the text.
     *
     * @return True if Node evaluates OK, false otherwise.
     */
    public abstract boolean evaluate();

    /**
     * Test Input during evaluate node to make sure input is valid Check data
     * requirements and value type validity
     *
     * @param nodeIn NodeInput to check
     * @return True if nodeInput passes all tests, false otherwise.
     */
    private NodeError inputIsValid(NodeInput nodeIn) {

        NodeError isValid = NodeError.NONE;
        boolean connected = nodeIn.hasConnection();
        String value = nodeIn.getInputValue();
        DataType type = nodeIn.getDataType();

        boolean nullValue = value == null || value.trim().isEmpty();
        InputDataRequirements dataReq = nodeIn.getDataRequirements();

        //check for upstream error
        if (connected) {
            NodeConnection conn = nodeIn.getConnection();
            NodeOutput output = conn.getNodeOutput();
            if (output.hasError()) {
                nodeIn.setUpstreamError();
                return NodeError.UPSTREAM;
            }
        }

        switch (dataReq) {
            /* must be connected, and connection cannot be null */
            case NOT_NULL_CONN:
                if (!connected) {
                    //fail!
                    setErrorState("Invalid Missing Connection");
                    isValid = NodeError.ERROR;
                    nodeIn.setError("Missing Connection");
                } else if (nullValue) {
                    setErrorState("Invalid Connection Value of NULL");
                    isValid = NodeError.ERROR;
                    nodeIn.setError("Connection has NULL value.");
                }
                break;
            /* must have non-null static value */
            case NOT_NULL_STATIC:

                if (nullValue) {
                    //fail!
                    setErrorState("Invalid Null Static Value");
                    isValid = NodeError.ERROR;
                    nodeIn.setError("Static value is NULL");
                }

                break;

            /* must have non-null static or connected value */
            case NOT_NULL_ANY:
                if (nullValue) {
                    //fail!
                    setErrorState("Invalid Null Value");
                    isValid = NodeError.ERROR;
                    nodeIn.setError("NULL value.");
                }
                break;

            /* NULLABLE_ANY falls through here */
            default:

                break;
        }//end switch        

        //if data present check against data type
        if (isValid == NodeError.NONE && !nullValue) {
            //validate input data
            if (!validateInputData(value, type)) {
                nodeIn.setError("Invalid " + type.toString());
                setErrorState("Invalid Input Value");
                isValid = NodeError.ERROR;
            }

        }

        //final check to clear error
        if (isValid == NodeError.NONE) {
            //all good clear error
            nodeIn.clearError();
        }

        return isValid;

    }

    /**
     * Validate an Input value against it's data type. TODO more testing here...
     *
     * @param strVal String value of the input
     * @param type The data type of the input
     * @return True if value is valid for type, false otherwise
     */
    private boolean validateInputData(String strVal, DataType type) {

        boolean isValid = true;

        switch (type) {

            case NUMBER:
                isValid = InputUtilities.isValidNumber(strVal);
                break;
            case VARIABLE:
                isValid = InputUtilities.isValidVariableName(strVal);
                break;
            case BOOLEAN:
                isValid = InputUtilities.isValidBoolean(strVal);
                break;
            case EXPRESSION:
                isValid = InputUtilities.isValidExpression(strVal);
                break;
            case RANGE:
                break;
            case VECTOR:
                break;
            case OPERATION:
                break;
            case FILE_NAME:
                break;
            case ANY:
                break;
            default:
                ///not really needed...
                break;

        }

        return isValid;

    }

    /**
     * Testing for error highlight
     */
    protected void clearErrorState() {

        _hasError = false;
        _errorMessage = "";
        _highlightMode = HighlightMode.NONE;

    }

    /**
     * Testing for error highlight
     *
     * @param msg Error message to display.
     */
    protected void setErrorState(String msg) {

        _hasError = true;
        _errorMessage = msg;

        _highlightMode = HighlightMode.ERROR;

    }

    protected void setUpstreamError() {

        _hasError = true;
        _errorMessage = "Upstrem Error";

        _highlightMode = HighlightMode.WARN;

    }

    public boolean hasError() {
        return _hasError;
    }

    public String getErrorMessage() {
        return _errorMessage;
    }

    public MouseFocus focusTest(float x, float y) {

        MouseFocus htr = new MouseFocus();

        if (_bounds.contains(x, y)) {

            htr.setFocusNode(this);

            int s = _tiles.size();
            for (int i = 0; i < s; i++) {
                NodeTile tile = _tiles.get(i);
                if (tile.mouseFocusTest(x, y)) {
                    htr.setFocusTile(tile);
                    break;
                }

            }
        }

        return htr;

    }

    public void setHighlight() {
        _highlight = true;
    }

    public void clearHighlight() {
        _highlight = false;

        for (NodeTile t : _tiles) {
            t.clearHighlight();
        }

    }

    /**
     * Translate the Node and all of it's children.
     *
     * @param x X translate value.
     * @param y Y translate value.
     */
    public void translate(float x, float y) {

        _bounds.translate(x, y);

        for (NodeTile t : _tiles) {
            t.translate(x, y);
        }

    }

    /**
     * Set the Location of the Node by calculating translate
     *
     * @param x Float x position
     * @param y Float y position
     */
    public void setLocation(float x, float y) {

        float dx = x - _bounds.minX();
        float dy = y - _bounds.minY();

        this.translate(dx, dy);

        // _bounds.setLocation(x, y);
    }

    /**
     * Get the list of all outputs.
     *
     * @return List of outputs.
     */
    public ArrayList<NodeOutput> getOuputs() {
        return _outputs;
    }

    /**
     * Get the list of all inputs.
     *
     * @return List of inputs.
     */
    public ArrayList<NodeInput> getInputs() {

        return _inputs;

    }

    

    
    /**
     *
     * Render the Node (called from owner paintComponent)
     *
     * @param g2 Graphics Object to paint to.
     */
    public void paintNode(Graphics2D g2) {

        if (_highlight) {
            //g2.setColor(hc);
            GradientPaint gp = new GradientPaint(_bounds.minX(), _bounds.minY(), Color.BLACK, _bounds.maxX(), _bounds.maxY(), Node.NODE_HIGHLIGHT);

            g2.setPaint(gp);
        } else {
            g2.setColor(Node.NODE_BACKGROUND);
        }

        Rectangle2D boundsR2 = new Rectangle2D.Float(_bounds.minX(), _bounds.minY(), _bounds.width(), _bounds.height());

        Line2D.Float line2d = new Line2D.Float(_bounds.minX(), _bounds.minY() + TILE_SIZE, _bounds.maxX(), _bounds.minY() + TILE_SIZE);

        g2.fill(boundsR2);

        g2.setColor(Node.NODE_FOREGROUND);

        Font nf = new Font(Font.DIALOG, Font.BOLD, 14);

        g2.setFont(nf);
        FontMetrics metrics = g2.getFontMetrics(nf);
        float strX = _bounds.minX() + metrics.charWidth('_');
        float strY = _bounds.minY() + metrics.getAscent() + metrics.getLeading();
        g2.drawString(_name, strX, strY);

        g2.setStroke(new BasicStroke(1));

        g2.draw(line2d);
        //g2.drawLine((int) _bounds.minX(), (int) _bounds.minY() + TILE_SIZE, (int) _bounds.maxX(), (int) _bounds.minY() + TILE_SIZE);

        int s = _tiles.size();
        for (int i = 0; i < s; i++) {
            NodeTile t = _tiles.get(i);
            t.paintTile(g2);
        }

        
        g2.setStroke(new BasicStroke(2));

        switch (_highlightMode) {
            case ERROR:
               // g2.setStroke(new BasicStroke(3));
                g2.setColor(Node.NODE_ERROR);
                break;

            case WARN:
               // g2.setStroke(new BasicStroke(3));
                g2.setColor(Node.NODE_WARN);
                break;

            case NONE:
               // g2.setStroke(new BasicStroke(2));
                g2.setColor(Node.NODE_FOREGROUND);
                break;

        }//end switch


        //draw outline rectangle
        g2.draw(boundsR2);
    }

  

    /**
     * Utility to indent a line or lines of text. Lines as delimited by '\n'.
     *
     * @param text Text to indent.
     * @return The indented text.
     */
    static String indentText(String text) {

        return "\t" + text.replace("\n", "\n\t");

    }

    
    
    
}//end class
