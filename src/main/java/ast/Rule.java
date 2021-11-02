package ast;

/**
 * A representation of a critter rule.
 */
public class Rule extends AbstractNode {
    private String operator = "-->";

    /**
     * A AST representation of Condition --> Command.
     *
     * @param l
     * @param r
     */
    public Rule(Condition l, Command r) {
        super(l, r);
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.RULE;
    }

    @Override
    public Node clone() {
        return new Rule((Condition) children.get(0).clone(), (Command) children.get(1).clone());
    }

    @Override
    public String toString() {
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v) {
        return v.visit(this, operator);
    }

    public boolean classInv() {
        return children.size() == 2 && children.get(0) instanceof Condition
                && children.get(1) instanceof Command;
    }

    /**
     * @return whether its Condition is true
     */
    public boolean value(){
        return ((Condition)getChildren().get(0)).value();
    }
}
