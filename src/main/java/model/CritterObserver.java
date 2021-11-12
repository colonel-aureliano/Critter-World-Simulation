package model;

public interface CritterObserver {

    /**
     * Return the amount of food consumed by c
     */
    int onEatFood(ReadOnlyCritter c, int n);

    /**
     * Return true if serve success
     */
    boolean onServeFood(ReadOnlyCritter c, int n);

    /**
     * left turn if true, right turn if false
     */
    boolean onTurn(ReadOnlyCritter c, boolean left);

    /**
     * forward if true, backward if false
     */
    boolean onMove(ReadOnlyCritter c, boolean forward);

    /**
     * put baby behind with same direction as parent
     */
    boolean onBud(ReadOnlyCritter parent, ReadOnlyCritter baby);

    /**
     * return true is there is a critter in front of c
     */
    boolean canAttack(ReadOnlyCritter c);

    /**
     * return the critter that is attacked by c
     * Requires: canAttack is true
     */
    ReadOnlyCritter onAttack(ReadOnlyCritter c);

    /**
     * mark a critter in world as dead
     */
    boolean onDeath(ReadOnlyCritter c);

    /**
     * return true if a critter in front of c want to mate
     * return false otherwise, mark c as attempting to mate
     */
    boolean wantToMate(ReadOnlyCritter c);

    /**
     * return the critter in front of c
     * Requires: wantToMate is true, so that both critters are attempting to mate
     */
    ReadOnlyCritter matePartner(ReadOnlyCritter c);

    /**
     * put baby behind a randomly chosen parent with direction as the parent in front of it
     */
    boolean onMate(ReadOnlyCritter parent, ReadOnlyCritter baby);


}
