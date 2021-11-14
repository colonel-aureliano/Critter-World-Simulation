package ast;

import model.ReadOnlyCritter;

public class CritterO {
    // Observes a critter, meant to be called by Mem.
    // TODO Sensor needs to observe the world as well.

    ReadOnlyCritter c;
    public CritterO(ReadOnlyCritter c){
        this.c=c;
    }

    /**
     * Returns the mem value of c at index i.
     * @param i 0<=i<=c.getMemory().length
     * @return 0 if i is invalid
     */
    public int getMem(int i){
        if(!(0<=i && i<c.getMemory().length)){
            return 0;
        }
        return c.getMemory()[i];
    }
}
