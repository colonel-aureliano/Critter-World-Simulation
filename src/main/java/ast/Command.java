package ast;

import java.util.ArrayList;
import java.util.List;

public class Command extends AbstractNode{

    private List<Node> coms;

    public Command(Node n) {
        coms = new ArrayList<Node>();
        coms.add(n);
    }

    public void add(Node n) {
        coms.add(n);
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public NodeCategory getCategory() {
        return null;
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
