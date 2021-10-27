package ast;

public class Mem extends Expr {

    /**
     * Create a Memory Object with expression e.
     *
     * @param e
     */
    public Mem(Expr e) {
        super(e);
    }

    @Override
    public String toString() {
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v) { return v.visit(this); }

    @Override
    public Node clone() {
        return new Mem((Expr) children.get(0).clone());
    }

    @Override
    public boolean classInv() {
        return children.size() == 1 && children.get(0) instanceof Expr;
    }

}
