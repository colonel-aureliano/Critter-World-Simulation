package ast;

public class Action extends AbstractNode {

    private Operator operator;
    private Expr expression;

    /**
     * Create an Action node
     * Requires: op must be one of the first 10 actions in rule
     * @param op
     */
    public Action(Operator op) {
        operator = op;
    }

    /**
     * Creat an Action node for serve[expr]
     * Requires: op must be SERVE
     * @param op
     * @param e
     */
    public Action(Operator op, Expr e) {
        operator = op;
        expression = e;
    }

    public enum Operator {
        WAIT,
        FORWARD,
        BACKWORD,
        LEFT,
        RIGHT,
        EAT,
        ATTACK,
        GROW,
        BUD,
        MATE,
        SERVE
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.ACTION;
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
