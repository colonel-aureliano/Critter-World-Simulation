package controller;

import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import cms.util.maybe.NoMaybeValue;
import model.Critter;
import model.World;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ControllerImplTest {

    @Test
    void testLoad() {
        ControllerImpl c = new ControllerImpl();
        assert (c.loadWorld("src/test/resources/A5files/test_world.txt", false, false));
        assert (c.loadWorld("src/test/resources/A5files/small_world.txt", false, false));
        assert (c.loadWorld("src/test/resources/A5files/empty_world.txt", false, false));
        assert (c.loadCritters("src/test/resources/A5files/eat_and_bud_critter.txt", 3));
    }

    @Test
    void testExampleWorld() {
        ControllerImpl c = new ControllerImpl();
        assert (c.loadWorld("src/test/resources/A5files/test_world.txt", false, true));
        assert (c.getReadOnlyWorld().getNumberOfAliveCritters() == 3);
        PrintStream o = new PrintStream(System.out);
        c.printWorld(o);
        System.out.println();
        for (int i = 0; i < 50; i++) {
            c.advanceTime(1);
        }
        c.printWorld(o);
        System.out.println();
    }

    @Test
    void testNewWorld() {
        Controller c = new ControllerImpl();
        c.newWorld();
        c.loadCritters("src/test/resources/A5files/eat_and_bud_critter.txt", 3);
        c.advanceTime(10);
        c.printWorld(System.out);
    }

    @Test
    void testSmartSmellImpl(){
        Controller c = new ControllerImpl();
        c.loadWorld("src/test/resources/A5files/smell_test_world.txt", false, false);
        World w = (World) c.getReadOnlyWorld();
        assertEquals(-101,w.getTerrainInfo(5,5));
        // Food exists at (5,5).
        assertEquals(4000,w.smartSmellImpl(new int[] {0,0,1}));
        assertEquals(0,w.smartSmellImpl(new int[] {5,7,3}));
        assertEquals(7005,w.smartSmellImpl(new int[] {0,4,2}));
        assertEquals(5001,w.smartSmellImpl(new int[] {3,7,2}));
    }

}