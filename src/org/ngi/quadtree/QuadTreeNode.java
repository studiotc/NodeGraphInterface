package org.ngi.quadtree;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.ngi.Bounds;
import org.ngi.MouseFocus;
import org.ngi.Node;

/**
 *  This is a node for the spatial index.  It holds the four quadrant child nodes or a list of Nodes at the leaf level.
 * @author Tom
 */
public class QuadTreeNode {

    private enum Quadrant {
        NW, NE, SE, SW, NONE;
    }

    static int COUNTER = 0;

    private Bounds _bounds;

    private QuadTreeNode _childNW;
    private QuadTreeNode _childNE;
    private QuadTreeNode _childSE;
    private QuadTreeNode _childSW;

    private ArrayList<QuadTreeNode> _children;

    private ArrayList<Node> _nodes;

    private boolean _hasChildren;
    private boolean _isLeaf;

    private boolean _highlight;

    public QuadTreeNode(Bounds b, int levels) {

        _bounds = b;

        _childNW = null;
        _childNE = null;
        _childSE = null;
        _childSW = null;
        _hasChildren = false;

        _children = new ArrayList<>();

        _nodes = null;
        _isLeaf = false;

        _highlight = false;

        //partition the node into quadrants or create leaf.
        partitionNode(levels);

    }

    public void setHighlight() {
        _highlight = true;
    }

    public void clearHighlight() {
        _highlight = false;
    }

    public Bounds getBounds() {
        return _bounds;
    }

    /**
     * Partition the node into quadrants. This is an initialization procedure.
     *
     * @param levels
     */
    private void partitionNode(int levels) {

        if (levels > 0) {

            _hasChildren = true;
            _isLeaf = false;
            int nl = levels - 1;

            _childNW = new QuadTreeNode(_bounds.getQuadrantNW(), nl);
            _childNE = new QuadTreeNode(_bounds.getQuadrantNE(), nl);
            _childSE = new QuadTreeNode(_bounds.getQuadrantSE(), nl);
            _childSW = new QuadTreeNode(_bounds.getQuadrantSW(), nl);

            _children.add(_childNW);
            _children.add(_childNE);
            _children.add(_childSE);
            _children.add(_childSW);

            //testing counter
            COUNTER += 4;

        } else {

            _hasChildren = false;
            _isLeaf = true;
            _nodes = new ArrayList<>();
        }

    }

    /**
     * Index a Node.  This traverses the quadrants down to the leaf node and records the node.
     * The Node is indexed based on the quadrants it intersects or is contained by..
     * @param n  Node to index.
     * @param qtnc Index collection associated with this node.  This is passed down from the top level.
     */
    public void indexNode(Node n, QuadTreeNodeCollection qtnc) {

        if (_hasChildren) {

            Bounds nb = n.getBounds();

            for (QuadTreeNode qtn : _children) {

                Bounds qb = qtn.getBounds();
                Bounds ib = nb.intersect(qb);

                if (ib != null) {
                    //index deeper
                    qtn.indexNode(n, qtnc);

                }

            }

        } else if (_isLeaf) {
            //index the node at the leaf level
            _nodes.add(n);
            //record this index for the node
            qtnc.add(this);

            setHighlight();
            //System.out.println("Index node: " + n.getName());
        }

    }

    /**
     * Remove a Node from the QuadTree index.
     * @param n Node to remove from the index.
     */
    public void removeNode(Node n) {

        if (_isLeaf) {
            _nodes.remove(n);

            if(_nodes.isEmpty()) {
                clearHighlight();
            }
            
        } else if (_hasChildren) {
            
           for (QuadTreeNode qtn : _children) {
               qtn.removeNode(n);

            }            
            
        }

    }



    /**
     *
     * Check for Mouse focus.
     *
     * @param x X value of point to check.
     * @param y Y value of point to check.
     * @return The mouse focus object with the aquired node if found.
     */
    public MouseFocus mouseFocusTest(float x, float y) {

        QuadTreeNode qtn = null;
        MouseFocus mf = new MouseFocus();

        if (_hasChildren) {

            Quadrant q = getQuadrant(x, y);

            switch (q) {
                case NW:
                    qtn = _childNW;
                    break;
                case NE:
                    qtn = _childNE;
                    break;
                case SE:
                    qtn = _childSE;
                    break;
                case SW:
                    qtn = _childSW;
                    break;
            }//end switch

            //recursive dive...
            if (qtn != null) {

                mf = qtn.mouseFocusTest(x, y);

            }

        } else if (_isLeaf) {

            //leaf node - search actual nodes
            if (_nodes != null) {

                int ns = _nodes.size();
                //iterate in reverse order to select node
                // on top - draw order hack of sorts...
                for(int i = ns - 1; i >= 0; i--) {
                    Node n = _nodes.get(i);
                    mf = n.focusTest(x, y);
                    //break if found a focus
                    if (mf.hasFocus()) {
                        break;
                    }                    
                    
                }
                
//                for (Node n : _nodes) {
//                    mf = n.focusTest(x, y);
//                    //break if found a focus
//                    if (mf.hasFocus()) {
//                        break;
//                    }
//                }

            }

        }//end if/else has children

        return mf;

    }

    /**
     * Get the Quadrant of the x,y point.
     *
     * @param x X value of the point.
     * @param y Y value of the point.
     * @return The quadrant the point falls into or none.
     */
    private Quadrant getQuadrant(float x, float y) {

        Quadrant q = Quadrant.NONE;
        float x1 = _bounds.minX();
        float x2 = _bounds.maxX();

        float y1 = _bounds.minY();
        float y2 = _bounds.maxY();

        float mx = x1 + _bounds.width() / 2;
        float my = y1 + _bounds.height() / 2;

        //west
        if (x >= x1 && x < mx) {

            //north/south
            if (y >= y1 && y < my) {
                q = Quadrant.NW;
            } else if (y >= my && y <= y2) {
                q = Quadrant.SW;
            }

            //east    
        } else if (x >= mx && x <= x2) {
            //north/south
            if (y >= y1 && y < my) {
                q = Quadrant.NE;
            } else if (y >= my && y <= y2) {
                q = Quadrant.SE;
            }
        }

        return q;

    }

    public void paintQuadTreeNode(Graphics2D g2) {

        if (_hasChildren) {

            _childNW.paintQuadTreeNode(g2);
            _childNE.paintQuadTreeNode(g2);
            _childSE.paintQuadTreeNode(g2);
            _childSW.paintQuadTreeNode(g2);

        } else if (_isLeaf) {

            //testing block for visualization
//            if (_highlight) {
//                g2.setStroke(new BasicStroke(2));
//                g2.setColor(new Color(255, 255, 0));
//            } else {
//                g2.setStroke(new BasicStroke(1));
//                g2.setColor(new Color(220, 220, 220));
//            }
            
             g2.setStroke(new BasicStroke(0));
             g2.setColor(new Color(220, 220, 220));            


            Rectangle2D.Float r2df = new Rectangle2D.Float(_bounds.minX(), _bounds.minY(), _bounds.width(), _bounds.width());
           // Line2D.Float l2dfV = new Line2D.Float(_bounds.)
            g2.draw(r2df);

        }//end if/else has children

    }

}//end class
