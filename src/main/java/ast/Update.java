package ast;

public class Update extends AbstractNode{

    private final String operator = ":=";
    private final String other = "mem";
    private Expr left, right;

    /**
     * A AST representation of mem [l] := r
     * @param l
     * @param r
     */
    public Update(Expr l, Expr r) {
        left = l;
        right = r;
    }


    @Override
    public NodeCategory getCategory() {
        return NodeCategory.UPDATE;
    }

    @Override
    public String toString() {
        return null;
    }

    // assert that left is mem[expr]
    @Override
    public boolean classInv() {
        return false;
    }
}
