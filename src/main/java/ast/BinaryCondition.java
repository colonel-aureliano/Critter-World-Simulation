package ast;

/** A representation of a binary Boolean condition: 'and' or 'or' */
public class BinaryCondition extends Condition {

    private Operator operator;

    /**
     * Create an AST representation of l op r.
     *
     * @param l
     * @param op
     * @param r
     */
    public BinaryCondition(Condition l, Operator op, Condition r) {
        super(l,r);
        operator = op;
    }

    /**
     * Resets the operator of this node.
     * Intended to be called only by MutationImpl.
     * @return
     */
    protected void resetOperator(){
        if(operator.equals(Operator.OR)){
            operator=Operator.AND;
        }
        else{
            operator=Operator.OR;
        }
    }

    /** An enumeration of all possible binary condition operators. */
    public enum Operator {
        OR,
        AND;
    }

    @Override
    public Node clone() {
        return new BinaryCondition((Condition) children.get(0).clone(),operator,(Condition) children.get(1).clone());
    }

    @Override
    public String toString(){
//        return visit(new PrintVisitor());

        StringBuilder sb = new StringBuilder();
        if(children.get(0) instanceof BinaryCondition){
            sb.append("{"+children.get(0)+"}");
        }
        else{
            sb.append(children.get(0));
        }
        sb.append(" "+operator.toString().toLowerCase()+" ");
        if(children.get(1) instanceof BinaryCondition){
            sb.append("{"+children.get(1)+"}");
        }
        else{
            sb.append(children.get(1));
        }
        return sb.toString();
    }

    public String visit(Visitor v) {
        return v.visit(this, operator);
    }

    public boolean classInv() {
        return children.size()==2 && children.get(0) instanceof Condition
                && children.get(1) instanceof Condition;
    }
}
