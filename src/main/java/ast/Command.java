package ast;

import java.util.ArrayList;
import java.util.List;

public class Command extends AbstractNode{

    /** A list of commands, all Updates except the last one, which could be an Action. */
    private List<Command> coms;

    /**
     * An AST representation of command.
     */
    public Command() {
        coms = new ArrayList<Command>();
    }

    /**
     * Add node n to the end of commands list.
     * Requires: n must be Update or Action
     * @param n
     */
    public void add(Command n) {
        coms.add(n);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(coms.get(0));
        for (int i = 1; i < coms.size(); i++){
            sb.append("\n"+"\t"+coms.get(i));
        }
        return sb.toString();
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.COMMAND;
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
