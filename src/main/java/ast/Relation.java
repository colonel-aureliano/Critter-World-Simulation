package ast;


public class Relation extends Condition{

    private Operator operator;
    private Expr left, right;

    /**
     * An AST representation of a relation with l and r.
     * @param l
     * @param rel
     * @param r
     */
    public Relation(Expr l, Operator rel, Expr r){
        operator = rel;
        left = l;
        right = r;
    }

    /** An enumeration of all possible relation operators. */
    public enum Operator {
        LESS_THAN,
        LESS_THAN_OR_EQUAl,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
        EQUAL,
        NOT_EQUAL
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(left);

        switch(operator){
            case EQUAL:
                sb.append(" = ");
                break;
            case LESS_THAN:
                sb.append(" < ");
                break;
            case NOT_EQUAL:
                sb.append(" != ");
                break;
            case GREATER_THAN:
                sb.append(" > ");
                break;
            case GREATER_THAN_OR_EQUAL:
                sb.append(" >= ");
                break;
            case LESS_THAN_OR_EQUAl:
                sb.append(" <= ");
                break;
        }

        sb.append(right);

        return sb.toString();
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
