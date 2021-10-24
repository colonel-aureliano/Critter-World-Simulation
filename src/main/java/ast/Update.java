package ast;

public class Update extends AbstractNode{

    private final String operator = "<=";
    private final String array = "mem";
    private Expr left, right;


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
