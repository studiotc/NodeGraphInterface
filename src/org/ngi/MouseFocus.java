package org.ngi;

import org.ngi.enums.FocusType;

/**
 *  Mouse over results object.  This is filled and returned during the mouse over or hover testing.
 * @author Tom
 */
public class MouseFocus {

    private FocusType _focusType;
    private Node _node;
    private NodeTile _tile;

    public MouseFocus() {

        _focusType = FocusType.NONE;
        _node = null;
        _tile = null;

    }

    public void clearResults() {

        _focusType = FocusType.NONE;
        _node = null;
        _tile = null;

    }

    public void setFocusNode(Node n) {
        _focusType = FocusType.NODE;
        _node = n;

    }

    public FocusType getFocusType() {
        return _focusType;
    }

    public void setFocusTile(NodeTile tile) {
        _focusType = FocusType.TILE;
        _tile = tile;
    }

    public NodeTile getFocusTile() {
        return _tile;
    }

    public boolean hasFocus() {
        return _focusType != FocusType.NONE;
    }

    public Node getFocusNode() {
        return _node;
    }

    public void setHighlight() {
        if (_node != null) {
            _node.setHighlight();
        }
        if (_tile != null) {
            _tile.setHighlight();
        }
    }

    public void clearHighlight() {
        if (_node != null) {
            _node.clearHighlight();
        }
        if (_tile != null) {
            _tile.clearHighlight();
        }
    }

}
