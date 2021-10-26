package ast;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import exceptions.SyntaxError;

import java.util.List;
import java.util.Random;

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
        // TODO
        return false;

    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if (!canApply(node) ) return Maybe.none();
        switch (type) {
            case 1:
                try {
                    Node parent = ((AbstractNode) node).getParent().get();

                    int i = parent.getChildren().indexOf(node);
                    parent.getChildren().remove(i);
                    if(parent instanceof Command){
                        return Maybe.some(program);
                    }
                    parent.getChildren().add(i,node.getChildren().get(0));

                    try {
                        int in = 1;
                        while (!parent.classInv()) {
                            parent.getChildren().remove(i);
                            parent.getChildren().add(i, node.getChildren().get(in));
                            in++;
                        }
                    } catch(Exception e){
                        System.err.println("canApply() does not catch an invalid" +
                                "node attempting to undergo Mutation 1");
                        return Maybe.none();
                    }
                } catch (NoMaybeValue noMaybeValue) {
                    noMaybeValue.printStackTrace();
                }
                return Maybe.some(program);
            case 2:
                List<Node> nodes = node.getChildren();
                Node temp = nodes.get(0);
                nodes.set(0, nodes.get(1));
                nodes.set(1, temp);
                return Maybe.some(program);
            case 3:

        }
        return null;
    }

    @Override
    public boolean canApply(Node n) {
        switch(type){
            case 1:
                if (n.getCategory() == NodeCategory.RULE ||
                        n.getCategory() == NodeCategory.UPDATE ||
                        n.getCategory() == NodeCategory.ACTION ) {
                    try {
                        return ((AbstractNode) n).getParent().get().getChildren().size() > 1;
                    } catch (NoMaybeValue e) {
                        return false;
                    }
                }
                else if (n instanceof Relation || n.getCategory() == NodeCategory.PROGRAM){
                    return false; // n is Relation or ProgramImpl
                }
                return true;
            case 2:
                return n.getChildren().size() == 2; // If swap, the node must have two children.
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
