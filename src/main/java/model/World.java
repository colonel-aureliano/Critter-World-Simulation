package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class World extends ROnlyWorld implements CritterObserver {

    HashSet<ReadOnlyCritter> puberty = new HashSet<>();

    /**
     * Create a world with width w, height h, and name n
     */
    public World(int w, int h, String n) {
        super(w, h, n);
    }

    public boolean step() {
        puberty.clear();
        for (ReadOnlyCritter c: critters) {
            ((Critter) c).step();
        }
        steps++;
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
     * return the coordinate of the hex that is dis unit away from [c,r] in dir direction.
     * Requires: 0 <= dir <= 5, c, r >= 0
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
        }
        return new int[]{column, row};
    }

    @Override
    public int onNearby(ReadOnlyCritter c, int dir) {
        int[] info = findCritter(c);
        int[] loc = sense(info[0], info[1], (dir + info[2]) % 6, 1);
        try {
            int i = map[loc[0]][loc[1]];
            if (i > 0) return critters.get(i - 1).getMemory()[6];
            return i;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Nearby at direction " + dir + " is out of World boundary.");
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
            System.out.println("Ahead at distance" + dist + " is out of World boundary.");
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
        int food = Math.min(n, -(1 + map[loc[0]][loc[1]]));
        map[loc[0]][loc[1]] += food;
        if (map[loc[0]][loc[1]] == -1) map[loc[0]][loc[1]] = 0; // consume all food
        return food;
    }

    @Override
    public boolean onServeFood(ReadOnlyCritter c, int n) {
        int[] info = findCritter(c);
        int[] loc = sense(info[0], info[1], info[2], 1);
        if (map[loc[0]][loc[1]] == -1 |map[loc[0]][loc[1]] > 0) return false;
        if (map[loc[0]][loc[1]] == 0) map[loc[0]][loc[1]] = -1;
        map[loc[0]][loc[1]] -= n;
        return true;
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
        map[info[0]][info[1]] = -energy - 1; // add energy
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
