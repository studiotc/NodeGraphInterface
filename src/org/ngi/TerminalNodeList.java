
package org.ngi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * 
 *   List class to hold terminal nodes as the child chain is resolved in the evaluation process.
 *
 * @author Tom
 */
public class TerminalNodeList {
    
    private HashMap<String, NodeRecord> _nodes;
    private ArrayList<NodeRecord> _evalOrder;
    
    
    public TerminalNodeList() {
        
        _nodes = new HashMap<>();
        
        _evalOrder = new ArrayList<>();
        
    }
    
    
    /**
     * Evaluate the Terminal Nodes.
     */
    public void evalTerminals() {
        
    
       ArrayList<TerminalNodeList.NodeRecord> al = new ArrayList<>(_nodes.values());
     
       
       //sort the list by level
       //evaluate the shallowist first
       Collections.sort(al); 
       
       int len = al.size();
       for(int i =0; i < len; i++) {
           NodeRecord nr = al.get(i);
           //System.out.println("evaluating upstream: " + nr.TNode.getName() + " , level=" + nr.Level);
           nr.TNode.evaluateUpstream();
       }
       

    }//end eval terminals
    
    
    /**
     * Add a Node to the list.
     * @param node  Node to add.
     * @param level  Level below initializing node.
     */
    public void addNode(Node node, int level) {
        
        
        String key = node.getId();
        NodeRecord nr = new NodeRecord(node, level);
        
        //check to see if this node has been flagged by a different path
        if(_nodes.containsKey(key)) {
            NodeRecord ex_nr = _nodes.get(key);
            int ex_l = ex_nr.Level;
            
            //if ex is deeper, replace with higher level
            if(ex_l > level) {
                ex_nr.TNode = node;
                ex_nr.Level = level;
            }
            
        } else {
            //add if not present
             _nodes.put(key,nr);
        }
        
       
        
    }
    
    /**
     * Class to hold a Node reference and a depth level for sorting.
     */
     class NodeRecord implements Comparable{
        public Node TNode;
        public int Level;
        
        public NodeRecord(Node n, int l) {
            TNode = n;
            Level = l;
        }
        
        
        @Override
        public int compareTo(Object otherObj) {
            NodeRecord other = (NodeRecord)(otherObj);
            if(this.Level > other.Level) {
                return 1;
            } else if(this.Level < other.Level) {
                return -1;
            } else {
                return 0;
            }
            
            
        }
        
    }
    
    
    
}
