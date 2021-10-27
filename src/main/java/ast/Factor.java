package ast;

public class Factor extends Expr {
    private int value;
    private Operator operator;
    private int which;

    /**
     * Create a factor for a number.
     * Requires: v is nonnegative.
     * @param v
     */
    public Factor (int v) {
        value = v;
        which = 0;
    }

    /**
     * Create a factor for a negative factor.
     * Requires: op must be NEGATIVE
     * @param op
     * @param f
     */
    public Factor (Operator op, Factor f) {
        super(f);
        operator = op;
        which = 1;
    }

    public enum Operator {
        NEGATIVE,
    }

    /**
     * Create a factor for an expression in parentheses.
     * @param e
     */
    public Factor (Expr e) {
        super(e);
        which = 2;
    }

    @Override
    public Node clone() {
        switch(which){
            case 0:
                return new Factor(value);
            case 1:
                return new Factor(operator, (Factor) children.get(0).clone());
            case 2:
                return new Factor((Factor) children.get(0).clone());
            default:
                throw new IllegalArgumentException("Factor clone() error.");
        }
    }

    @Override
    public String toString() {
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v){
        return v.visit(this, which, value);
    }

    @Override
    public boolean classInv() {
        return (which==0 && value>=0 && children==null) ||
                (which==1 && operator==Operator.NEGATIVE && children.get(0) instanceof Factor) ||
                (which==2 && children.get(0) instanceof Expr);
    }
}
