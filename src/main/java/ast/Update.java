package ast;

public class Update extends AbstractNode {

    protected final String operator = ":=";

    /**
     * A AST representation of mem[l] := r
     *
     * @param l
     * @param r
     */
    public Update(Expr l, Expr r) {
        super(l, r);
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.UPDATE;
    }

    @Override
    public Node clone() {
        return new Update((Expr) children.get(0).clone(), (Expr) children.get(1).clone());
    }

    @Override
    public String toString() {
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v) {
        return v.visit(this);
    }

    // assert that children.get(0) is mem[expr]
    @Override
    public boolean classInv() {
        return children.size() == 2 && children.get(0) instanceof Expr
                && children.get(1) instanceof Expr;
    }
}
