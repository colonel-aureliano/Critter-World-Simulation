package ast;


public class Relation extends Condition {

    protected Operator operator;

    /**
     * An AST representation of a relation with l and r.
     *
     * @param l
     * @param rel
     * @param r
     */
    public Relation(Expr l, Operator rel, Expr r) {
        super(l, r);
        operator = rel;
    }

    /**
     * An enumeration of all possible relation operators.
     */
    public enum Operator {
        LESS_THAN,
        LESS_THAN_OR_EQUAl,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
        EQUAL,
        NOT_EQUAL
    }

    @Override
    public Node cloneHelper() {
        return new Relation((Expr) children.get(0).clone(), operator, (Expr) children.get(1).clone());
    }

    @Override
    public String toString() {
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v) {
        return v.visit(this);
    }


    @Override
    public boolean classInv() {
        return children.size() == 2 && children.get(0) instanceof Expr
                && children.get(1) instanceof Expr;
    }

    @Override
    public boolean value(){
        switch(operator){
            case LESS_THAN:
                return ((Expr)children.get(0)).value()<((Expr)children.get(1)).value();
            case LESS_THAN_OR_EQUAl:
                return ((Expr)children.get(0)).value()<=((Expr)children.get(1)).value();
            case GREATER_THAN:
                return ((Expr)children.get(0)).value()>((Expr)children.get(1)).value();
            case GREATER_THAN_OR_EQUAL:
                return ((Expr)children.get(0)).value()>=((Expr)children.get(1)).value();
            case EQUAL:
                return ((Expr)children.get(0)).value()==((Expr)children.get(1)).value();
            case NOT_EQUAL:
                return ((Expr)children.get(0)).value()!=((Expr)children.get(1)).value();
        }
        System.out.println("Relation value() error.");
        return false;
    }
}
