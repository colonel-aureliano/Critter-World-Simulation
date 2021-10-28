package ast;


public class Relation extends Condition {

    private Operator operator;

    /**
     * An AST representation of a relation with l and r.
     *
     * @param l
     * @param rel
     * @param r
     */
    public Relation(Expr l, Operator rel, Expr r) {
        super(l, r);
        operator = rel;
    }

    /**
     * Resets the operator of this node.
     * Intended to be called only by MutationImpl.
     *
     * @param o
     * @return
     */
    protected boolean resetOperator(Operator o) {
        if (operator.equals(o)) {
            return false;
        } else {
            operator = o;
            return true;
        }
    }

    /**
     * An enumeration of all possible relation operators.
     */
    public enum Operator {
        LESS_THAN,
        LESS_THAN_OR_EQUAl,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
        EQUAL,
        NOT_EQUAL
    }

    @Override
    public Node clone() {
        return new Relation((Expr) children.get(0).clone(), operator, (Expr) children.get(1).clone());
    }

    @Override
    public String toString() {
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v) {
        return v.visit(this, operator);
    }


    @Override
    public boolean classInv() {
        return children.size() == 2 && children.get(0) instanceof Expr
                && children.get(1) instanceof Expr;
    }
}
