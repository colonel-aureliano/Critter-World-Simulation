package controller;

import model.Critter;
import model.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerImplTest {

    @Test
    void testSpaceCritter(){
        ControllerImpl c = new ControllerImpl();
        Critter space = c.readCritter("A5files/space_critter.txt");
        space.step();
    }

    @Test
    void testLoadWorld() {
        ControllerImpl c = new ControllerImpl();
        World space = c.readWorld("A5files/small_world.txt");
    }

}