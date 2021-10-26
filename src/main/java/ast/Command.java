package ast;

import java.util.ArrayList;
import java.util.List;

public class Command extends AbstractNode{

    /** A list of commands comprising of >= 0 Update and (1 or 0) Action.
     * Size must be greater than or equal to 1.*/

    /**
     * An AST representation of command.
     */
    public Command(List<Node> ln) {
        super(ln);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(children.get(0));
        for (int i = 1; i < children.size(); i++){
            sb.append("\n"+"\t"+children.get(i));
        }
        return sb.toString();
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.COMMAND;
    }

    @Override
    public boolean classInv() {
        return children.size()>=1;
    }
}
