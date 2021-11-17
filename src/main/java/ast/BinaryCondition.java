package ast;

/**
 * A representation of a binary Boolean condition: 'and' or 'or'
 */
public class BinaryCondition extends Condition {

    protected Operator operator;

    /**
     * Create an AST representation of l op r.
     *
     * @param l
     * @param op
     * @param r
     */
    public BinaryCondition(Condition l, Operator op, Condition r) {
        super(l, r);
        operator = op;
    }

    /**
     * Resets the operator of this node.
     * Intended to be called only by MutationImpl.
     *
     * @return
     */
    protected void resetOperator() {
        if (operator.equals(Operator.OR)) {
            operator = Operator.AND;
        } else {
            operator = Operator.OR;
        }
    }

    /**
     * An enumeration of all possible binary condition operators.
     */
    public enum Operator {
        OR,
        AND;
    }

    @Override
    public Node cloneHelper() {
        return new BinaryCondition((Condition) children.get(0).clone(), operator, (Condition) children.get(1).clone());
    }

    @Override
    public String toString() {
        return visit(new PrintVisitor());
    }

    public String visit(Visitor v) {
        return v.visit(this);
    }

    public boolean classInv() {
        return children.size() == 2 && children.get(0) instanceof Condition
                && children.get(1) instanceof Condition;
    }

    @Override
    public boolean value() {
        switch(operator){
            case OR:
                return ((Condition)children.get(0)).value()
                        || ((Condition)children.get(1)).value();
            case AND:
                return ((Condition)children.get(0)).value()
                        && ((Condition)children.get(1)).value();
        }
        System.out.println("Binary Condition value() error.");
        return false;
    }
}
