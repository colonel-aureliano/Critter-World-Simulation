package ast;

public class Factor extends Expr {
    private int value;
    private Factor factor;
    private Operator operator;
    private Expr e;
    private int which;

    /**
     * Create a factor for a number.
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
        operator = op;
        factor = f;
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
        this.e=e;
        which = 2;
    }

    @Override
    public String toString() {
        switch(which){
            case 0:
                return String.valueOf(value);
            case 1:
                return " -"+factor;
            case 2:
                return "("+e+")";
        }
        return "Factor class toString() error.";
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
