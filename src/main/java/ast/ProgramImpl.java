package ast;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import model.Critter;
import model.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A data structure representing a critter program.
 */
public class ProgramImpl extends AbstractNode implements Program {

    public ProgramImpl(List<Node> lr) {
        super(lr); // a list of Rules
        setRoot(this);
    }

    public void critterWorldSetUp(Critter c, World w){
        setInterpreter(new Interpreter(c,w));
    }

    private int random6(){
        Random r = new Random();
        return r.nextInt(6) + 1;
    }

    @Override
    public Program mutate() {
        mutateWithType(random6());
        return this;
    }

    /**
     * Mutates this program with mutation type i.
     * If mutation type i is not applicable, a random applicable mutation type is used.
     * @return mutation type applied
     */
    public int mutateWithType(int i){
        Mutation m = null;
        switch (i) {
            case 1:
                m = MutationFactory.getRemove();
                break;
            case 2:
                m = MutationFactory.getSwap();
                break;
            case 3:
                m = MutationFactory.getReplace();
                break;
            case 4:
                m = MutationFactory.getTransform();
                break;
            case 5:
                m = MutationFactory.getInsert();
                break;
            case 6:
                m = MutationFactory.getDuplicate();
                break;
        }
        boolean b = false;
        for(int in = 0; in < size(); in++){
            if(m.canApply(nodeAt(in))){
                b = true;
                break;
            }
        }
        if(!b) return mutateWithType(random6());
        int index;
        Random r = new Random();
        do {
            index = r.nextInt(size());
        } while(!m.canApply(nodeAt(index)));
        try {
            m.apply(this, this.nodeAt(index)).get();
            return i;
        } catch (NoMaybeValue noMaybeValue) {
            throw new IllegalArgumentException("Program: mutation " + i + " failed at Node position " + index);
        }
    }

    @Override
    public Maybe<Program> mutate(int index, Mutation m) {
        return m.apply(this, this.nodeAt(index));
    }

    @Override
    public Maybe<Node> findNodeOfType(NodeCategory type) {
        for (int i = 0; i < this.size(); i++) {
            if (nodeAt(i).getCategory().equals(type)) {
                return Maybe.some(nodeAt(i));
            }
        }
        return Maybe.none();
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.PROGRAM;
    }

    @Override
    public Node cloneHelper() {
        List<Node> ln = new ArrayList<>();
        for (Node n : children) {
            ln.add(n.clone());
        }
        return new ProgramImpl(ln);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node n : children) {
            sb.append(n);
        }
        return sb.toString();
    }

    public boolean classInv() {
        return children.size() != 0 && children.stream().allMatch(x -> x instanceof Rule);
    }
}
