package ast;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;

import java.util.Random;

public class Mutation1 implements Mutation{

    @Override
    public boolean equals(Mutation m) {
        return m instanceof Mutation1;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if (!canApply(node)) return Maybe.none();
        Random rand = new Random();
        try {
            Node parent = ((AbstractNode) node).getParent().get();
            int i = parent.getChildren().indexOf(node);
            parent.getChildren().remove(i);
            if (node instanceof BinaryCondition || node instanceof BinaryExpr) {
                int r = rand.nextInt(2);
                parent.getChildren().add(i, node.getChildren().get(r));
            } else if (node.getCategory() == NodeCategory.EXPRESSION) {
                // Factor, Sensor, Mem
                parent.getChildren().add(i, node.getChildren().get(0));
            }
        } catch (NoMaybeValue e) {
            // cannot occur
        }
        return Maybe.some(program);
    }

    @Override
    public boolean canApply(Node n) {
        switch (n.getCategory()) {
            case PROGRAM:
            case COMMAND:
                return false;
            case RULE:
            case UPDATE:
            case ACTION:
                try {
                    return ((AbstractNode) n).getParent().get().getChildren().size() > 1;
                } catch (NoMaybeValue e) {
                    return false; // case PROGRAM
                }
            case CONDITION:
                if (n instanceof Relation) return false;
                return true; // n instance of BinaryCondition
            case EXPRESSION:
                if (n instanceof Factor || n instanceof Sensor) {
                    if (!((Expr) n).hasChild) return false;
                }
                return true;
        }
        throw new IllegalArgumentException("Unsupported Mutation");
    }
}
