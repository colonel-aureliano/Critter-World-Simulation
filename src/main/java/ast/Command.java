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
    public Node cloneHelper(){
        List<Node> ln = new ArrayList<>();
        for (Node n: children){
            ln.add(n.clone());
        }
        return new Command(ln);
    }

    @Override
    public String toString(){
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v) {
        return v.visit(this);
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
