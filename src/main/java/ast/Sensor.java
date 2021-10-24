package ast;

public class Sensor extends Expr {
    private Operator operator;
    private Expr expression;

    /**
     * Create an AST representation of sensor.
     * Requires: op must be NEARBY, AHEAD, or RANDOM
     * @param op
     * @param e
     */
    public Sensor(Operator op, Expr e) {
        operator = op;
        expression = e;
    }

    /**
     * Create an AST representation of token smell
     * Requires: op must be SMELL
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
    public String toString() {
        return null;
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
