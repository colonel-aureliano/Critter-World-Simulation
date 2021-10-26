package ast;

import cms.util.maybe.Maybe;

import java.util.List;

/** A data structure representing a critter program. */
public class ProgramImpl extends AbstractNode implements Program {

    public ProgramImpl(List<Node> lr){
        super(lr);
    }

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
        StringBuilder sb = new StringBuilder();
        for (Node n: children){
            sb.append(n);
        }
        return sb.toString();
    }

    public boolean classInv() {
        // TODO
        return false;
    }
}
