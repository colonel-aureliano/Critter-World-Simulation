package ast;

import cms.util.maybe.Maybe;

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
        type=t;
    }

    @Override
    public boolean equals(Mutation m) {
        return false;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        return null;
    }

    @Override
    public boolean canApply(Node n) {
        switch(type){
            case 1:
                
        }
        return false;
    }
}
