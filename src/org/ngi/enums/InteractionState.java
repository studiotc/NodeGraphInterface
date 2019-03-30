package org.ngi.enums;

/**
 *  Main Enum for user interaction state in the UI (NodeGraph).
 * @author Tom
 */
public enum InteractionState {
    /**
     * Mouse has no action (no click and drag, etc.).
     */
    NEUTRAL,
    /**
     * Mouse is dragging a Node (translating).
     */
    DRAGGING_NODE,
    /**
     * Mouse is dragging a connection between nodes.
     */
    DRAGGING_CONNECTOR;

}

