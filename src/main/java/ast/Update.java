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
    public String toString() {
        return left+" "+operator+" "+right;
    }

    // assert that left is mem[expr]
    @Override
    public boolean classInv() {
        return left!=null && right!=null;
    }
}
