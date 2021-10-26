package ast;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import exceptions.SyntaxError;

public class MutationImpl implements Mutation {
    int type;

    /**
     * Constructs a Mutation of type t, where
     * 1: Remove
     * 2: Swap
     * 3: Replace
     * 4: Transform
     * 5: Insert
     * 6: Duplicate
     * @param t
     */
    public MutationImpl(int t){
        type = t;
    }

    @Override
    public boolean equals(Mutation m) {
        return false;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if (!canApply(node) ) return Maybe.none();
        switch (type) {
            case 1:
                
        }
        return null;
    }

    @Override
    public boolean canApply(Node n) {
        switch(type){
            case 1:
                if (n.getCategory() == NodeCategory.RULE || n.getCategory() == NodeCategory.PROGRAM) {
                    AbstractNode node = (AbstractNode) n;
                    try {
                        return node.getParent().get().getChildren().size() > 1;
                    } catch (NoMaybeValue e) {
                        return false; // n is Program
                    }
                }
                return true;
            case 2:
                return n.getChildren().size() > 1;
            case 3:
            case 4:
                return true;
            case 5:
                return n.getCategory() != NodeCategory.PROGRAM;
            case 6:
                return n.getCategory() == NodeCategory.PROGRAM | n.getCategory() == NodeCategory.COMMAND;
            default:
                throw new IllegalArgumentException("Unsupported Mutation");
                
        }
    }
}
