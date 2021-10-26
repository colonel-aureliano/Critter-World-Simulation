package ast;

/** An abstract class representing a Boolean condition in a critter program. */
public abstract class Condition extends AbstractNode {

    public Condition(Node l, Node r){
        super(l,r);
    }

    public Condition(Node s){
        super(s);
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.CONDITION;
    }
}
