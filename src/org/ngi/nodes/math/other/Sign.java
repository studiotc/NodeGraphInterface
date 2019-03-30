
package org.ngi.nodes.math.other;

import org.ngi.nodes.math.BaseFunction;

/**
 * Sign math function.  Returns 1 or 0 for positive or negative number.
 * @author Tom
 */
public class Sign extends BaseFunction {
    public Sign() {
        super( "Sign", "sign", false, "Get the sign of a number: 1.0 for (+), 0.0 for (-).");
    }    
}
