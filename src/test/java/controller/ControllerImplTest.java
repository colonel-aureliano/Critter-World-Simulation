package controller;

import model.Critter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerImplTest {

    @Test
    void testSpaceCritter(){
        ControllerImpl c = new ControllerImpl();
        Critter space = c.readCritter("A5files/space_critter.txt");
        space.step();
    }

}