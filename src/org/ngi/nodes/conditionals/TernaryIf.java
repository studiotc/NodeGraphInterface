package org.ngi.nodes.conditionals;

import org.ngi.Node;
import org.ngi.NodeInput;
import org.ngi.NodeOutput;
import org.ngi.nodeinputs.ExpressionInput;
import org.ngi.nodeoutputs.ExpressionOutput;

/**
 *  Ternary If operator for use in an expression (test ? then : else).  Uses expression inputs for if, then, and else. 
 * @author Tom
 */
public class TernaryIf extends Node {

    private NodeOutput _outExp;

    protected NodeInput _inExpTest;
    protected NodeInput _inThenExp;
    private NodeInput _inElseExp;

    public TernaryIf() {
        super("TernaryIf", "Ternary If operator.");

    }

    @Override
    protected void init() {

        _inExpTest = new ExpressionInput(this, "C", "Conditional test.");

        _inThenExp = new ExpressionInput(this, "T", "Then case of if statement.");

        _inElseExp = new ExpressionInput(this, "E", "Else case of the if statement");

        _outExp = new ExpressionOutput(this, "X", "Ternary If expression.");

        _inExpTest.setStaticValue("1=1");

        addInput(_inExpTest);
        addInput(_inThenExp);
        addInput(_inElseExp);

        addOutput(_outExp);

    }

    @Override
    public boolean evaluate() {

        String testExp = _inExpTest.getInputValue();
        String thenStr = _inThenExp.getInputValue();
        String elseStr = _inElseExp.getInputValue();
      
        String outStr = testExp + " ? " + thenStr + " : " + elseStr;

        _outExp.setOutputValue(outStr);

        return true;
    }


}
