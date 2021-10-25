package ast;

/** A representation of a binary Boolean condition: 'and' or 'or' */
public class BinaryCondition extends Condition {

    private Operator operator;
    private Condition left, right;

    /**
     * Create an AST representation of l op r.
     *
     * @param l
     * @param op
     * @param r
     */
    public BinaryCondition(Condition l, Operator op, Condition r) {
        operator = op;
        left = l;
        right = r;
    }

    /** An enumeration of all possible binary condition operators. */
    public enum Operator {
        OR,
        AND;
    }

    @Override
    public String toString(){
        return left+" "+operator.toString().toLowerCase()+" "+right;
    }

    public boolean classInv() {
        // TODO
        return false;
    }
}
