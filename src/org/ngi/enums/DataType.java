
package org.ngi.enums;

/**
 *  Enum for assignment of a data type to NodeInputs and NodeOutputs.  
 * NodeINputs use this value occasionally, but depend on the type from the NodeOuput.
 * @author Tom
 */
public enum DataType {
    /**
     * General Number type: integer or float.
     */
    NUMBER,
    /**
     * Variable name (also Function or Module name).
     */
    VARIABLE,
    /**
     * Boolean of "true" or "false" (1 or 0 respectively)
     */
    BOOLEAN,
    /**
     * General Expression (any OpenSCAD math expression).
     */
    EXPRESSION,
    /**
     * Any OpenSCAD Range (start, end; or: start, increment, end).
     */
    RANGE,
    /**
     * Lists of data .
     */
    VECTOR,
    /**
     * Any Range or Vector.
     */
    SET,
    /**
     * Any block of code, usually to be enclosed in braes: {...}
     */
    OPERATION,
    /**
     * OpenSCAD output file name.
     */
    FILE_NAME,
    /**
     * Fragment Specification.
     */
    FRAGMENT_SPEC,
    /**
     * Any input - used for some general purpose Nodes: MergeBlock, etc.
     */
    ANY;
    
}


