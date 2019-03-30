
package org.ngi.enums;

/**
 *  Mouse focus test enum - used for mouse over to identify the target if there is one.
 * @author Tom
 */
public enum FocusType {
    /**
     * No Node under the mouse.
     */
    NONE,
    /**
     * Node is under the mouse.
     */
    NODE,
    /**
     * Node and specific tile (Input or Output) is under the mouse.
     */
    TILE;
}
