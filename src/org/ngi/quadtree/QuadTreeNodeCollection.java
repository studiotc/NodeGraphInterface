
package org.ngi.quadtree;

import java.util.ArrayList;
import org.ngi.Node;

/**
 *  Collection Object to to record Spatial Index records.
 * Each entry is record in the spatial index.
 * @author Tom
 */
public class QuadTreeNodeCollection {
    
    
    private ArrayList<QuadTreeNode> _qtNodes;
    
    private Node _indexedNode;
    
    public QuadTreeNodeCollection(Node n) {
        
        _qtNodes = new ArrayList<>();
        
        _indexedNode = n;
        
    }
    
    /**
     * Add a node to the record.  
     * @param node The QuadTreeNode where this Node is indexed.
     */
    public void add(QuadTreeNode node) {
        _qtNodes.add(node);
    }
    
    
    /**
     * Clear the list - do this before indexing after a move.
     */
    public void clearNodes() {
        
        //remove the node from each index
        for(QuadTreeNode qtn : _qtNodes) {
           qtn.removeNode(_indexedNode);
        }
        
        //clear the list
        _qtNodes.clear();
    }
    
    
}
