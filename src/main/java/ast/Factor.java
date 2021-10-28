package ast;

public class Factor extends Expr {
    private int value;
    private Operator operator;

    /**
     * Create a factor for a number.
     * Requires: v is nonnegative.
     * @param v
     */
    public Factor (int v) {
        value = v;
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
    }

    public enum Operator {
        NEGATIVE;
    }

    @Override
    public Node clone() {
        if (operator == null) return new Factor(value);
        else return new Factor(operator, (Factor) children.get(0).clone());
    }

    @Override
    public String toString() {
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v){
        return v.visit(this, operator, value);
    }

    @Override
    public boolean classInv() {
        if (operator == null) return value >= 0 && children == null;
        else return children.get(0) instanceof Factor;
    }
}
