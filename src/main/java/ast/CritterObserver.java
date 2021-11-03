package ast;

import model.ReadOnlyCritter;

public class CritterObserver {
    // Observes a critter, meant to be called by Mem.
    // TODO Sensor needs to observe the world as well.

    ReadOnlyCritter c;
    public CritterObserver(ReadOnlyCritter c){
        this.c=c;
    }

    /**
     * Returns the mem value of c at index i.
     * @param i 0<=i<=c.getMemory().length
     * @return -1 if i is invalid
     */
    public int getMem(int i){
        if(!(0<=i && i<c.getMemory().length)){
            return -1;
        }
        return c.getMemory()[i];
    }
}
