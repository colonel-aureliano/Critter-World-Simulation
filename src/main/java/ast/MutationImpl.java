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
        Random rand = new Random();

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
                        System.err.println("canApply() didn't catch an invalid node attempting to undergo Mutation 1");
                        return Maybe.none();
                    }
                } catch (NoMaybeValue noMaybeValue) {
                    noMaybeValue.printStackTrace();
                }
                break;
            case 2:
                List<Node> nodes = node.getChildren();
                Node temp = nodes.get(0);
                nodes.set(0, nodes.get(1));
                nodes.set(1, temp);
                break;
            case 3:
                String c = node.getClass().getSimpleName();
                Node root = ((AbstractNode) node).getRoot();
                int size = root.size();

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
                    parent.getChildren().set(i,replacement);
                } catch (NoMaybeValue noMaybeValue) {
                    return Maybe.none();
                }
                break;
            case 4:
                // Mutation 4 if successful gurantees that the mutated node is different from the original node.

                rand = new Random();
                if(node instanceof Action){
                    Action.Operator operator;
                    do {
                        int i = rand.nextInt(10);
                        operator=getActionOperator(i);
                    } while(!((Action) node).resetOperator(operator));
                }

                if(node instanceof BinaryExpr){
                    BinaryExpr.Operator operator;
                    do{
                        int i = rand.nextInt(5);
                        operator = getBinaryExprOperator(i);
                    }while(!((BinaryExpr) node).resetOperator(operator));
                }

                if(node instanceof Relation){
                    Relation.Operator operator;
                    do{
                        int i = rand.nextInt(6);
                        operator = getRelationOperator(i);
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
                        System.err.println("canApply() didn't catch an invalid node attempting to undergo Mutation 4");
                        return Maybe.none();
                    }
                    try {
                        Node parent = ((AbstractNode) node).getParent().get();
                        int in = parent.getChildren().indexOf(node);
                        parent.getChildren().set(in,replacement);
                    } catch (NoMaybeValue noMaybeValue) {
                        return Maybe.none();
                    }
                }
                break;

            case 5:
                if(node instanceof Factor){
                    // Parent of factor is either Mem(0) or Sensor(1) or BinaryExpr or Relation.
                    int which = -1; // which tyep of node to insert
                    try {
                        Node p = ((Factor) node).getParent().get();
                        if (p instanceof Relation || p instanceof Mem || p instanceof Sensor
                                || p instanceof BinaryExpr){
                            which=rand.nextInt(2);
                        }
                        switch(which){
                            case 0:
                                Mem m = new Mem(new Factor(0));
                                ((Factor) node).setParent(m);
                                break;
                            case 1:
                                int l = rand.nextInt(3);
                                Sensor.Operator o = null;
                                switch(l){
                                    case 0:
                                        o= Sensor.Operator.NEARBY;
                                        break;
                                    case 1:
                                        o= Sensor.Operator.AHEAD;
                                        break;
                                    case 2:
                                        o= Sensor.Operator.RANDOM;
                                        break;
                                }
                                Sensor s=new Sensor(o,null);
                                ((Factor) node).setParent(s);
                                break;
                        }
                    } catch (NoMaybeValue e) {
                        e.printStackTrace();
                    }
                }

                if(node instanceof Relation | node instanceof BinaryCondition){
                    try{
                        Node p = ((Condition) node).getParent().get();

                        int i = rand.nextInt(2);
                        BinaryCondition.Operator o = getBinaryConditionOperator(i);
                        i = rand.nextInt(6);
                        Relation.Operator operator = getRelationOperator(i);
                        BinaryCondition b = new BinaryCondition((Condition) node,o,
                                new Relation(new Factor((int) (100*rand.nextDouble())),operator,
                                        new Factor((int) (100*rand.nextDouble()))));

                        if(p instanceof BinaryCondition){
                            int in = p.getChildren().indexOf(node);
                            p.getChildren().set(in,b);
                        }
                        else if(p instanceof Rule){
                            int in = p.getChildren().indexOf(node);
                            p.getChildren().set(in,b);
                        }
                        else{
                            System.err.println("canApply() didn't catch an invalid node attempting to undergo Mutation 5");
                            return Maybe.none();
                        }
                    } catch (NoMaybeValue noMaybeValue) {
                        noMaybeValue.printStackTrace();
                    }

                }

                if(node instanceof Mem){
                    // Acceptable direct paretns of Mem are Relation, BinaryExpr, and Update.
                    // The latter two do not allow Mutation 5.
                    try {
                        Node p = ((Condition) node).getParent().get();
                        // Parent is Relation
                        if(p instanceof Relation){
                            BinaryExpr.Operator operator= getBinaryExprOperator(rand.nextInt(5));
                            BinaryExpr be = new BinaryExpr((Expr) node,operator,new Factor((int) (100*rand.nextDouble())));
                        }
                        else{
                            System.err.println("canApply() didn't catch an invalid node attempting to undergo Mutation 5");
                            return Maybe.none();
                        }

                    } catch (NoMaybeValue noMaybeValue) {
                        noMaybeValue.printStackTrace();
                    }
                }
                break;

            case 6:
                if (node instanceof ProgramImpl){
                    // should append a randomly selected Rule
                    int n = node.getChildren().size();
                    int in = rand.nextInt(n);
                    node.getChildren().add(node.getChildren().get(in));
                }
                else if(node instanceof Command){
                    // should append a randomly selected Update
                    int n = node.getChildren().size();
                    int in = rand.nextInt(n);
                    if(node.getChildren().get(n-1) instanceof Action){
                        if(in==n-1){
                            in--;
                        }
                        node.getChildren().add(in,node.getChildren().get(in));
                    }
                    else{
                        node.getChildren().add(node.getChildren().get(in));
                    }
                }
                else{
                    System.err.println("canApply() didn't catch an invalid node attempting to undergo Mutation 6");
                    return Maybe.none();
                }
                break;

        }
        return Maybe.some(program);
    }

    private Action.Operator getActionOperator(int i){
        switch(i){
            case 0:
                return Action.Operator.WAIT;
            case 1:
                return Action.Operator.FORWARD;
            case 2:
                return Action.Operator.BACKWARD;
            case 3:
                return Action.Operator.LEFT;
            case 4:
                return Action.Operator.RIGHT;
            case 5:
                return Action.Operator.EAT;
            case 6:
                return Action.Operator.ATTACK;
            case 7:
                return Action.Operator.GROW;
            case 8:
                return Action.Operator.BUD;
            case 9:
                return Action.Operator.MATE;
        }
        throw new IllegalArgumentException ("getActionOperator error.");
    }

    private BinaryCondition.Operator getBinaryConditionOperator(int i){
        switch(i){
            case 0:
                return BinaryCondition.Operator.AND;
            case 1:
                return BinaryCondition.Operator.OR;
        }
        throw new IllegalArgumentException ("getBinaryConditionOperator error.");
    }

    private Relation.Operator getRelationOperator(int i) {
        switch(i){
            case 0:
                return Relation.Operator.LESS_THAN;
            case 1:
                return Relation.Operator.LESS_THAN_OR_EQUAl;
            case 2:
                return Relation.Operator.GREATER_THAN;
            case 3:
                return Relation.Operator.GREATER_THAN_OR_EQUAL;
            case 4:
                return Relation.Operator.EQUAL;
            case 5:
                return Relation.Operator.NOT_EQUAL;
        }
        throw new IllegalArgumentException ("getRelationOperator error.");
    }

    private BinaryExpr.Operator getBinaryExprOperator(int i) {
        switch(i){
            case 0:
                return BinaryExpr.Operator.PLUS;
            case 1:
                return BinaryExpr.Operator.MINUS;
            case 2:
                return BinaryExpr.Operator.MULTIPLY;
            case 3:
                return BinaryExpr.Operator.DIVIDE;
            case 4:
                return BinaryExpr.Operator.MOD;
        }
        throw new IllegalArgumentException ("getBinaryExprOperator error.");
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
                // A: parent of Node B
                // B
                // C: the Node to be inserted
                // A is a valid parent of C / C is a valid child of A
                // B is a valid child of C / C is a valid parent of B
                if(n instanceof Action || n instanceof Update){
                    // Command, only parent of Action and Update, can't be the parent of itself.
                    return false;
                }
                if(n instanceof Command){
                    // Rule, only parent of Command, can't be the parent of itself.
                    return false;
                }
                try {
                    if (n instanceof Mem && (((Mem) n).getParent().get() instanceof Update
                            || ((Mem) n).getParent().get() instanceof BinaryExpr)){
                        // Update, the parent of Mem, has to be the parent of Mem.
                        // Mem can't be the parent of itself.
                        return false;
                    }
                } catch (NoMaybeValue noMaybeValue) {
                    noMaybeValue.printStackTrace();
                }
                return n.getCategory() != NodeCategory.PROGRAM;
            case 6:
                if(n.getChildren().size()==1 && n.getChildren().get(0).getCategory()==NodeCategory.ACTION){
                    return false;
                }
                return n.getCategory() == NodeCategory.PROGRAM | n.getCategory() == NodeCategory.COMMAND;
            default:
                throw new IllegalArgumentException("Unsupported Mutation");
                
        }
    }
}
