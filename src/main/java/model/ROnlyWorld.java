package model;

import cms.util.maybe.Maybe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
     * Create a world with width w, height h, and name n
     * Requires: w, h > 0
     */
    public ROnlyWorld(int w, int h, String n) {
        steps = 0;
        name = n;
        map = new int[w+1][h+1];
        critters = new ArrayList<>();
        directions = new ArrayList<>();
    }

    /**
     * Add a rock to world.
     * Requires: (c, r) is a hex inside the world
     * Checks: (c, r) is empty
     */
    public boolean addRock(int c, int r) {
        if (map[c][r] != 0) return false;
        map[c][r] = -1;
        return true;
    }

    /**
     * Add food to world. Can only add
     * Requires: (c, r) is a hex inside the world
     * Checks: (c, r) is empty of contain food
     */
    public boolean addFood(int c, int r, int amount) {
        if (map[c][r] > 0 | map[c][r] == -1) return false;
        if (map[c][r] == 0) map[c][r] = - amount - 1;
        else map[c][r] -= amount;
        return true;
    }

    /**
     * Add critter to world
     * Requires: (c, r) is a hex inside the world
     * Checks: (c, r) is empty
     */
    public boolean addCritter(int c, int r, ReadOnlyCritter critter, int direction) {
        if (map[c][r] != 0) return false;
        critters.add(critter);
        directions.add(direction);
        map[c][r] = critters.size();
        return true;
    }

    /**
     * return number of columns (c);
     */
    public int getColumns() {return map.length;}

    /**
     * return number of rows (w);
     */
    public int getRows() {return map[0].length;}

    /**
     * return true if there exists empty space in this world
     */
    public boolean hasEmptySpace() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; i < map[0].length; i++) {
                if (map[i][j] == 0) return true;
            }
        }
        return false;
    }

    /**
     * return an empty space in format (c, r)
     * if no empty space present, return (-1, -1)
     */
    public int[] getEmptySpace() {
        if (!hasEmptySpace()) return new int[]{-1, -1};
        int c, r;
        Random random = new Random();
        while (true) {
            c = random.nextInt(map.length);
            r = random.nextInt(map[0].length);
            if (map[c][r] == 0) return new int[]{c, r};
        }
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
                if (map[i][j] > 0) count++;
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

    /**
     * - empty space
     * # rock
     * d critter facing d
     * F food
     */
    public String print() {
        StringBuilder sb = new StringBuilder();
        for (int j = map[0].length - 1; j >= 0; j--) {
            for (int i = 0; i < map.length; i++) {
                if ((i + j)% 2 == 1) sb.append(" ");
                else if (map[i][j] < -1) sb.append("F");
                else if (map[i][j] == -1) sb.append("#");
                else if (map[i][j] == 0) sb.append("-");
                else sb.append(directions.get(map[i][j]-1));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
