package model;

import cms.util.maybe.Maybe;

import java.util.ArrayList;
import java.util.List;

public class ROnlyWorld implements ReadOnlyWorld{

    String name;
    protected int steps;

    /**
     * 0: the hex is empty
     * n > 0: critter with index n-1
     * n = -1: rock
     * n < -1: food with energy level (-n)-1
     */
    protected int[][] map;

    protected List<ReadOnlyCritter> critters;
    protected List<Integer> directions;

    /**
     * Create a world with width w and height h
     */
    public ROnlyWorld(int w, int h, String n) {
        steps = 0;
        name = n;
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
    public boolean addRock(int c, int r) {
        map[c][r] = -1;
        return true;
    }

    /**
     * Add food to world
     * @param c column
     * @param r row
     * @param amount total energy value
     * Requires: (c,r) must be valid position
     */
    public boolean addFood(int c, int r, int amount) {
        map[c][r] = - amount - 1;
        return true;
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
    public boolean addCritter(int c, int r, ReadOnlyCritter critter, int direction) {
        critters.add(critter);
        directions.add(direction);
        map[c][r] = critters.size();
        return true;
    }

    @Override
    public int getSteps() {
        return steps;
    }

    @Override
    public int getNumberOfAliveCritters() {
        int count = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; i < map[0].length; i++) {
                if (map[i][j] > 0) count ++;
            }
        }
        return count;
    }

    @Override
    public Maybe<ReadOnlyCritter> getReadOnlyCritter(int c, int r) {
        if (map[c][r] <= 0) return Maybe.none();
        return Maybe.some(critters.get(map[c][r]-1));
    }

    @Override
    public int getTerrainInfo(int c, int r) {
        if ((c+r) % 2 == 1) return -1; // invalid location
        try {
            int i = map[c][r];
            return i >= 0 ? 0 : i;
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
    }
}
