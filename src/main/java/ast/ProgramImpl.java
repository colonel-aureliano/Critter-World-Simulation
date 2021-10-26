package ast;

import cms.util.maybe.Maybe;

import java.util.ArrayList;
import java.util.List;

/** A data structure representing a critter program. */
public class ProgramImpl extends AbstractNode implements Program {

    public ProgramImpl(List<Node> lr){
        super(lr);
        setRoot(this);
    }

    @Override
    public Program mutate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Maybe<Program> mutate(int index, Mutation m) {
        return m.apply(this, this.nodeAt(index));
    }

    @Override
    public Maybe<Node> findNodeOfType(NodeCategory type) {
        return null;
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.PROGRAM;
    }

    @Override
    public Node clone(){
        List<Node> ln = new ArrayList<>();
        for (Node n: children){
            ln.add(n.clone());
        }
        return new ProgramImpl(ln);
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
