package controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerImplTest {

    @Test
    void testCritter(){
        ControllerImpl c = new ControllerImpl();
        assert(c.loadCritters("src/test/resources/A5files/eat-and-bud-critter.txt",3));
    }

    @Test
    void testWorld(){
        ControllerImpl c = new ControllerImpl();
        assert(c.loadWorld("src/test/resources/A5files/test_world.txt",false,false));
    }

}