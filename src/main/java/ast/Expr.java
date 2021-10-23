package ast;

import java.util.List;

/** A critter program expression that has an integer value. */
public abstract class Expr extends AbstractNode {

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.EXPRESSION;
    }

    abstract public void add(Expr e); // to support linking multiple expressions together
}
