
package org.ngi.quadtree;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import org.ngi.Bounds;
import org.ngi.MouseFocus;
import org.ngi.Node;

/**
 *
 *   Quick and dirty fixed size and fixed depth QuadTree spatial index.
 *   Index records are stored here with the index tree instead of with the 
 *   Nodes. 
 *   
 * 
 * @author Tom
 */
public class QuadTree {
    
    private final int DEPTH = 6; //6 is good for the node size
    private float _width;
    private float _height;
    
    private Bounds _bounds;
    
    private QuadTreeNode _rootNode;
    
    private HashMap<String, QuadTreeNodeCollection> _nodeIndex;
    
    public QuadTree(float width, float height) {
        
        _width = width;
        _height = height;
        
        float hw = width  / 2;
        float hh = height / 2;
        
        //center the bounds on 0,0...
        _bounds = new Bounds(-hw,-hh,hw,hh);
        
        QuadTreeNode.COUNTER = 0;
        //init root
        _rootNode = new QuadTreeNode(_bounds, DEPTH);
        
        _nodeIndex = new HashMap<>();
        
    }
    
    /**
     * Clear the index for file open.  Clears the index but keeps the size.
     */
    public void clear() {
        
        //reset the node index
        _nodeIndex.clear();
        //reset all the nodes
        _rootNode = new QuadTreeNode(_bounds, DEPTH);
        
        
    }
    
    
    /**
     *
     * Check for Mouse focus - this is the entry point for selecting nodes or inputs/outputs.
     * Tests if the mouse is over a node and/or an input or output through the spatial index.
     *
     * @param x X value of point to check.
     * @param y Y value of point to check.
     * @return The mouse focus object with the acquired node if found.
     */
    public MouseFocus mouseFocusTest(float x, float y) {
        
        return _rootNode.mouseFocusTest(x, y);

    }  
    
    /**
     * Wrapper over Bounds.contains to test Nodes when being translated to keep them "in bounds".
     * @param b  Bounds to test for containment.
     * @return  True if the QuadTree contains the Bounds, false if it is outside the bounds.
     */
    public boolean contains(Bounds b) {
        return _bounds.contains(b);
    }
    
    
    /**
     * Force a Bounds in side the QuadTree Bounds.  Used to make sure no Nodes are created out-of-bounds.
     * This will not change Bounds that are within bounds already.
     * @param b  Bounds object to check and make in-bounds.
     * @return   Bounds object inside limits of QuadTree bounds.
     */
    public Bounds ensureContainment(Bounds b) {
        
 
        
        float w = b.width();
        float h = b.height();
        
        float x1 = b.minX();
        float x2 = b.maxX();
        float y1 = b.minY();
        float y2 = b.maxY();
        
        
        float cxmin = _bounds.minX();
        float cxmax = _bounds.maxX();
        float cymin = _bounds.minY();
        float cymax = _bounds.maxY();        
        
        //check x alignment - left or right of bounds
        if(x1 < cxmin) {
            x1 = cxmin;
            x2 = cxmin + w;
        } else if (x2 > cxmax) {
            x1 = cxmax - w;
            x2 = cxmax;
        }
        
        //check y alignment - above or below bounds
        if(y1 < cymin) {
            y1 = cymin;
            y2 = cymin + h;
        } else if (y2 > cymax) {
            y1 = cymax - h;
            y2 = cymax;
        }
        
        return new Bounds(x1,y1,x2,y2);
        
    }
    
    
    
    
    
    /**
     * Index a Node.  First find and clear previous records.
     * @param n Node to index.
     */
    public void indexNode(Node n) {
        
        String id = n.getId();
        QuadTreeNodeCollection qtnc = null;
        
        //look for node in index
        if(_nodeIndex.containsKey(id)) {
            
            //get the record and clear it
            qtnc = _nodeIndex.get(id);
            qtnc.clearNodes();
            
        } else {
            //create first instance and add to collection
            qtnc = new QuadTreeNodeCollection(n);
            _nodeIndex.put(id, qtnc);
            
        }
        
        //index the node
        _rootNode.indexNode(n, qtnc);
    }
    
    
    /**
     * Delete a Node from the index - this is called when a node is deleted from the NodeGraph.
     * @param n The Node to delete from the QuadTree.
     */
    public void deleteNode(Node n) {
        
        String id = n.getId();
        
        if(_nodeIndex.containsKey(id)) {
            QuadTreeNodeCollection qtnc = _nodeIndex.get(id);
            qtnc.clearNodes();
            //remove it completely from the list
            _nodeIndex.remove(id);
        }
        
 
        
        
    }
    
    /**
     * Draw the Bounds and sub nodes.
     * @param g2 Graphics object to paint to.
     */
    public void paintQuadTree(Graphics2D g2) {
        
        
        //paint the nodes
        _rootNode.paintQuadTreeNode(g2);
        
        
        //Bounds b = _rootNode.
        g2.setColor(new Color(60,60,60));
        Rectangle2D.Float r2df = new Rectangle2D.Float(_bounds.minX(), _bounds.minY(), _bounds.width(),  _bounds.width());
        //Stroke dashed = new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{20,20,5,20}, 0);
        Stroke dashed = new BasicStroke(8, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[]{20,20}, 0);
        g2.setStroke(dashed);
        g2.draw(r2df);        
        
    }
    
}
