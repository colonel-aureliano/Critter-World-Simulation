package ast;

import java.util.List;

/** A critter program expression that has an integer value. */
public abstract class Expr extends AbstractNode {

    public Expr(List<Node> ln){
        super(ln);
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.EXPRESSION;
    }
}
