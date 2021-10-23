package ast;

import parse.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/** A representation of a critter rule. */
public class Rule extends AbstractNode {

    public Rule(List<Node> ln){
        super(ln);
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.RULE;
    }

    @Override
    public String toString(){
        // TODO Auto-generated method stub
        return null;
    }

    public boolean classInv() {
        // TODO implement/override
        return false;
    }
}
