package ast;

import cms.util.maybe.Maybe;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Mutation4 implements Mutation{

    @Override
    public boolean equals(Mutation m) {
        return m instanceof Mutation4;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if (!canApply(node)) return Maybe.none();
        Random rand = new Random();
        int i;
        if (node instanceof Action) {
            List<Action.Operator> ActionOps = Arrays.asList(Action.Operator.values());
            Action.Operator o = ((Action) node).operator;
            do {
                i = rand.nextInt(ActionOps.size());
                ((Action) node).operator = ActionOps.get(i);
            } while (o == ((Action) node).operator || ((Action) node).operator == Action.Operator.SERVE);
        } else if (node instanceof BinaryCondition) ((BinaryCondition) node).resetOperator();
        else if (node instanceof Relation) {
            List<Relation.Operator> RelationOps = Arrays.asList(Relation.Operator.values());
            Relation.Operator o = ((Relation) node).operator;
            do {
                i = rand.nextInt(RelationOps.size());
                ((Relation) node).operator = RelationOps.get(i);
            } while (o == ((Relation) node).operator);
        } else if (node instanceof BinaryExpr) {
            List<BinaryExpr.Operator> BinExprOps = Arrays.asList(BinaryExpr.Operator.values());
            BinaryExpr.Operator o = ((BinaryExpr) node).operator;
            do {
                i = rand.nextInt(BinExprOps.size());
                ((BinaryExpr) node).operator = BinExprOps.get(i);
            } while (o == ((BinaryExpr) node).operator);
        } else if (node instanceof Sensor) {
            List<Sensor.Operator> SensorOps = Arrays.asList(Sensor.Operator.values());
            Sensor.Operator o = ((Sensor) node).operator;
            do {
                i = rand.nextInt(SensorOps.size());
                ((Sensor) node).operator = SensorOps.get(i);
            } while (o==((Sensor) node).operator || ((Sensor) node).operator == Sensor.Operator.SMELL);
        } else if (node instanceof Factor) {
            Node replacement;
            try {
                do {
                    i = Integer.parseInt(node.toString());
                    i += java.lang.Integer.MAX_VALUE / rand.nextInt();
                    replacement = new Factor(i);
                    ((AbstractNode)replacement).interpreter=((AbstractNode)program).interpreter;
                    ((AbstractNode)replacement).root=program;
                } while (node.toString().equals(replacement.toString()));
            } catch (Exception e) {
                //canApply() didn't catch an invalid node attempting to undergo Mutation 4
                return Maybe.none();
            }
            ((Factor) node).replace(replacement);
        }

        return Maybe.some(program);
    }

    @Override
    public boolean canApply(Node n) {
        switch (n.getCategory()) {
            case PROGRAM:
            case RULE:
            case COMMAND:
            case UPDATE:
                return false;
            case ACTION:
                if (((AbstractNode) n).hasChild) return false; // SERVE
                return true;
            case CONDITION:
                return true;
            case EXPRESSION:
                if (n instanceof Mem) return false;
                else if (n instanceof Sensor && !((AbstractNode) n).hasChild) return false; // SMELL
                return true;
        }
        throw new IllegalArgumentException("Unsupported Mutation");
    }
}
