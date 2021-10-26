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
    public String toString(){
        return left+" "+operator+" "+right+";"+'\n';
    }

    public boolean classInv() {
        return left!=null && right!=null;
    }
}
