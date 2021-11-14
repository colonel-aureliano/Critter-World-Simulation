package ast;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;

import java.util.Random;

public class Mutation3 implements Mutation{

    @Override
    public boolean equals(Mutation m) {
        return m instanceof Mutation3;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if (!canApply(node)) return Maybe.none();
        Random rand = new Random();
        Node root = ((AbstractNode) node).getRoot();
        Node searchNode = root;
        while (searchNode == node || searchNode.getCategory() != node.getCategory()) {
            searchNode = root.nodeAt(rand.nextInt(root.size()));
        }
        Node copy = searchNode.clone();
        ((AbstractNode) node).replace(copy);
        return Maybe.some(program);
    }

    @Override
    public boolean canApply(Node n) {
        switch (n.getCategory()) {
            case PROGRAM:
                return false;
            case COMMAND:
            case ACTION:
                if(((AbstractNode) n).getRoot().getChildren().size()==1) return false;
                return true;
            case RULE:
                try {
                    return ((AbstractNode) n).getParent().get().getChildren().size() > 1;
                } catch (NoMaybeValue e) {
                    return false; // case PROGRAM
                }
            case UPDATE:
                try {
                    Node parent = ((AbstractNode) n).getParent().get();
                    int i = 1;
                    if (parent.getChildren().get(parent.getChildren().size() - 1).getCategory()
                            == NodeCategory.ACTION) {
                        i = 2; // last Node in Command is Action
                    }
                    return parent.getChildren().size() > i;
                } catch (NoMaybeValue e) {
                    return false; // case PROGRAM
                }
            case CONDITION:
                if (n instanceof Relation) {
                    Node root = ((AbstractNode) n).getRoot();
                    return root.getChildren().size() != 1
                            || root.getChildren().get(0).getChildren().get(0) != n;
                }
            case EXPRESSION:
                return true;
        }
        throw new IllegalArgumentException("Unsupported Mutation");
    }
}
