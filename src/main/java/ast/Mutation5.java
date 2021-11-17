package ast;

import cms.util.maybe.Maybe;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Mutation5 implements Mutation{

    @Override
    public boolean equals(Mutation m) {
        return m instanceof Mutation5;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if (!canApply(node)) return Maybe.none();
        Random rand = new Random();
        Node insert = null;
        Node root = ((AbstractNode) node).getRoot();
        Node searchNode = root;
        if (node.getCategory() == NodeCategory.CONDITION) {
            while (searchNode == node || searchNode.getCategory() != NodeCategory.CONDITION) {
                searchNode = root.nodeAt(rand.nextInt(root.size()));
            }
            BinaryCondition.Operator[] ops = BinaryCondition.Operator.values();
            Condition right = (Condition) searchNode.clone();
            insert = new BinaryCondition((Condition) node, ops[rand.nextInt(2)], right);
        } else if (node.getCategory() == NodeCategory.EXPRESSION) {
            switch (rand.nextInt(3)) {
                case 0: // Binary Expr
                    while (searchNode == node || searchNode.getCategory() != NodeCategory.EXPRESSION) {
                        searchNode = root.nodeAt(rand.nextInt(root.size()));
                    }
                    Expr right = (Expr) searchNode.clone();
                    BinaryExpr.Operator[] Binops = BinaryExpr.Operator.values();
                    insert = new BinaryExpr((Expr) node, Binops[rand.nextInt(5)], right);
                    break;
                case 1: // Sensor
                    List<Sensor.Operator> Senops = Arrays.asList(Sensor.Operator.values());
                    insert = new Sensor(Senops.get(rand.nextInt(3)), (Expr) node); // cannot be SMELL
                    break;
                case 2: // Mem
                    insert = new Mem((Expr)node);
                    break;
                case 3: // NEGATIVE factor, uncompleted
                    break;
            }
        }
        ((AbstractNode) node).replace(insert);
        ((AbstractNode) insert).root = program;
        ((AbstractNode) insert).interpreter = ((ProgramImpl) program).interpreter;
        return Maybe.some(program);
    }

    @Override
    public boolean canApply(Node n) {
        switch (n.getCategory()) {
            case PROGRAM:
            case RULE:
            case COMMAND:
            case UPDATE:
            case ACTION:
                return false;
            case CONDITION:
                if (n instanceof Relation) {
                    Node root = ((AbstractNode) n).getRoot();
                    return root.getChildren().size() != 1
                            || root.getChildren().get(0).getChildren().get(0) != n;
                }
            case EXPRESSION:
                return true;
        }
        throw new IllegalArgumentException("Unsupported Mutation");
    }
}
