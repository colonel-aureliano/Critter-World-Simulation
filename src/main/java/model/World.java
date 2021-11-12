package model;

import cms.util.maybe.Maybe;

import java.util.ArrayList;
import java.util.List;

public class World implements ReadOnlyWorld{

    protected int steps;

    /**
     * 0: the hex is empty
     * n > 0: critter with index n
     * n = -1: rock
     * n < -1: food with energy level (-n)-1
     */
    protected int[][] map;

    /**
     * start from 1
     */
    protected List<ReadOnlyCritter> critters;
    protected List<Integer> directions;

    /**
     * Create a world with width w and height h
     */
    public World(int w, int h) {
        steps = 0;
        map = new int[w][h];
        critters = new ArrayList<>();
        directions = new ArrayList<>();
    }

    /**
     * Add a rock to world
     * @param c column
     * @param r row
     * Requires: (c,r) must be valid position
     */
    protected void addRock(int c, int r) {
        map[c][r] = -1;
    }

    /**
     * Add food to world
     * @param c column
     * @param r row
     * @param amount total energy value
     * Requires: (c,r) must be valid position
     */
    protected void addFood(int c, int r, int amount) {
        map[c][r] = - amount - 1;
    }

    /**
     * Add critter to world
     * @param c column
     * @param r row
     * @param c
     * @param direction
     * @return index of critter
     * Requires: (c, r) must be valid position
     */
    protected int addCritter(int c, int r, ReadOnlyCritter critter, int direction) {
        critters.add(critter);
        directions.add(direction);
        return critters.size()-1;
    }




    @Override
    public int getSteps() {
        return steps;
    }

    @Override
    public int getNumberOfAliveCritters() {
        return 0;
    }

    @Override
    public Maybe<ReadOnlyCritter> getReadOnlyCritter(int c, int r) {
        return null;
    }

    @Override
    public int getTerrainInfo(int c, int r) {
        return 0;
    }
}
