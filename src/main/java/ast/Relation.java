package ast;


public class Relation extends Condition{

    private Operator operator;
    private Expr left, right;

    public Relation(Expr l, Operator rel, Expr r){
        operator = rel;
        left = l;
        right = r;
    }

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
        return null;
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
