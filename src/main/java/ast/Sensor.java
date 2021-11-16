package ast;

import java.util.Random;

public class Sensor extends Expr {
    protected Operator operator;

    /**
     * Create an AST representation of sensor.
     * Requires: op must be NEARBY, AHEAD, or RANDOM
     *
     * @param op
     * @param e
     */
    public Sensor(Operator op, Expr e) {
        super(e);
        operator = op;
    }

    /**
     * Create an AST representation of token smell
     * Requires: op must be SMELL
     *
     * @param op
     */
    public Sensor(Operator op) {
        operator = op;
    }

    public enum Operator {
        NEARBY,
        AHEAD,
        RANDOM,
        SMELL
    }

    @Override
    public Node clone() {
        if (operator == Operator.SMELL) {
            return new Sensor(operator);
        } else {
            return new Sensor(operator, (Expr) children.get(0).clone());
        }
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
        return (children.size() == 1 && children.get(0) instanceof Expr) ||
                (operator == Operator.SMELL && children == null);
    }

    @Override
    public int value() {
        switch(operator){
            case NEARBY:
                return interpreter.nearby(((Expr)children.get(0)).value());
            case AHEAD:
                return interpreter.ahead(((Expr)children.get(0)).value());
            case RANDOM:
                Random r = new Random();
                return r.nextInt(((Expr)children.get(0)).value());
            case SMELL:
                return interpreter.smell();
        }
        return 0;
    }
}
