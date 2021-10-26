package ast;

/** A representation of a critter rule. */
public class Rule extends AbstractNode {
    private String operator = "-->";

    /**
     * A AST representation of Condition --> Command.
     * @param l
     * @param r
     */
    public Rule(Condition l, Command r){
        super(l,r);
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.RULE;
    }

    @Override
    public Node clone() {
        return new Rule((Condition)children.get(0).clone(),(Command) children.get(1).clone());
    }

    @Override
    public String toString(){
        return children.get(0)+" "+operator+" "+children.get(1)+";"+'\n';
    }

    public boolean classInv() {
        return children.get(0)!=null && children.get(1)!=null;
    }
}
