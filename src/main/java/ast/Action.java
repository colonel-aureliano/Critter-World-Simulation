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

    @Override
    public String toString() {
        if(operator== Operator.SERVE){
            return operator.toString().toLowerCase()+"["+single+"]";
        }
        return operator.toString().toLowerCase();
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.ACTION;
    }

    @Override
    public boolean classInv() {
        return (operator!=null && single== null) ||
                (operator==Operator.SERVE && single!= null);
    }
}
