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
//        return visit(new PrintVisitor());

        StringBuilder sb = new StringBuilder();
        if(left instanceof BinaryCondition){
            sb.append("{"+left+"}");
        }
        else{
            sb.append(left);
        }
        sb.append(" "+operator.toString().toLowerCase()+" ");
        if(right instanceof BinaryCondition){
            sb.append("{"+right+"}");
        }
        else{
            sb.append(right);
        }
        return sb.toString();
    }

    public String visit(Visitor v) {
        return v.visit(this);
    }

    public boolean classInv() {
        // TODO
        return false;
    }
}
