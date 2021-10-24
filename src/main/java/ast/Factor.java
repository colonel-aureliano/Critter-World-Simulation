package ast;

public class Factor extends Expr {
    private int value;
    private Expr expression;
    private Operator operator;

    public Factor (int v) {
        value = v;
    }

    public Factor (Operator op, int v) {
        operator = op;
        value = v;
    }

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
