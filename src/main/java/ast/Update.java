package ast;

public class Update extends AbstractNode{

    private final String operator = ":=";

    /**
     * A AST representation of l := r
     * @param l Mem class, representing mem[expr]
     * @param r
     */
    public Update(Mem l, Expr r) {
        super(l,r);
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.UPDATE;
    }

    @Override
    public Node clone() {
        return new Update((Mem) children.get(0).clone(),(Expr) children.get(1).clone());
    }

    @Override
    public String toString() {
        return children.get(0)+" "+operator+" "+children.get(1);
    }

    // assert that children.get(0) is mem[expr]
    @Override
    public boolean classInv() {
        return children.size()==2 && children.get(0) instanceof Mem
                && children.get(1) instanceof Expr;
    }
}
