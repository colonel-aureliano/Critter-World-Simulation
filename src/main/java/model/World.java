package model;

public class World extends ROnlyWorld implements CritterObserver {
    /**
     * Create a world with width w and height h
     *
     * @param w
     * @param h
     */
    public World(int w, int h, String n) {
        super(w, h, n);
    }

    /**
     * Return the location of Critter in form [c, w, direction]
     * Requires: critter exists in World, otherwise return null
     */
    private int[] findCritter(ReadOnlyCritter c) {
        int index = critters.indexOf(c) + 1;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; i < map[0].length; j++) {
                if (map[i][j] == index) return new int[]{i,j, directions.get(index)};
            }
        }
        return null;
    }

    @Override
    public int onEatFood(ReadOnlyCritter c, int n) {
        return 0;
    }

    @Override
    public boolean onServeFood(ReadOnlyCritter c, int n) {
        return false;
    }

    @Override
    public boolean onTurn(ReadOnlyCritter c, boolean left) {
        return false;
    }

    @Override
    public boolean onMove(ReadOnlyCritter c, boolean forward) {
        return false;
    }

    @Override
    public boolean onBud(ReadOnlyCritter parent, ReadOnlyCritter baby) {
        return false;
    }

    @Override
    public boolean canAttack(ReadOnlyCritter c) {
        return false;
    }

    @Override
    public ReadOnlyCritter onAttack(ReadOnlyCritter c) {
        return null;
    }

    @Override
    public boolean onDeath(ReadOnlyCritter c) {
        return false;
    }

    @Override
    public boolean wantToMate(ReadOnlyCritter c) {
        return false;
    }

    @Override
    public ReadOnlyCritter matePartner(ReadOnlyCritter c) {
        return null;
    }

    @Override
    public boolean onMate(ReadOnlyCritter parent, ReadOnlyCritter baby) {
        return false;
    }
}
