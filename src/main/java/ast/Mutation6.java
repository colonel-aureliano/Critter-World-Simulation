package ast;

import cms.util.maybe.Maybe;

public class Mutation6 implements Mutation{

    @Override
    public boolean equals(Mutation m) {
        return m instanceof Mutation6;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if (!canApply(node)) return Maybe.none();
        int n = node.getChildren().size();
        if (node instanceof Program) {
            node.getChildren().add(node.getChildren().get((int) (Math.random() * n)));
        } else if (node instanceof Command) {
            Node d;
            do {
                d = node.getChildren().get((int) (Math.random() * n));
            } while (d instanceof Action);

            if (node.getChildren().get(n - 1) instanceof Action) {
                node.getChildren().add(n - 1, d);
            } else {
                node.getChildren().add(d);
            }
        }
        return Maybe.some(program);
    }

    @Override
    public boolean canApply(Node n) {
        switch (n.getCategory()) {
            case PROGRAM:
                return true;
            case COMMAND:
                return !(n.getChildren().get(0) instanceof Action);
            case RULE:
            case UPDATE:
            case ACTION:
            case CONDITION:
            case EXPRESSION:
                return false;
        }
        throw new IllegalArgumentException("Unsupported Mutation");
    }
}
