package ast;

import parse.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/** A representation of a critter rule. */
public class Rule extends AbstractNode {
    private String operator = "-->";
    private Condition left;
    private Command right;

    /**
     * A AST representation of Condition --> Command.
     * @param l
     * @param r
     */
    public Rule(Condition l, Command r){
        left = l;
        right = r;
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
