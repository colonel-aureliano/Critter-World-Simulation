package model;

import cms.util.maybe.Maybe;

public interface ReadOnlyWorld {
    /** @return number of steps */
    int getSteps();

    /** @return number of alive critters. */
    int getNumberOfAliveCritters();

    /**
     * @param c column id.
     * @param r row id.
     * @return the critter at the specified hex.
     */
    Maybe<ReadOnlyCritter> getReadOnlyCritter(int c, int r);

    /**
     * @param c column id.
     * @param r row id.
     * @return 0 is empty or a critter, -1 is rock, -X is (X-1) food. Treat out-of-bound or invalid hex as rock.
     */
    int getTerrainInfo(int c, int r);

    /**
     * Return number of hexes of world
     */
    int getSize();

    /**
     * Return height of world
     */
    int getHeight();

    /**
     * Return width of world
     */
    int getWidth();

    /**
     * Return direction of Critter at (c, r) if it exists
     * Otherwise return -1
     */
    int getCritterDirection(int c, int r);
}
