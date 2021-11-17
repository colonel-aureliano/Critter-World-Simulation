package ast;

import model.ReadOnlyCritter;
import model.World;

public class Interpreter {
    // Observes a critter and the world it inhabits.
    // Meant to be called by Mem and Sensor nodes.

    ReadOnlyCritter c;
    World w;

    public Interpreter(ReadOnlyCritter c, World w) {
        this.c = c;
        this.w = w;
    }

    /**
     * Returns the mem value of c at index i.
     *
     * @param i 0<=i<=c.getMemory().length
     * @return 0 if i is invalid
     */
    public int getMem(int i) {
        if (!(0 <= i && i < c.getMemory().length)) {
            return 0;
        }
        return c.getMemory()[i];
    }

    /**
     * Returns the value of smell for critter c in world w.
     */
    public int smell() {
        return w.onSmell(c);
    }

    /**
     * Returns the value of nearby[i] for critter c in world w.
     */
    public int nearby(int i) {
        return w.onNearby(c, i);
    }

    /**
     * Returns the value of ahead[i] for critter c in world w.
     */
    public int ahead(int i) {
        return w.onAhead(c, i);
    }
}
