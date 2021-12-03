package model;

import cms.util.maybe.Maybe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ROnlyWorld implements ReadOnlyWorld{

    /** World name */
    protected String name;

    /** number of time steps passed */
    protected int steps;

    /**
     * 0: the hex is empty
     * n > 0: critter with index n-1
     * n = -1: rock
     * n < -1: food with energy level (-n)-1
     * Invariants:
     * 1) map[c][r] for which c+r is odd must be 0
     * 2) there cannot be two map values > 0 that are the same (duplicate critters)
     */
    protected int[][] map;

    /** Stores critters whose index n+1 corresponds to map value
     * Ex. critters[0] corresponds to 1 on the map */
    protected List<ReadOnlyCritter> critters;

    /** Stores direction of critters.
     * Invariant: 0 <= direction <= 5 */
    protected List<Integer> directions;

    protected boolean Manna;
    protected boolean ForcedMutation;

    /**
     * Create a Read Only world width w, height h, and name n
     * Getter class
     */
    public ROnlyWorld(int w, int h, String n) {
        steps = 0;
        name = n;
        map = new int[w][h];
        critters = new ArrayList<>();
        directions = new ArrayList<>();
    }

    @Override
    public int getSize() { return (int) Math.ceil(map.length * map[0].length / 2.0); }
    @Override
    public int getHeight() { return map[0].length; }
    @Override
    public int getWidth() { return map.length; }
    @Override
    public int getCritterDirection(int c, int r) {
        if (map[c][r] <= 0) return -1;;
        return directions.get(map[c][r]-1);
    }

    /**
     * return true if there exists empty space in this world
     */
    protected boolean hasEmptySpace() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 0) return true;
            }
        }
        return false;
    }

    /**
     * return an empty space in format (c, r)
     * Requires: has empty space
     */
    public int[] getEmptySpace() {
        int c, r;
        Random random = new Random();
        while (true) {
            c = random.nextInt(map.length);
            r = random.nextInt(map[0].length);
            if ((c+r) % 2 == 0 && map[c][r] == 0) return new int[]{c, r};
        }
    }

    /**
     * return true if there exists food space in this world
     */
    protected boolean hasFoodSpace() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 0 | map[i][j] < -1) return true;
            }
        }
        return false;
    }

    /**
     * return a space for food in format (c, r)
     * Requires: has space for food
     */
    protected int[] getFoodSpace() {
        if (!hasEmptySpace()) return new int[]{-1, -1};
        int c, r;
        Random random = new Random();
        while (true) {
            c = random.nextInt(map.length);
            r = random.nextInt(map[0].length);
            if ((c+r) % 2 == 0 && (map[c][r] == 0 | map[c][r] < -1)) return new int[]{c, r};
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
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) count++;
            }
        }
        return count;
    }

    @Override
    public Maybe<ReadOnlyCritter> getReadOnlyCritter(int c, int r) {
        try {
            if (map[c][r] <= 0) return Maybe.none();
        } catch (IndexOutOfBoundsException e) {
            return Maybe.none();
        }
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
