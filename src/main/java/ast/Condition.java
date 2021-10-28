package ast;

/** An abstract class representing a Boolean condition in a critter program. */
public abstract class Condition extends AbstractNode {

    public Condition(Node l, Node r){
        super(l,r);
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.CONDITION;
    }
}
