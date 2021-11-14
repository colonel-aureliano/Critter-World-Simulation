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

    @Override
    public Node clone() {
        return new BinaryExpr((Expr) children.get(0).clone(),operator,(Expr) children.get(1).clone());
    }

    @Override
    public String toString(){
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v) {
        return v.visit(this);
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
                if(((Expr) children.get(1)).value()==0) return 0;
                return Math.floorDiv(((Expr) children.get(0)).value(),((Expr) children.get(1)).value());
            case MOD:
                if(((Expr) children.get(1)).value()==0) return 0;
                return Math.floorMod(((Expr) children.get(0)).value(),((Expr) children.get(1)).value());
        }
        System.out.println("BinaryExpr value() error.");
        return -1;
    }

}
