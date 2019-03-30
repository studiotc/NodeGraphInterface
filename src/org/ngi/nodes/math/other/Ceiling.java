
package org.ngi.nodes.math.other;

import org.ngi.nodes.math.BaseFunction;

/**
 *  Ceil math function.   Round a number up to the next highest integer.
 * @author Tom
 */
public class Ceiling extends BaseFunction {
    public Ceiling() {
        super( "Ceiling", "ceil", false, "Ceiling function, the next highest integer rounding up.");
    }    
}
