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
        StringBuilder sb = new StringBuilder();
        sb.append(left);

        switch(operator){
            case PLUS:
                sb.append(" + ");
                break;
            case MINUS:
                sb.append(" - ");
                break;
            case MULTIPLY:
                sb.append(" * ");
                break;
            case DIVIDE:
                sb.append(" / ");
                break;
            case MOD:
                sb.append(" mod ");
                break;
        }

        sb.append(right);

        return sb.toString();
    }

    public boolean classInv() {
        // TODO
        return false;
    }

}
