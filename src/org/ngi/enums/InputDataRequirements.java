
package org.ngi.enums;

/**
 *  Enum for Input states.  Inputs can be null, or require a static value only, or a non-null connection (no static allowed),
 *  or non-null static or connected value
 * @author Tom
 */
public enum InputDataRequirements {
    /**
     * Any data input, null allowed.
     */
    NULLABLE_ANY,
    /**
     * Any data input, static or connected (no null value).
     */
    NOT_NULL_ANY,
    /**
     * Static data input (local to he NodeInput, no null value).
     */
    NOT_NULL_STATIC,
    /**
     * Connected data Input (no null value).
     */
    NOT_NULL_CONN;
    
    
}
