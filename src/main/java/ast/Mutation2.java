package ast;

import cms.util.maybe.Maybe;

import java.util.List;

public class Mutation2 implements Mutation{
    @Override
    public boolean equals(Mutation m) {
        return m instanceof Mutation2;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if (!canApply(node)) return Maybe.none();
        List<Node> nodes = node.getChildren();
        Node temp = nodes.get(0);
        nodes.set(0, nodes.get(1));
        nodes.set(1, temp);
        return Maybe.some(program);
    }

    @Override
    public boolean canApply(Node n) {
        try {
            return n.getChildren().size() >= 2 && n.getCategory() != NodeCategory.RULE;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unsupported Mutation");
        }
    }
}
