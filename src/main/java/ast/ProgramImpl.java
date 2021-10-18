package ast;

import cms.util.maybe.Maybe;

/** A data structure representing a critter program. */
public class ProgramImpl extends AbstractNode implements Program {

    @Override
    public Program mutate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Maybe<Program> mutate(int index, Mutation m) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Maybe<Node> findNodeOfType(NodeCategory type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.PROGRAM;
    }

    @Override
    public String toString(){
        // TODO Auto-generated method stub
        return null;
    }

    public boolean classInv() {
        // TODO
        return false;
    }
}
