package org.ngi;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.ngi.enums.TileType;

/**
 *  Information panel to used display information about Nodes, Inputs and Outputs on mouse overs.
 * @author Tom
 */
public class InfoPanel {
    
   
    
    private NodeGraph _nodeGraph;
    
    
    private float _posX;
    private float _posY;
    
    private ArrayList<String> _lines;
    
    public InfoPanel(NodeGraph nodeGraph) {
        
        _nodeGraph = nodeGraph;
        
    
        
        _posX = 0;
        _posY = 0;
        
        _lines = new ArrayList<>();
        
        
    }
    
    /**
     * Reset the info Panel.
     */
    public void reset() {
      
        _posX = 0;
        _posY = 0;
        
        _lines.clear();
        
    }
    
    /**
     * Set the panel information from the NodeTile.
     * @param tile The NodeTile to display the properties of.
     */
  public void setInfo(NodeTile tile) {
        
      reset();
        String tmp = ""; 
        String desc = "";
        String nVal = "";
        String valType = "";
        String cName = "";
        
        if(tile.getTileType() == TileType.INPUT) {
            tmp = "Input: ";
            NodeInput input = (NodeInput) tile;
            desc = input.getDescription();
            nVal =  input.getInputValue();
            cName = input.getClass().getSimpleName();
            if(input.hasConnection()) {
                valType = "Connected";
            } else {
                valType = "Static";
            }
            
        } else {
            tmp = "Output: ";
            NodeOutput output = (NodeOutput) tile;
            desc = output.getDescription();
            cName = output.getClass().getSimpleName();
            nVal =  output.getOutputValue();
            valType = "Output";            
        }
        
        //add the name
        tmp += tile.getName();
        //add the line
        _lines.add(tmp);
        tmp = "";

        
        //new line for type
        tmp += "Type: " + cName; // + "[" + tile.getDataType().toString() + "]";
        _lines.add(tmp);
        tmp = "";
        
        
        tmp = "Description: " + desc;
        _lines.add(tmp);
        tmp = "";
        
        //report any error
        if(tile.hasError()) {
            
            _lines.add( "Error:: " + tile.getErrorMessage());
            
        }
 
        
        _lines.add( valType + " Value:");
        addLines( nVal);
        
        
        //set the location of the info panel
        _posX = tile.getBounds().maxX();
        _posY = tile.getBounds().minY();
        
    }
        
    
    

    
    public void setInfo(Node node) {
        
        reset();
        
        _lines.add("Node: " + node.getName());
        
        _lines.add("Type: " + node.getClass().getSimpleName());
        
        _lines.add("Description: " + node.getDescription());
        
        if(node.hasError()) {
            _lines.add("Error:: " + node.getErrorMessage() );
        }
        
  
        //set the location of the info panel
        _posX = node.getBounds().maxX();
        _posY = node.getBounds().minY();        
        
        
        
    }
    
    /**
     * Add all the lines of text from a multi-line string ('\n' separated).
     * @param val String to parse
     */
    private void addLines(String val) {
        
        //bail on empty string
        if(val.equals("")) {
            _lines.add("NULL");
            return;
        }
   
        String[] sl = val.split("\\n");
        
        for(String s : sl) {
            s = s.replace("\t", "    ");
            _lines.add(s);
        }
        
    }
    
    /**
     * Pain the InfoPanel on screen.  Should be painted inside camera transform.
     * @param g2 Graphics object to paint to.  
     */
    public void paintInfoPanel(Graphics2D g2) {
        
        //bail if nothing to display
        if(_lines.size() < 1) return;
        
        
       g2.setColor(Color.white);

        Font nf = new Font(Font.DIALOG, Font.BOLD, 14);

        g2.setFont(nf);
        FontMetrics metrics = g2.getFontMetrics(nf);
        
        
        
        
        float lh = metrics.getHeight();
        float x = _posX + 4;
        float y = _posY;
 
        float th = 0;
        float lw = Float.MIN_VALUE;
        
        for(String l : _lines) {
            
            LineMetrics lm = metrics.getLineMetrics(l, g2);
            float sw = metrics.stringWidth(l);
            if (sw > lw) lw = sw;

            th += lh;
        }
        
        float pad = 4;
        Rectangle2D r2d = new Rectangle2D.Float(x , y  ,lw + pad + pad  ,th + pad + pad);
        
        Composite c1 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f);
        Composite pcomp = g2.getComposite();
        
        g2.setComposite(c1);
        g2.setColor(Color.BLACK);
        //g2.setColor(new Color(230,228,174));
        g2.fill(r2d);
        
        g2.setComposite(pcomp);
        g2.setColor(Color.WHITE);
        g2.setStroke( new BasicStroke(1));
        g2.draw(r2d);
        
        //g2.setColor(Color.BLACK);
        y += metrics.getHeight() - metrics.getDescent();
        for(String l : _lines) {

            g2.drawString(l, x + pad, y + pad);
            y += lh;
        }        
        
        
        
    }
    
    
}
