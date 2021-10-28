package ast;

public class Action extends AbstractNode {

    private Operator operator;

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

    /**
     * Resets the operator of this node.
     * Intended to be called only by MutationImpl.
     * @param o
     * @return
     */
    protected boolean resetOperator(Operator o){
        if(operator.equals(o)){
            return false;
        }
        else{
            operator=o;
            return true;
        }
    }

    @Override
    public Node clone() {
        if(operator== Operator.SERVE){
            return new Action(operator, (Expr) children.get(0).clone());
        }
        else{
            return new Action(operator);
        }
    }

    @Override
    public String toString() {
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v){
        return v.visit(this, operator);
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.ACTION;
    }

    @Override
    public boolean classInv() {
        return (operator != null && children == null) ||
                (operator==Operator.SERVE && children.size()==1 && children.get(0) instanceof Expr);
    }
}
