package ast;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import exceptions.SyntaxError;

import java.security.spec.ECField;
import java.util.List;
import java.util.Random;

public class MutationImpl implements Mutation {
    int type;

    /**
     * Constructs a Mutation of type t, where
     * 1: Remove
     * 2: Swap
     * 3: Replace
     * 4: Transform
     * 5: Insert
     * 6: Duplicate
     * @param t
     */
    public MutationImpl(int t){
        type = t;
    }

    @Override
    public boolean equals(Mutation m) {
        // TODO
        return false;

    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if (!canApply(node) ) return Maybe.none();
        switch (type) {
            case 1:
                try {
                    Node parent = ((AbstractNode) node).getParent().get();

                    int i = parent.getChildren().indexOf(node);
                    parent.getChildren().remove(i);
                    if(parent instanceof Command){
                        return Maybe.some(program);
                    }
                    parent.getChildren().add(i,node.getChildren().get(0));

                    try {
                        int in = 1;
                        while (!parent.classInv()) {
                            parent.getChildren().remove(i);
                            parent.getChildren().add(i, node.getChildren().get(in));
                            in++;
                        }
                    } catch(Exception e){
                        System.err.println("canApply() does not catch an invalid" +
                                "node attempting to undergo Mutation 1");
                        return Maybe.none();
                    }
                } catch (NoMaybeValue noMaybeValue) {
                    noMaybeValue.printStackTrace();
                }
                return Maybe.some(program);
            case 2:
                List<Node> nodes = node.getChildren();
                Node temp = nodes.get(0);
                nodes.set(0, nodes.get(1));
                nodes.set(1, temp);
                return Maybe.some(program);
            case 3:
                String c = node.getClass().getSimpleName();
                Node root = ((AbstractNode) node).getRoot();
                int size = root.size();
                Random rand = new Random();

                Node replacement;
                int z = 0;
                while(true){
                    int i = rand.nextInt(size);
                    if(root.nodeAt(i).getClass().getSimpleName().equals(c)){
                        if(root.nodeAt(i)==node){
                            continue;
                        }
                        replacement = root.nodeAt(i);
                        break;
                    }
                    z++;
                    if(z>size*30){
                        return Maybe.none();
                    }
                }

                try {
                    Node parent = ((AbstractNode) node).getParent().get();
                    int i = parent.getChildren().indexOf(node);
                    parent.getChildren().remove(i);
                    parent.getChildren().add(i,replacement);
                    return Maybe.some(program);
                } catch (NoMaybeValue noMaybeValue) {
                    return Maybe.none();
                }
            case 4:
                // Mutation 4 if successful gurantees that the mutated node is different from the original node.

                rand = new Random();
                if(node instanceof Action){
                    Action.Operator operator = null;
                    do {
                        int i = rand.nextInt(10);
                        switch(i){
                            case 0:
                                operator = Action.Operator.WAIT;
                                break;
                            case 1:
                                operator = Action.Operator.FORWARD;
                                break;
                            case 2:
                                operator = Action.Operator.BACKWARD;
                                break;
                            case 3:
                                operator = Action.Operator.LEFT;
                                break;
                            case 4:
                                operator = Action.Operator.RIGHT;
                                break;
                            case 5:
                                operator = Action.Operator.EAT;
                                break;
                            case 6:
                                operator = Action.Operator.ATTACK;
                                break;
                            case 7:
                                operator = Action.Operator.GROW;
                                break;
                            case 8:
                                operator = Action.Operator.BUD;
                                break;
                            case 9:
                                operator = Action.Operator.MATE;
                                break;
                        }
                    } while(!((Action) node).resetOperator(operator));
                }

                if(node instanceof BinaryExpr){
                    BinaryExpr.Operator operator = null;
                    do{
                        int i = rand.nextInt(5);
                        switch(i){
                            case 0:
                                operator= BinaryExpr.Operator.PLUS;
                                break;
                            case 1:
                                operator= BinaryExpr.Operator.MINUS;
                                break;
                            case 2:
                                operator= BinaryExpr.Operator.MULTIPLY;
                                break;
                            case 3:
                                operator= BinaryExpr.Operator.DIVIDE;
                                break;
                            case 4:
                                operator= BinaryExpr.Operator.MOD;
                                break;
                        }
                    }while(!((BinaryExpr) node).resetOperator(operator));
                }

                if(node instanceof Relation){
                    Relation.Operator operator = null;
                    do{
                        int i = rand.nextInt(6);
                        switch(i){
                            case 0:
                                operator=Relation.Operator.LESS_THAN;
                                break;
                            case 1:
                                operator=Relation.Operator.LESS_THAN_OR_EQUAl;
                                break;
                            case 2:
                                operator=Relation.Operator.GREATER_THAN;
                                break;
                            case 3:
                                operator=Relation.Operator.GREATER_THAN_OR_EQUAL;
                                break;
                            case 4:
                                operator=Relation.Operator.EQUAL;
                                break;
                            case 5:
                                operator=Relation.Operator.NOT_EQUAL;
                                break;
                        }
                    }while(!((Relation) node).resetOperator(operator));
                }

                if(node instanceof BinaryCondition){
                    ((BinaryCondition) node).resetOperator();
                }

                if(node instanceof Factor) {
                    int i;
                    try {
                        do {
                            i = Integer.parseInt(node.toString());
                            i += java.lang.Integer.MAX_VALUE / rand.nextInt();
                            replacement = new Factor(i);
                        } while (node.toString().equals(replacement.toString()));
                    } catch (Exception e) {
                        System.err.println("canApply() does not catch an invalid" +
                                "node attempting to undergo Mutation 4"); // TODO need to comment out
                        return Maybe.none();
                    }
                    try {
                        Node parent = ((AbstractNode) node).getParent().get();
                        int in = parent.getChildren().indexOf(node);
                        parent.getChildren().remove(in);
                        parent.getChildren().add(in,replacement);
                    } catch (NoMaybeValue noMaybeValue) {
                        return Maybe.none();
                    }
                }
                return Maybe.some(program);
            case 5:

        }
        return null;
    }

    @Override
    public boolean canApply(Node n) {
        switch(type){
            case 1:
                if (n.getCategory() == NodeCategory.RULE ||
                        n.getCategory() == NodeCategory.UPDATE ||
                        n.getCategory() == NodeCategory.ACTION ) {
                    try {
                        return ((AbstractNode) n).getParent().get().getChildren().size() > 1;
                    } catch (NoMaybeValue e) {
                        return false;
                    }
                }
                else if (n instanceof Relation || n.getCategory() == NodeCategory.PROGRAM
                || n instanceof Factor && n.getChildren()==null){
                    return false; // n is Relation or ProgramImpl
                }
                return true;
            case 2:
                return n.getChildren().size() == 2; // If swap, the node must have two children.
            case 3:
            case 4:
                if(n instanceof Command || n instanceof Mem || n instanceof ProgramImpl
                        || n instanceof Rule || n instanceof Update){
                    return false; // A Node cannot replace itself.
                }
                if(n instanceof Action && ((Action) n).hasChild){
                    // If node is SERVE, no replacement is allowed.
                    // Because no replacement of SERVE can preserve its child.
                    return false;
                }
                if(n instanceof Factor){
                    try{
                        Integer.parseInt(n.toString());
                    } catch (Exception e){
                        // n is a Factor that stores (Expr) or -((Expr))
                        return false;
                    }
                }
                return true;
            case 5:
                return n.getCategory() != NodeCategory.PROGRAM;
            case 6:
                return n.getCategory() == NodeCategory.PROGRAM | n.getCategory() == NodeCategory.COMMAND;
            default:
                throw new IllegalArgumentException("Unsupported Mutation");
                
        }
    }
}
