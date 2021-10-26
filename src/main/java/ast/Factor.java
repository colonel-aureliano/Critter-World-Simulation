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
    public String toString() {
        switch(which){
            case 0:
                return String.valueOf(value);
            case 1:
                return " -"+single;
            case 2:
                return "("+single+")";
        }
        return "Factor class toString() error.";
    }

    @Override
    public boolean classInv() {
        return (which==0 && value>=0) ||
                (which==1 && operator==Operator.NEGATIVE && single!=null) ||
                (which==2 && single!=null);
    }
}
