package ast;

import parse.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/** A representation of a critter rule. */
public class Rule extends AbstractNode {
    private Operator operator;
    private Condition left;
    private Command right;

    public Rule(Condition l, Operator op, Command r){
        operator = op;
        left = l;
        right = r;
    }

    public enum Operator{
        DO;
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
