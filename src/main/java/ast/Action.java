package ast;

public class Action extends AbstractNode {

    protected Operator operator;

    /**
     * Create an Action node
     * Requires: op must be one of the first 10 actions in rule
     *
     * @param op
     */
    public Action(Operator op) {
        operator = op;
    }

    /**
     * Creat an Action node for serve[expr]
     * Requires: op must be SERVE
     *
     * @param op
     * @param e
     */
    public Action(Operator op, Expr e) {
        super(e);
        operator = op;
    }

    public enum Operator {
        WAIT,
        FORWARD,
        BACKWARD,
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
    public Node cloneHelper() {
        if (operator == Operator.SERVE) {
            return new Action(operator, (Expr) children.get(0).clone());
        } else {
            return new Action(operator);
        }
    }

    @Override
    public String toString() {
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v) {
        return v.visit(this);
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.ACTION;
    }

    @Override
    public boolean classInv() {
        return (operator != null && children == null) ||
                (operator == Operator.SERVE && children.size() == 1 && children.get(0) instanceof Expr);
    }

    public Operator getOperator() {
        return operator;
    }
}
