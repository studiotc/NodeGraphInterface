package org.ngi.nodes.math;

import org.ngi.InputUtilities;
import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.ExpressionOutput;


/**
 *  Base class for Math Operators.  Accepts two expression inputs and concatenates them with the operator in the middle.
 *  The inputs are tested for atomic value (number or variable) and are wrapped in parenthesis if they are not atomic.
 *  This is done to preserve the order of operations.
 *  
 *  
 * 
 * @author Tom
 */
public class BaseOperator extends Node {

    private NodeInput _inA;
    private NodeInput _inB;
    private NodeOutput _outExp;

    private String _operator;

    /**
     * Constructor
     *
     * 
     * @param name  Name of the operator.
     * @param operator The operator string to use in evaluate.
     * @param description  Description of the operator.
     */
    public BaseOperator(String name, String operator, String description) {
        super( name, description);

        _operator = operator;

    }

    @Override
    protected void init() {

        _inA = new ExpressionInput(this, "A", "Operand A.");
        _inB = new ExpressionInput(this, "B", "Operand B");
        _outExp = new ExpressionOutput(this, "X", "Operation value.");


        addInput(_inA);
        addInput(_inB);
        addOutput(_outExp);

    }

    @Override
    public boolean evaluate() {

        String inAstr = _inA.getInputValue();
        String inBstr = _inB.getInputValue();

        //if not number or variable
        if (!InputUtilities.isValidNumber(inAstr) && !InputUtilities.isValidVariableName(inAstr)) {
            //assume expression and encapsulate
            inAstr = "(" + inAstr + ")";
        }

        if (!InputUtilities.isValidNumber(inBstr) && !InputUtilities.isValidVariableName(inBstr)) {
            //assume expression and encapsulate
            inBstr = "(" + inBstr + ")";
        }

        //yep, that's all....
        String outStr = inAstr + " " + _operator + " " + inBstr;
        _outExp.setOutputValue(outStr);

        return true;

    }

    /**
     * Provided for special cases - not used, see getB()
     * @return NodeInput for A operand.
     */
    protected NodeInput getA() {
        return _inA;
    }

    /**
     * Provided for special cases - ok only the division operator...  divide by zero check.
     * @return NodeInput for B operand.
     */
    protected NodeInput getB() {
        return _inB;
    }

    /**
     * Provided for special cases - not used....
     * 
     * @return The NodeOutput of the operator.
     */
    protected NodeOutput getOut() {
        return _outExp;
    }

}
