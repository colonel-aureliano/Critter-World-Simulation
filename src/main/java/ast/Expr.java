package ast;

import java.util.List;

/** A critter program expression that has an integer value. */
public abstract class Expr extends AbstractNode {

    public Expr(){}

    public Expr(Node l, Node r){
        super(l,r);
    }

    public Expr(Node s){
        super(s);
    }


    @Override
    public NodeCategory getCategory() {
        return NodeCategory.EXPRESSION;
    }
    
}
