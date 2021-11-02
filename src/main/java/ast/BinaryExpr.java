package ast;

public class BinaryExpr extends Expr {

    /** Only intended to be used by PrintVisitor to check whether parentheses are needed */
    protected Operator operator;

    /**
     * Create an AST representation of l op r.
     *
     * @param l
     * @param op
     * @param r
     */
    public BinaryExpr(Expr l, Operator op, Expr r) {
        super(l,r);
        operator = op;
    }

    /** An enumeration of all possible binary expression operators. */
    public enum Operator {
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        MOD;
    }

    /**
     * Resets the operator of this node.
     * Intended to be called only by MutationImpl.
     * @param o
     * @return
     */
    protected boolean resetOperator(Operator o){
        if(operator.equals(o)){
            return false;
        }
        else{
            operator=o;
            return true;
        }
    }

    @Override
    public Node clone() {
        return new BinaryExpr((Expr) children.get(0).clone(),operator,(Expr) children.get(1).clone());
    }

    @Override
    public String toString(){
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v) {
        return v.visit(this, operator);
    }

    public boolean classInv() {
        return children.size()==2 && children.get(0) instanceof Expr
                && children.get(1) instanceof Expr;
    }

    @Override
    public int value() {
        switch(operator){
            case PLUS:
                return ((Expr) children.get(0)).value()+((Expr) children.get(1)).value();
            case MINUS:
                return ((Expr) children.get(0)).value()-((Expr) children.get(1)).value();
            case MULTIPLY:
                return ((Expr) children.get(0)).value()*((Expr) children.get(1)).value();
            case DIVIDE:
                return ((Expr) children.get(0)).value()/((Expr) children.get(1)).value();
            case MOD:
                return ((Expr) children.get(0)).value()%((Expr) children.get(1)).value();
        }
        System.out.println("BinaryExpr value() error.");
        return -1;
    }

}
