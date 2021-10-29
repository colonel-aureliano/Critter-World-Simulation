package ast;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import exceptions.SyntaxError;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MutationImpl implements Mutation {
    int type;
    Random rand;

    /**
     * Constructs a Mutation of type t, where
     * 1: Remove
     * 2: Swap
     * 3: Replace
     * 4: Transform
     * 5: Insert
     * 6: Duplicate
     *
     * @param t
     */
    public MutationImpl(int t) {
        type = t;
    }

    @Override
    public boolean equals(Mutation m) {
        return ((MutationImpl) m).type == type;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        rand = new Random();
        if (!canApply(node)) return Maybe.none();

        switch (type) {
            case 1:
                return mutate1(program, node);
            case 2:
                return mutate2(program, node);
            case 3:
                return mutate3(program, node);
            case 4:
                return mutate4(program, node);
            case 5:
                return mutate5(program, node);
            case 6:
                return mutate6(program, node);
        }
        return Maybe.none();
    }

    private Maybe<Program> mutate1(Program program, Node node) {
        try {
            Node parent = ((AbstractNode) node).getParent().get();
            int i = parent.getChildren().indexOf(node);
            parent.getChildren().remove(i);
            if (node instanceof BinaryCondition | node instanceof BinaryExpr) {
                int r = rand.nextInt(2);
                parent.getChildren().add(i, node.getChildren().get(r));
                if(parent instanceof Mem && !parent.classInv()){
                    if(r == 1){ // to avoid e.g. mem[-1]
                        r=0;
                    }
                    else{
                        r=1;
                    }
                    parent.getChildren().remove(i);
                    parent.getChildren().add(i, node.getChildren().get(r));
                }
            } else if (node.getCategory() == NodeCategory.EXPRESSION) {
                // Factor, Sensor, Mem
                parent.getChildren().add(i, node.getChildren().get(0));
            }
        } catch (NoMaybeValue e) {
            e.printStackTrace(); // cannot happen
        }
        return Maybe.some(program);
    }

    private Maybe<Program> mutate2(Program program, Node node) {
        List<Node> nodes = node.getChildren();
        Node temp = nodes.get(0);
        nodes.set(0, nodes.get(1));
        nodes.set(1, temp);
        return Maybe.some(program);
    }

    private Maybe<Program> mutate3(Program program, Node node) {
        Node root = ((AbstractNode) node).getRoot();
        Node searchNode = root;
        while (searchNode == node | searchNode.getCategory() != node.getCategory()) {
            searchNode = root.nodeAt(rand.nextInt(root.size()));
        }
        Node copy = searchNode.clone();
        ((AbstractNode) node).replace(copy);
        return Maybe.some(program);
    }

    private Maybe<Program> mutate4(Program program, Node node) {
        int i;

        if (node instanceof Action) {
            List<Action.Operator> ActionOps = Arrays.asList(Action.Operator.values());
            do {
                i = rand.nextInt(ActionOps.size());
            } while (!((Action) node).resetOperator(ActionOps.get(i))); // cannot be SERVE
        } else if (node instanceof BinaryCondition) ((BinaryCondition) node).resetOperator();

        else if (node instanceof Relation) {
            List<Relation.Operator> RelationOps = Arrays.asList(Relation.Operator.values());
            do {
                i = rand.nextInt(RelationOps.size());
            } while (!((Relation) node).resetOperator(RelationOps.get(i)));
        } else if (node instanceof BinaryExpr) {
            List<BinaryExpr.Operator> BinExprOps = Arrays.asList(BinaryExpr.Operator.values());
            do {
                i = rand.nextInt(BinExprOps.size());
            } while (!((BinaryExpr) node).resetOperator(BinExprOps.get(i)));
        } else if (node instanceof Sensor) {
            List<Sensor.Operator> SensorOps = Arrays.asList(Sensor.Operator.values());
            do {
                i = rand.nextInt(SensorOps.size());
            } while (!((Sensor) node).resetOperator(SensorOps.get(i)));
        } else if (node instanceof Factor) {
            Node replacement;
            try {
                do {
                    i = Integer.parseInt(node.toString());
                    i += java.lang.Integer.MAX_VALUE / rand.nextInt();
                    replacement = new Factor(i);
                } while (node.toString().equals(replacement.toString()));
            } catch (Exception e) {
                System.err.println("canApply() didn't catch an invalid node attempting to undergo Mutation 4");
                return Maybe.none();
            }
            ((Factor) node).replace(replacement);
        }

        return Maybe.some(program);
    }

    private Maybe<Program> mutate5(Program program, Node node) {
        Node insert = null;
        Node root = ((AbstractNode) node).getRoot();
        Node searchNode = root;
        if (node.getCategory() == NodeCategory.CONDITION) {
            while (searchNode == node | searchNode.getCategory() != NodeCategory.CONDITION) {
                searchNode = root.nodeAt(rand.nextInt(root.size()));
            }
            BinaryCondition.Operator[] ops = BinaryCondition.Operator.values();
            Condition right = (Condition) searchNode.clone();
            insert = new BinaryCondition((Condition) node, ops[rand.nextInt(2)], right);
        } else if (node.getCategory() == NodeCategory.EXPRESSION) {
            switch (rand.nextInt(3)) {
                case 0: // Binary Expr
                    while (searchNode == node | searchNode.getCategory() != NodeCategory.EXPRESSION) {
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
        return Maybe.some(program);
    }

    private Maybe<Program> mutate6(Program program, Node node) {
        int n = node.getChildren().size();
        if (node instanceof Program) {
            node.getChildren().add(node.getChildren().get((int) (Math.random() * n)));
        } else if (node instanceof Command) {
            Node d;
            do {
                d = node.getChildren().get((int) (Math.random() * n));
            } while (d instanceof Action);

            if (node.getChildren().get(n - 1) instanceof Action) {
                node.getChildren().add(n - 1, d);
            } else {
                node.getChildren().add(d);
            }
        }
        return Maybe.some(program);
    }

    @Override
    public boolean canApply(Node n) {
        switch (type) {
            case 1:
                switch (n.getCategory()) {
                    case PROGRAM:
                    case COMMAND:
                        return false;
                    case RULE:
                    case UPDATE:
                    case ACTION:
                        try {
                            return ((AbstractNode) n).getParent().get().getChildren().size() > 1;
                        } catch (NoMaybeValue e) {
                            return false; // case PROGRAM
                        }
                    case CONDITION:
                        if (n instanceof Relation) return false;
                        return true; // n instance of BinaryCondition
                    case EXPRESSION:
                        if (n instanceof Factor | n instanceof Sensor) {
                            if (!((Expr) n).hasChild) return false;
                        }
                        return true;
                }

            case 2:
                try {
                    return n.getChildren().size() >= 2 && n.getCategory() != NodeCategory.RULE;
                } catch (Exception e) {
                    return false;
                }

            case 3:
                switch (n.getCategory()) {
                    case PROGRAM:
                    case COMMAND:
                    case ACTION:
                        return false;
                    case RULE:
                        try {
                            return ((AbstractNode) n).getParent().get().getChildren().size() > 1;
                        } catch (NoMaybeValue e) {
                            return false; // case PROGRAM
                        }
                    case UPDATE:
                        try {
                            Node parent = ((AbstractNode) n).getParent().get();
                            int i = 1;
                            if (parent.getChildren().get(parent.getChildren().size() - 1).getCategory()
                                    == NodeCategory.ACTION) {
                                i = 2; // last Node in Command is Action
                            }
                            return parent.getChildren().size() > i;
                        } catch (NoMaybeValue e) {
                            return false; // case PROGRAM
                        }
                    case CONDITION:
                        if (n instanceof Relation) {
                            Node root = ((AbstractNode) n).getRoot();
                            return root.getChildren().size() != 1
                                    | root.getChildren().get(0).getChildren().get(0) != n;
                        }
                    case EXPRESSION:
                        return true;
                }

            case 4:
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

            case 5:
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
                                    | root.getChildren().get(0).getChildren().get(0) != n;
                        }
                    case EXPRESSION:
                        return true;
                }

            case 6:
                switch (n.getCategory()) {
                    case PROGRAM:
                        return true;
                    case COMMAND:
                        return !(n.getChildren().get(0) instanceof Action);
                    case RULE:
                    case UPDATE:
                    case ACTION:
                    case CONDITION:
                    case EXPRESSION:
                        return false;
                }
            default:
                throw new IllegalArgumentException("Unsupported Mutation");
        }
    }
}
