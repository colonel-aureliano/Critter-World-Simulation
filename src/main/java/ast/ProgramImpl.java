package ast;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** A data structure representing a critter program. */
public class ProgramImpl extends AbstractNode implements Program {

    public ProgramImpl(List<Node> lr){
        super(lr); // a list of rules
        setRoot(this);
    }

    @Override
    public Program mutate() {
        Random r = new Random();
        int i = r.nextInt(6)+1;
        Mutation m = null;
        switch(i){
            case 1:
                m=MutationFactory.getRemove();
                break;
            case 2:
                m=MutationFactory.getSwap();
                break;
            case 3:
                m=MutationFactory.getReplace();
                break;
            case 4:
                m=MutationFactory.getTransform();
                break;
            case 5:
                m=MutationFactory.getInsert();
                break;
            case 6:
                m=MutationFactory.getDuplicate();
                break;
        }

        do{
            i = r.nextInt(this.size());
        }while (!m.canApply(this.nodeAt(i)));

        try {
            return m.apply(this, this.nodeAt(i)).get();
        } catch (NoMaybeValue noMaybeValue) {
            throw new IllegalArgumentException("Program.mutate() failed.");
        }
    }

    /**
     * Mutates this program with mutation rule i on a random node.
     *
     * @return The root of the mutated AST
     */
    public Program mutateR(int i){
        Random r = new Random();
        Mutation m = null;
        switch(i){
            case 1:
                m=MutationFactory.getRemove();
                break;
            case 2:
                m=MutationFactory.getSwap();
                break;
            case 3:
                m=MutationFactory.getReplace();
                break;
            case 4:
                m=MutationFactory.getTransform();
                break;
            case 5:
                m=MutationFactory.getInsert();
                break;
            case 6:
                m=MutationFactory.getDuplicate();
                break;
        }
        do{
            i = r.nextInt(this.size());
        }while (!m.canApply(this.nodeAt(i)));

        try {
            return m.apply(this, this.nodeAt(i)).get();
        } catch (NoMaybeValue noMaybeValue) {
            throw new IllegalArgumentException("Program.mutateR() failed.");
        }
    }

    @Override
    public Maybe<Program> mutate(int index, Mutation m) {
        return m.apply(this, this.nodeAt(index));
    }

    @Override
    public Maybe<Node> findNodeOfType(NodeCategory type) {
        for(int i = 0; i < this.size(); i++){
            if(this.nodeAt(i).getCategory().equals(type)){
                return Maybe.some(this.nodeAt(i));
            }
        }
        return Maybe.none();
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
        return children.size()!=0 && children.stream().allMatch(x -> x instanceof Rule);
    }
}
