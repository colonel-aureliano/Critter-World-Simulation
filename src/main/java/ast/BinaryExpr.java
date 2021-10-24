package ast;

public class BinaryExpr extends Expr {

    private Operator operator;
    private Expr left, right;

    /**
     * Create an AST representation of l op r.
     *
     * @param l
     * @param op
     * @param r
     */
    public BinaryExpr(Expr l, Operator op, Expr r) {
        operator = op;
        left = l;
        right = r;
    }

    /** An enumeration of all possible binary expression operators. */
    public enum Operator {
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        MOD;
    }

    @Override
    public String toString(){
        // TODO
        return null;
    }

    public boolean classInv() {
        // TODO
        return false;
    }

}
