package model;

public interface CritterObserver {

    // Requires: c must be alive

    /**
     * report content of hex in direction dir.
     * 0: empty, or outside world boundary
     * n > 0: critter with appearance n
     * n = -1: rock
     * n < -1: food with energy value (-n) -1
     */
    int onNearby(ReadOnlyCritter c, int dir);

    /**
     * report content of hex directly in front of critter with distance dist in the same format as onNearby
     * negative distance treated as zero distance
     */
    int onAhead(ReadOnlyCritter c, int dist);

    /**
     * report 1000 * distance + direction to the nearest food, up to MAX_SMELL_DISTANCE = 10
     * if no food, return 1,000,000
     */
    int onSmell(ReadOnlyCritter c);

    /**
     * Return the amount of food consumed by c
     */
    int onEatFood(ReadOnlyCritter c, int n);

    /**
     * Return true if serve success
     * Requires: n > 0
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
     * Requires: baby must be a new critter
     */
    boolean onBud(ReadOnlyCritter parent, ReadOnlyCritter baby);

    /**
     * return true is there is a critter in front of c
     */
    boolean canAttack(ReadOnlyCritter c);

    /**
     * return the critter that in front of c
     * Requires: c.ahead[1] is a critter
     */
    ReadOnlyCritter getCritterAhead(ReadOnlyCritter c);

    /**
     * mark a critter in world as dead
     */
    boolean onDeath(ReadOnlyCritter c, int energy);

    /**
     * return true if a critter in front of c want to mate and is also facing toward c
     * return false otherwise, mark c as attempting to mate
     */
    boolean wantToMate(ReadOnlyCritter c);

    /**
     * put baby behind a randomly chosen parent with direction as the parent in front of it
     * Requires: baby must be a new critter
     */
    boolean onMate(ReadOnlyCritter parent1, ReadOnlyCritter parent2, ReadOnlyCritter baby);


}
