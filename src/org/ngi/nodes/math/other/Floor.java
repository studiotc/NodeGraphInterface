
package org.ngi.nodes.math.other;

import org.ngi.nodes.math.BaseFunction;

/**
 * Floor math function.  Round a number down to next nearest integer.
 * @author Tom
 */
public class Floor extends BaseFunction {
    public Floor() {
        super( "Floor", "floor", false, "Floor function, the largest integer not greater than x.");
    }    
}
