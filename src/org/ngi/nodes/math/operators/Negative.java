package org.ngi.nodes.math.operators;

import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.ExpressionOutput;


/**
 *  Ternary Negation Node.  Adds the ternary negative operator,
 *  the equivalent of multiplying the the number by -1.
 *  @author Tom
 */
public class Negative extends Node {

    private NodeInput _inA;
    private NodeOutput _outExp;


    public Negative() {
        super( "Negative", "Negative operator (-x).");

    }

    @Override
    protected void init() {

        _inA = new ExpressionInput(this, "A", "Expression to negate.");
        _outExp = new ExpressionOutput(this, "X", "Negative value expression.");


        addInput(_inA);
        addOutput(_outExp);

    }

    @Override
    public boolean evaluate() {

        String inAstr = _inA.getInputValue();


        //if not number or variable
        if (!InputUtilities.isValidNumber(inAstr) && !InputUtilities.isValidVariableName(inAstr)) {
            //assume expression and encapsulate
            inAstr = "(" + inAstr + ")";
        }

        String outStr = "-" + inAstr;
        _outExp.setOutputValue(outStr);

        return true;

    }

 

}
