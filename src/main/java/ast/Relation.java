package ast;


public class Relation extends Condition{

    private Operator operator;

    /**
     * An AST representation of a relation with l and r.
     * @param l
     * @param rel
     * @param r
     */
    public Relation(Expr l, Operator rel, Expr r){
        super(l,r);
        operator = rel;
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
    public Node clone(){
        return new Relation((Expr)children.get(0).clone(),operator,(Expr)children.get(1).clone());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(children.get(0));

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

        sb.append(children.get(1));

        return sb.toString();
    }

    @Override
    public boolean classInv() {
        return children.get(0)!=null && children.get(1)!=null && operator!=null;
    }
}
