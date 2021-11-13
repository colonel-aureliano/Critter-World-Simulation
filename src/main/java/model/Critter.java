package model;

import ast.*;
import cms.util.maybe.Maybe;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Critter implements ReadOnlyCritter {

    private String name;
    private int[] mem; // length >= 7, each entry >= 0
    private Program program;
    private Node lastRuleExecuted;
    private CritterObserver co;

    // maxmium energy of critter = 500*mem[3]

    /**
     * Constructs a Critter object.
     * @param n species name
     * @param arr mem values, length >= 7
     * @param p program rules
     */
    public Critter(String n, int[] arr, Program p){
        name=n;
        mem=arr;
        program=p;
        if(!classInv()){
            System.out.println("WARNING: critter created with invalid values, will reset invalid values to default.");
            setDefault();
        }
        ((ProgramImpl) p).critterToObserve(this);
    }

    private void setDefault(){
        if (mem[0] < 7) {
            mem[0] = mem.length;
            System.out.println("Resetting mem[0] to mem.length().");
        }
        if (mem[1] < 1) {
            mem[1] = 1;
            System.out.println("Resetting mem[1] to 1.");
        }
        if (mem[2] < 1) {
            mem[2] = 1;
            System.out.println("Resetting mem[2] to 1.");
        }
        if (mem[3] < 1) {
            mem[3] = 1;
            System.out.println("Resetting mem[3] to 1.");
        }
        if (mem[4] < 1) {
            mem[4] = 250;
            System.out.println("Resetting mem[4] to 250.");
        }
        if (mem[5] != 1) {
            mem[5] = 1;
            System.out.println("Resetting mem[5] to 1.");
        }
        if (mem[6] < 0 || mem[6] > 99){
            mem[6] = 0;
            System.out.println("Resetting mem[6] to 0");
        }
    }

    private boolean classInv(){
        if(mem.length>=7 && mem[0]>=7 && mem[1]>=1 && mem[2]>=1
                && mem[3]>=1 && mem[4]>=1 && mem[5]>=1 && (mem[6]>=0 && mem[6]<=99) ){
            if(name!=null && program!=null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Advances the critter by one time step.
     */
    public void step(){
        List<Node> l = program.getChildren();

        mem[5]=0;
        while(mem[5]<999) {
            mem[5]++;
            boolean noRuleTrue = true;
            for (Node i : l) {
                if (((Rule) i).value()) {
                    // Condition for rule is true. Should perform update/action.
                    lastRuleExecuted=i;
                    Node c = i.getChildren().get(1); // Command node
                    for (Node n : c.getChildren()) {
                        if (n.getCategory() == NodeCategory.ACTION) {
                            // For any action that will decrease critter's energy, critter may die.
                            // Critter can die before performing action.
                            int complexity = l.size()*Constants.RULE_COST+(mem[2]+mem[1])*Constants.ABILITY_COST;
                            Action.Operator o = ((Action)n).getOperator();
                            switch(o){
                                case WAIT:
                                    mem[4]+=mem[3]*Constants.SOLAR_FLUX;
                                    break;
                                case EAT:
                                    if(500*mem[3]-mem[4] > 0) {
                                        mem[4]+=co.onEatFood(this, 500 * mem[3] - mem[4]);
                                    }
                                    break;
                                case GROW:
                                    mem[4]-=mem[3]*complexity*Constants.GROW_COST;
                                    if (isDead()) return;
                                    mem[3]++;
                                    break;
                                case FORWARD:
                                    mem[4]-=mem[3]*Constants.MOVE_COST;
                                    if (isDead()) return;
                                    co.onMove(this,true);
                                    break;
                                case BACKWARD:
                                    mem[4]-=mem[3]*Constants.MOVE_COST;
                                    if (isDead()) return;
                                    co.onMove(this,false);
                                    break;
                                case LEFT:
                                    mem[4]-=1; // TURN costs 1 unit of energy.
                                    if (isDead()) return;
                                    co.onTurn(this,true);
                                    break;
                                case RIGHT:
                                    mem[4]-=1; // TURN costs 1 unit of energy.
                                    if (isDead()) return;
                                    co.onTurn(this,false);
                                    break;
                                case SERVE:
                                    // if energy=2, size=2:
                                    // command serve[any amount] will kill critter and no food will be served
                                    // if energy=200, size=2:
                                    // command serve[190] will decrease enregy to 8 and 190 will be served
                                    mem[4]-=mem[3]; // decreasing energy by size
                                    if (isDead()) return;
                                    int amount = ((Expr)n.getChildren().get(0)).value();
                                    if(mem[4] <= amount){
                                        co.onServeFood(this,mem[4]); //TODO should serveFood return an int?
                                        mem[4]=0; // decreasing energy by amount of food deposited
                                    }
                                    else{
                                        mem[4]-=amount;
                                        co.onServeFood(this,amount); // decreasing energy by amount of food deposited
                                    }
                                    if (isDead()) return;
                                    break;
                                case ATTACK:
                                    mem[4]-=mem[3]*Constants.ATTACK_COST;
                                    // If critter has just enough energy to attack, it attacks and then dies.
                                    if (mem[4] < 0){
                                        isDead();
                                        return;
                                    }
                                    if(co.canAttack(this)){
                                        Critter toAttack = (Critter) co.onAttack(this);
                                        double d = Constants.DAMAGE_INC*(mem[3]*mem[2]-toAttack.mem[3]*toAttack.mem[1]);
                                        double p = 1.0/(1.0+Math.exp(-d));
                                        int attackEffect = (int) Math.round(Constants.BASE_DAMAGE*mem[3]*p);
                                        toAttack.mem[4]-=attackEffect;
                                        toAttack.isDead();
                                    }
                                    if (isDead()) return;
                                    break;
                                case BUD://TODO
                                    break;
                                case MATE:
                                    break;
                            }
                            return; // mem[4] should be > 0
                        }
                        // n is an instance of Update, with children l,r, represents mem[l] := r
                        try {
                            mem[((Expr) n.getChildren().get(0)).value()] = ((Expr) n.getChildren().get(1)).value();
                        } catch (IndexOutOfBoundsException e) {
                            // do not perform update
                        }
                    }
                    noRuleTrue=false;
                    break;
                }
            }
            // On this pass through the rules, no rule's condition is true.
            if(noRuleTrue) break;
        }
        mem[4]+=mem[3]*Constants.SOLAR_FLUX;
    }

    private boolean isDead() {
        if(mem[4]<=0){
            co.onDeath(this);
            return true;
        }
        return false;
    }

    @Override
    public String getSpecies() {
        return name;
    }

    @Override
    public int[] getMemory() {
        return mem.clone();
    }

    @Override
    public String getProgramString() {
        return program.toString();
    }

    @Override
    public Maybe<String> getLastRuleString() {
        if(lastRuleExecuted==null) return Maybe.none();
        return Maybe.some(lastRuleExecuted.toString());
    }
}
