package ast;

public class Factor extends Expr {
    private int value;
    private Expr expression;
    private Factor factor;
    private Operator operator;

    /**
     * Create a factor for a number.
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
        operator = op;
        factor = f;
    }

    /**
     * Create a factor for mem[expr]
     * Requires: op must be MEM
     * @param op
     * @param e
     */
    public Factor (Operator op, Expr e) {
        operator = op;
        expression = e;
    }

    public enum Operator {
        NEGATIVE,
        MEM;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
