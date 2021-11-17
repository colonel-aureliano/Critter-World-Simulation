package controller;

import cms.util.maybe.NoMaybeValue;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ControllerImplTest {

    @Test
    void testLoad() {
        ControllerImpl c = new ControllerImpl();
        assert (c.loadWorld("src/test/resources/A5files/test_world.txt", false, false));
        assert (c.loadWorld("src/test/resources/A5files/small_world.txt", false, false));
        assert (c.loadWorld("src/test/resources/A5files/empty_world.txt", false, false));
        assert (c.loadCritters("src/test/resources/A5files/eat-and-bud-critter.txt", 3));
    }

    @Test
    void testExampleWorld() {
        ControllerImpl c = new ControllerImpl();
        assert (c.loadWorld("src/test/resources/A5files/test_world.txt", false, true));
        assert (c.getReadOnlyWorld().getNumberOfAliveCritters() == 3);
        PrintStream o = new PrintStream(System.out);
        c.printWorld(o);
        System.out.println();
        for (int i = 0; i < 10; i++) {
            c.advanceTime(1);
            c.printWorld(o);
            System.out.println();
            //System.out.println(c.getReadOnlyWorld().getNumberOfAliveCritters());
        }
    }

    @Test
    void testNewWorld() {
        Controller c = new ControllerImpl();
        c.newWorld();
        c.printWorld(System.out);
    }

}