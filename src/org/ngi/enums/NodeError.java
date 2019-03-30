package org.ngi.enums;

/**
 *  Error Flag for Nodes, Inputs, and Outputs.
 * 
 * @author Tom
 */
public enum NodeError {
    /**
     * There is no error condition.
     */
    NONE,
    
    /**
     * There is an upstream error - a connected output node is reporting an error or upstream error.
     */
    UPSTREAM,
    
    /**
     * There is a local error (this node, input or output).
     */
    ERROR;
    
    
}
