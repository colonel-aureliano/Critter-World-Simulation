package model;

import ast.*;
import cms.util.maybe.Maybe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Critter implements ReadOnlyCritter {

    private String name;
    private int[] mem; // length >= 7, each entry >= 0
    private Program program;
    private Node lastRuleExecuted;
    private CritterObserver co;
    private boolean dead = false;

    // maxmium energy of critter = 500*mem[3]

    /**
     * Constructs a Critter object.
     *
     * @param n   species name
     * @param arr mem values
     * @param p   program rules
     * @param w   critter observer
     */
    public Critter(String n, int[] arr, Program p, CritterObserver w) {
        name = n;
        mem = arr;
        program = p;
        co = w;
        ((ProgramImpl) p).critterWorldSetUp(this, (World) w);
        if (!classInv()) {
            setDefault();
        }
    }

    private void setDefault() {
        if (co == null) {
            System.out.println("WARNING: Critter " + name + " does not have a CritterObserver object, " +
                    "cannot perform step().");
            if (memInv()) return;
        }
        System.out.println("WARNING: critter created with invalid values, will reset invalid values to default.");
        if (mem.length < 7) {
            mem = new int[]{Constants.MIN_MEMORY, 1, 1, 1, Constants.INITIAL_ENERGY, 1, 0};
            System.out.println("Resetting mem array to default array.");
        }
        if (mem[0] < 7) {
            mem[0] = mem.length;
            System.out.println("Resetting mem[0] to mem.length.");
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
            mem[4] = Constants.INITIAL_ENERGY;
            System.out.println("Resetting mem[4] to " + Constants.INITIAL_ENERGY + ".");
        }
        if (mem[5] != 1) {
            mem[5] = 1;
            System.out.println("Resetting mem[5] to 1.");
        }
        if (mem[6] < 0 || mem[6] > 99) {
            mem[6] = 0;
            System.out.println("Resetting mem[6] to 0");
        }
    }

    private boolean classInv() {
        if (memInv() && name != null && program != null && co != null) return true;
        return false;
    }

    private boolean memInv() {
        if (mem.length >= Constants.MIN_MEMORY && (mem[0] >= 7 && mem[0] == mem.length) && mem[1] >= 1 && mem[2] >= 1
                && mem[3] >= 1 && mem[4] >= 1 && mem[5] >= 1 && (mem[6] >= 0 && mem[6] <= 99)) {
            return true;
        }
        return false;
    }

    /**
     * Mutates the program of this critter with a random mutation.
     */
    public void mutate(){
        program.mutate();
        return;
    }

    /**
     * Advances the critter by one time step.
     */
    public void step() {
        if (dead) return;
        List<Node> l = program.getChildren();

        mem[5] = 0;
        while (mem[5] < Constants.MAX_RULES_PER_TURN) {
            mem[5]++;
            boolean noRuleTrue = true;
            for (Node i : l) {
                if (((Rule) i).value()) { // Condition for rule is true. Should perform update/action.
                    lastRuleExecuted = i;
                    Node c = i.getChildren().get(1); // Command node
                    for (Node n : c.getChildren()) {
                        if (n.getCategory() == NodeCategory.ACTION) {
                            Action.Operator o = ((Action) n).getOperator();
                            int complexity = l.size() * Constants.RULE_COST + (mem[2] + mem[1]) * Constants.ABILITY_COST;
                            act(o, complexity, n);
                            return;
                        }
                        try { // n is an instance of Update, with children l,r, represents mem[l] := r
                            mem[((Expr) n.getChildren().get(0)).value()] = ((Expr) n.getChildren().get(1)).value();
                        } catch (Exception e) {
                        } // do not perform update
                    }
                    noRuleTrue = false;
                    break;
                }
            }
            // On this pass through the rules, no rule's condition is true.
            if (noRuleTrue) break;
        }
        mem[4] += mem[3] * Constants.SOLAR_FLUX;
    }

    private void act(Action.Operator o, int complexity, Node n) {
        // For any action that will decrease critter's energy, critter may die before or after performing action.
        Random rand = new Random();
        switch (o) {
            case WAIT:
                mem[4] += mem[3] * Constants.SOLAR_FLUX;
                break;
            case EAT:
                if (500 * mem[3] - mem[4] > 0) {
                    mem[4] += co.onEatFood(this, 500 * mem[3] - mem[4]);
                }
                break;
            case GROW:
                mem[4] -= mem[3] * complexity * Constants.GROW_COST;
                if (isDead()) return;
                mem[3]++;
                break;
            case FORWARD:
                mem[4] -= mem[3] * Constants.MOVE_COST;
                if (isDead()) return;
                co.onMove(this, true);
                break;
            case BACKWARD:
                mem[4] -= mem[3] * Constants.MOVE_COST;
                if (isDead()) return;
                co.onMove(this, false);
                break;
            case LEFT:
                co.onTurn(this, true);
                break;
            case RIGHT:
                co.onTurn(this, false);
                break;
            case SERVE:
                // if energy=2, size=2:
                // command serve[any amount] will kill critter and no food will be served
                // if energy=200, size=2:
                // command serve[190] will decrease enregy to 8 and 190 will be served
                mem[4] -= mem[3]; // decreasing energy by size
                if (isDead()) return;
                int amount = ((Expr) n.getChildren().get(0)).value();
                // If onServeFood is unsuccessful (such as when there is a critter/rock ahead),
                // energy will only be decreased by size.
                if (mem[4] <= amount) {
                    if (co.onServeFood(this, mem[4])) mem[4] = 0;
                    // decreasing energy by amount of food deposited
                } else {
                    if (co.onServeFood(this, amount)) mem[4] -= amount;
                }
                isDead();
                break;
            case ATTACK:
                mem[4] -= mem[3] * Constants.ATTACK_COST;
                if (mem[4] < 0) {
                    isDead();
                    return;
                }
                // If critter has just enough energy to attack, it attacks and then dies.
                if (co.canAttack(this)) {
                    Critter toAttack = (Critter) co.getCritterAhead(this);
                    double d = Constants.DAMAGE_INC * (mem[3] * mem[2] - toAttack.mem[3] * toAttack.mem[1]);
                    double p = 1.0 / (1.0 + Math.exp(-d));
                    int attackEffect = (int) Math.round(Constants.BASE_DAMAGE * mem[3] * p);
                    toAttack.mem[4] -= attackEffect;
                    toAttack.isDead();
                }
                isDead();
                break;
            case BUD:
                mem[4] -= Constants.BUD_COST * complexity;
                if (mem[4] < 0) {
                    isDead();
                    return;
                }
                // If critter has just enough energy to bud, it buds and then dies.
                int[] arr = new int[mem.length];
                for (int in = 0; in < arr.length; in++) {
                    arr[in] = mem[in];
                    if (in == 3) arr[in] = 1;
                    if (in == 4) arr[in] = Constants.INITIAL_ENERGY;
                    if (in == 6) arr[in] = 0;
                    if (in >= 7) arr[in] = 0;
                }
                Program p = (Program) program.clone();
                int ran = rand.nextInt(8); // Mutations may occur with 1/4 chance.
                while (ran <= 1) {
                    if (ran == 0) p.mutate(); // mutating rule set
                    if (ran == 1) { // mutating attributes
                        int r = rand.nextInt(3);
                        if (rand.nextBoolean()) arr[r]++;
                        else arr[r]--;
                        if (r == 0 && arr[r] < 7) arr[r] = 7;
                        else if (arr[r] < 1) arr[r] = 1;
                    }
                    ran = rand.nextInt(8);
                }
                Critter baby = new Critter(name + "'s baby", arr, p, co);
                co.onBud(this, baby);
                isDead();
                break;
            case MATE:
                if (co.wantToMate(this)) {
                    Critter partner = (Critter) co.getCritterAhead(this);
                    mem[4] -= Constants.MATE_COST * complexity;
                    partner.mem[4] -= Constants.MATE_COST * complexity;
                    if (mem[4] < 0 && partner.mem[4] < 0) {
                        isDead();
                        partner.isDead();
                        return;
                    } else if (partner.mem[4] < 0) {
                        // If only one of the mating partners die, mating is unsuccessful
                        // and energy is also deducted from the other partner.
                        partner.isDead();
                        return;
                    } else if (mem[4] < 0) {
                        isDead();
                        return;
                    }
                    // If critters have just enough energy to mate, they mate and then die.

                    int[] a;
                    if (rand.nextBoolean()) a = new int[partner.getMemory()[0]];
                    else a = new int[mem[0]];
                    a[0] = a.length;
                    for (int i = 1; i < 3; i++) {
                        if (rand.nextBoolean()) a[i] = partner.getMemory()[i];
                        else a[i] = mem[i];
                    }
                    a[3] = 1;
                    a[4] = Constants.INITIAL_ENERGY;
                    if (rand.nextBoolean()) a[5] = partner.getMemory()[5];
                    else a[5] = mem[5];
                    a[6] = 0;
                    for (int i = 7; i < a.length; i++) {
                        a[i] = 0;
                    }
                    List<Node> motherRules = program.getChildren();
                    List<Node> fatherRules = partner.program.getChildren();
                    List<Node> ln = new ArrayList<>();
                    int s;
                    if (rand.nextBoolean()) s = motherRules.size();
                    else s = fatherRules.size();
                    int m = 0;
                    int f = 0;
                    for (int in = 0; in < s; in++) {
                        if (rand.nextBoolean()) {
                            if (m < motherRules.size()) ln.add(motherRules.get(m++));
                            else in--;
                        } else {
                            if (f < fatherRules.size()) ln.add(fatherRules.get(f++));
                            else in--;
                        }
                    }
                    Program pro = new ProgramImpl(ln);
                    Critter b = new Critter(name + "&" + partner.name + "'s baby", a, pro, co);
                    co.onMate(this, partner, b);

                    isDead();
                    partner.isDead();
                }
                break;
        }
    }

    private boolean isDead() {
        if (mem[4] <= 0) {
            co.onDeath(this,mem[3]*Constants.FOOD_PER_SIZE);
            dead = true;
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
        if (lastRuleExecuted == null) return Maybe.none();
        return Maybe.some(lastRuleExecuted.toString());
    }
}
