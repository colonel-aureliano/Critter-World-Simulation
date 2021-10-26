package ast;

public class BinaryExpr extends Expr {

    private Operator operator;

    /**
     * Create an AST representation of l op r.
     *
     * @param l
     * @param op
     * @param r
     */
    public BinaryExpr(Expr l, Operator op, Expr r) {
        super(l,r);
        operator = op;
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
    public Node clone() {
        return new BinaryExpr((Expr) children.get(0).clone(),operator,(Expr) children.get(1).clone());
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(children.get(0));

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

        sb.append(children.get(1));

        return sb.toString();
    }

    public boolean classInv() {
        return children.size()==2 && children.get(0) instanceof Expr
                && children.get(1) instanceof Expr;
    }

}
