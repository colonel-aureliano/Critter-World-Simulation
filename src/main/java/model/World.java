package model;

import java.util.*;

public class World extends ROnlyWorld implements CritterObserver {

    /** Stores the critters that want to mate in the current time step */
    private HashSet<ReadOnlyCritter> puberty = new HashSet<>();

    /**
     * Create a Read Only world width w, height h, and name n
     * Setter class
     */
    public World(int w, int h, String n) {
        super(w, h, n);
    }

    /**
     * Load the two Controller parameters
     */
    public void loadParams(boolean enableManna, boolean enableForcedMutation) {
        Manna = enableManna;
        ForcedMutation = enableForcedMutation;
    }

    /**
     * Add a rock to world.
     * Checks: (c, r) is empty, inside the world, and valid position
     */
    public boolean addRock(int c, int r) {
        if ((c + r) % 2 == 1) return false;
        try {
            if (map[c][r] != 0) return false;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        map[c][r] = -1;
        return true;
    }

    /**
     * Add food to world.
     * Checks: (c, r) is empty or contains food, inside the world, and valid position
     */
    public boolean addFood(int c, int r, int amount) {
        if ((c + r) % 2 == 1) return false;
        try {
            if (map[c][r] > 0 | map[c][r] == -1) return false;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        if (map[c][r] == 0) map[c][r] = - amount - 1;
        else map[c][r] -= amount;
        return true;
    }

    /**
     * Add critter to world
     * Checks: (c, r) is empty, inside the world, and valid position
     */
    public boolean addCritter(int c, int r, ReadOnlyCritter critter, int direction) {
        if ((c + r) % 2 == 1) return false;
        try {
            if (map[c][r] != 0) return false;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        direction = direction % 6;
        if (direction < 0) direction += 6;
        critters.add(critter);
        directions.add(direction);
        map[c][r] = critters.size();
        return true;
    }

    /**
     * step the world one time
     */
    public boolean step() {
        puberty.clear();
        List<ReadOnlyCritter> temp = List.copyOf(critters);
        for (ReadOnlyCritter c: temp) {
            ((Critter) c).step();
            if (ForcedMutation) ((Critter) c).mutate();
            if (Manna) addManna();
        }
        steps++;
        return true;
    }

    /**
     * return true is Manna is added to World.
     */
    private boolean addManna() {
        if (getNumberOfAliveCritters() == 0) return false;
        Random r = new Random();
        if (r.nextInt(getNumberOfAliveCritters()) > 0) return false;
        for (int i = 0; i <= Constants.MANNA_COUNT * getSize() / 1000; i++) {
            if (!hasFoodSpace()) return false;
            int[] loc = getFoodSpace();
            addFood(loc[0], loc[1], Constants.MANNA_AMOUNT);
        }
        return true;
    }

    /**
     * Return the location of Critter in form [c, w, direction]
     * Requires: critter exists in World, otherwise return null
     */
    private int[] findCritter(ReadOnlyCritter c) {
        int index = critters.indexOf(c) + 1;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == index) return new int[]{i, j, directions.get(index - 1)};
            }
        }
        return null;
    }

    /**
     * Return the coordinate of the hex that is dis unit away from [c,r] in dir direction.
     * Might produce location index out of world boundary
     * Requires: 0 <= dir <= 5
     */
    private int[] sense(int c, int r, int dir, int dis) {
        int column = c;
        int row = r;
        switch (dir) {
            case 0:
                row += 2 * dis;
                break;
            case 1:
                column += dis;
                row += dis;
                break;
            case 2:
                column += dis;
                row -= dis;
                break;
            case 3:
                row -= 2 * dis;
                break;
            case 4:
                column -= dis;
                row -= dis;
                break;
            case 5:
                column -= dis;
                row += dis;
                break;
        }
        return new int[]{column, row};
    }

    @Override
    public int onNearby(ReadOnlyCritter c, int dir) {
        int[] info = findCritter(c);
        dir = dir % 6;
        if (dir < 0) dir += 6;
        int[] loc = sense(info[0], info[1], (dir + info[2]) % 6, 1);
        try {
            int i = map[loc[0]][loc[1]];
            if (i > 0) return critters.get(i - 1).getMemory()[6];
            return i;
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    @Override
    public int onAhead(ReadOnlyCritter c, int dist) {
        if (dist < 0) dist = 0;
        int[] info = findCritter(c);
        int[] loc = sense(info[0], info[1], info[2], dist);
        try {
            int i = map[loc[0]][loc[1]];
            if (i > 0) return critters.get(i - 1).getMemory()[6];
            return i;
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    @Override
    public int onSmell(ReadOnlyCritter c) {
        int[] info = findCritter(c);
        ArrayList<Integer[]> food = new ArrayList<>();
        for (int i = Math.max(0, info[0] - Constants.MAX_SMELL_DISTANCE);
             i < Math.min(map.length, info[0] + Constants.MAX_SMELL_DISTANCE + 1); i++) {
            for (int j = Math.max(0, info[1] - Constants.MAX_SMELL_DISTANCE);
                 j < Math.min(map[0].length, info[1] + Constants.MAX_SMELL_DISTANCE + 1); j++) {
                if (map[i][j] < -1) food.add(new Integer[]{i, j});
            }
        }
        if (food.size() == 0) return 1000000;

        ArrayList<Integer> distances = new ArrayList<Integer>();
        for (Integer[] y : food) {
            distances.add(distance(info[0], y[0], info[1], y[1]));
        }
        int distance = Collections.min(distances);

        int direction;
        Integer[] y = food.get(distances.indexOf(distance));
        double slope = (double) (y[1] - info[1]) / (y[0] - info[0]);
        if (slope >= 0 && slope < 3) direction = 1;
        else if (slope < 0 & slope >= -3) direction = 2;
        else if (slope > 0) direction = 0;
        else direction = 3;
        if ((y[0] - info[0]) < 0) direction *= -1;
        direction -= info[2];
        if (direction < 0) direction = (direction % 6) + 6;

        return 1000 * distance + direction;
    }

    /**
     * Return the minimum-distance walk along hexes between (x1, y1) and (x2, y2)
     */
    private int distance(int x1, int x2, int y1, int y2) {
        return Math.max(Math.max(Math.abs(x2-x1), Math.abs(x2-x1+y2-y1)/2), Math.abs(x2-x1-y2+y1));
    }

    @Override
    public int onEatFood(ReadOnlyCritter c, int n) {
        int[] info = findCritter(c);
        int[] loc = sense(info[0], info[1], info[2], 1);
        try {
            int food = Math.min(n, -(1 + map[loc[0]][loc[1]]));
            map[loc[0]][loc[1]] += food;
            if (map[loc[0]][loc[1]] == -1) map[loc[0]][loc[1]] = 0; // consume all food
            return food;
        } catch (IndexOutOfBoundsException e){
            return 0;
        }
    }

    @Override
    public boolean onServeFood(ReadOnlyCritter c, int n) {
        int[] info = findCritter(c);
        int[] loc = sense(info[0], info[1], info[2], 1);
        return addFood(loc[0], loc[1], n);
    }

    @Override
    public boolean onTurn(ReadOnlyCritter c, boolean left) {
        int index = critters.indexOf(c);
        int dir = directions.get(index);
        if (left) dir--;
        else dir++;
        if (dir > 5) dir -= 6;
        else if (dir < 0) dir += 6;
        directions.set(index,dir);
        return true;
    }

    @Override
    public boolean onMove(ReadOnlyCritter c, boolean forward) {
        int[] info = findCritter(c);
        int dir = info[2];
        if (!forward) dir = (dir + 3) % 6;
        int[] loc = sense(info[0], info[1], dir, 1);
        try {
            if (map[loc[0]][loc[1]] != 0) return false;
            map[loc[0]][loc[1]] = map[info[0]][info[1]];
            map[info[0]][info[1]] = 0;
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean onBud(ReadOnlyCritter parent, ReadOnlyCritter baby) {
        int[] info = findCritter(parent);
        int[] loc = sense(info[0], info[1], (info[2] + 3) % 6, 1);
        try {
            if (map[loc[0]][loc[1]] != 0) return false;
            addCritter(loc[0], loc[1], baby, info[2]);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean canAttack(ReadOnlyCritter c) {
        int[] info = findCritter(c);
        int[] loc = sense(info[0], info[1], info[2], 1);
        try {
            return map[loc[0]][loc[1]] > 0;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public ReadOnlyCritter getCritterAhead(ReadOnlyCritter c) {
        int[] info = findCritter(c);
        int[] loc = sense(info[0], info[1], info[2], 1);
        return critters.get(map[loc[0]][loc[1]] - 1);
    }

    @Override
    public boolean onDeath(ReadOnlyCritter c, int energy) {
        int[] info = findCritter(c);
        map[info[0]][info[1]] = - energy - 1; // add energy
        return true;
    }

    @Override
    public boolean wantToMate(ReadOnlyCritter c) {
        puberty.add(c);
        int[] info = findCritter(c);
        int[] loc = sense(info[0], info[1], info[2], 1);
        try {
            int index = map[loc[0]][loc[1]] - 1;
            if (puberty.contains(critters.get(index))
                    && (directions.get(index) + 3) % 6 == directions.get(critters.indexOf(c))) return true;
            return false;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean onMate(ReadOnlyCritter parent1, ReadOnlyCritter parent2, ReadOnlyCritter baby) {
        ReadOnlyCritter parent = Math.random() >= 0.5 ? parent1 : parent2;
        if (onBud(parent, baby)) return true;
        parent = parent.equals(parent1) ? parent2 : parent1;
        if (onBud(parent, baby)) return true;
        return false;
    }

}
