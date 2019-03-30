package org.ngi;

import java.awt.BasicStroke;
import java.io.Serializable;

import org.ngi.enums.TileType;
import org.ngi.enums.DataType;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import org.ngi.enums.NodeError;

/**
 * Base Class for NodeINputs and NodeOutputs that handles all the graphics functions.
 *
 * @author Tom
 */
public abstract class NodeTile extends Element implements Serializable {

    private boolean _highlight;
    private boolean _hasError;

    private NodeError _error;

    private String _errorMessage;

    private String _displayName;

    public NodeTile(String name, Bounds bounds, String description) {

        super(name, bounds, description);

        _highlight = false;
        _hasError = false;
        _error = NodeError.NONE;
        _errorMessage = "";

        _displayName = "";

    }

    /**
     * Get the general type of Tile: Input or Output.
     *
     * @return TileType The type of the tile: Input or Output.
     */
    public abstract TileType getTileType();

    /**
     * Get the specific Data Type the Tile either inputs or outputs
     *
     * @return DataType The specific data type the Tile either inputs or outputs
     */
    public abstract DataType getDataType();

    /**
     * Test to see if mouse is over Tile.
     *
     * @param x Mouse X value to test.
     * @param y Mouse Y value to test
     * @return boolean True if point is contained within Tile, false otherwise.
     */
    public boolean mouseFocusTest(float x, float y) {
        return _bounds.contains(x, y);
    }

    /**
     * translate the Tile
     *
     * @param x X value to translate Tile
     * @param y Y value to translate Tile
     */
    public void translate(float x, float y) {

        _bounds.translate(x, y);

    }

    /**
     * Alternate highlight flag to avoid circular references when flag is set
     * from connection object
     */
    final public void setHighlightIntern() {
        _highlight = true;
    }

    /**
     * Alternate highlight flag to avoid circular references when flag is set
     * from connection object
     */
    final public void clearHighlightIntern() {
        _highlight = false;
    }

    /**
     * Set the highlight flag. NodeTile is displayed highlighted.
     */
    public void setHighlight() {
        _highlight = true;
    }

    /**
     * Clear the highlight flag. NodeTile is displayed normally.
     */
    public void clearHighlight() {
        _highlight = false;
    }

    public boolean hasHighlight() {
        return _highlight;
    }

    /**
     * Sets the Error flag, outlining the NodeTile in red.
     *
     * @param error Error message for the current error.
     */
    public void setError(String error) {
        _hasError = true;
        _errorMessage = error;
        _error = NodeError.ERROR;
    }

    public void setUpstreamError() {
        _hasError = true;
        _errorMessage = "Upstream Error";
        _error = NodeError.UPSTREAM;
    }

    /**
     * Clear the Error flag.
     */
    public void clearError() {
        _hasError = false;
        _errorMessage = "";
        _error = NodeError.NONE;
    }

    public boolean hasError() {
        return _hasError;
    }
    public boolean hasUpstreamError() {
        return _error == NodeError.UPSTREAM;
    }

    public String getErrorMessage() {
        return _errorMessage;
    }

    /**
     * Set the display name - this is a display override to show in the tile.
     * This doesn't change the name.
     *
     * @param name Name of the tile to display.
     */
    public void setDisplayName(String name) {

        name = truncateString(name);

        _displayName = name;
    }

 /**
  * Truncate a long string and terminate it with ellipsis.
  * @param s  The string to truncate.
  * @return The truncated string with an ellipsis suffix added.
  */
    private String truncateString(String s) {

        String newStr = s;
        int slen = newStr.length();
        int max = 6;

        if (slen > max) {
            newStr = newStr.substring(0, max - 1);
            newStr += "...";
        }

        return newStr;

    }

    public void paintTile(Graphics2D g2) {

        Rectangle2D boundsR2 = new Rectangle2D.Float(_bounds.minX(), _bounds.minY(), _bounds.width(), _bounds.height());

        g2.setStroke(new BasicStroke(1));

        Color fg = new Color(0,0,0);
        
        switch(_error) {
            
            case ERROR:
                if(_highlight) {
                    GradientPaint gp = new GradientPaint(_bounds.minX(), _bounds.minY(), Node.NODE_BACKGROUND , _bounds.maxX(), _bounds.maxY(), Node.NODE_ERROR_HL);
                    g2.setPaint(gp); 
                    
                    fg = Node.NODE_ERROR_HL;
                } else {
                    ///GradientPaint gp = new GradientPaint(_bounds.minX() , _bounds.minY(), Node.NODE_ERROR, _bounds.maxX() , _bounds.maxY(), Node.NODE_BACKGROUND);
                    //g2.setPaint(gp);
                    g2.setColor(Node.NODE_BACKGROUND);
                    fg = Node.NODE_ERROR;
                }                
                
                break;
                
                
            case UPSTREAM:
                if(_highlight) {
                    GradientPaint gp = new GradientPaint(_bounds.minX(), _bounds.minY(), Node.NODE_BACKGROUND , _bounds.maxX(), _bounds.maxY(), Node.NODE_WARN_HL);
                    g2.setPaint(gp); 
                    fg = Node.NODE_WARN_HL;
                } else {
                    //GradientPaint gp = new GradientPaint(_bounds.minX(), _bounds.minY(), Node.NODE_WARN, _bounds.maxX() - _bounds.width(), _bounds.maxY(), Node.NODE_BACKGROUND);
                    //g2.setPaint(gp);
                    g2.setColor(Node.NODE_BACKGROUND);
                    fg = Node.NODE_WARN;
                }  

                break;
                
                
            case NONE:
                
                if(_highlight) {
                    GradientPaint gp = new GradientPaint(_bounds.minX(), _bounds.minY(), Node.NODE_HIGHLIGHT, _bounds.maxX(), _bounds.maxY(), Node.NODE_BACKGROUND);
                    g2.setPaint(gp);                    
                } else {
                    g2.setColor(Node.NODE_BACKGROUND);
                }
                
                fg = Node.NODE_FOREGROUND;
                
                break;
    
        }//end switch
        


        //fill background
        g2.fill(boundsR2);

        g2.setColor(Node.NODE_FOREGROUND);

        Font nf = new Font(Font.DIALOG, Font.BOLD, 14);

        g2.setFont(nf);
        FontMetrics metrics = g2.getFontMetrics(nf);

        String dName = !_displayName.isEmpty() ? _displayName : _name;

        float strX = _bounds.minX() + metrics.charWidth('_');
        float strY = _bounds.minY() + metrics.getAscent() + metrics.getLeading();
        g2.drawString(dName, strX, strY);
        //g2.drawString(dName, _bounds.minX() + metrics.getHeight() * 0.33f, _bounds.minY() + metrics.getHeight());

        g2.setColor(fg);
        //draw outline of rectangle
        g2.draw(boundsR2);

    }//end paintTile    

    /**
     * Get the Connection Point for drawing wires.
     *
     * @return Point2D Connection point for the input or output
     */
    public Point2D.Float getConnectionPoint() {
        float hw = _bounds.width() / 2;
        float hh = _bounds.height() / 2;

        Point2D.Float cp = new Point2D.Float(_bounds.minX() + hw, _bounds.minY() + hh);
        if (getTileType() == TileType.INPUT) {
            cp.x -= hw;
        } else if (getTileType() == TileType.OUTPUT) {
            cp.x += hw;
        }

        return cp;

    }

}//edn class
