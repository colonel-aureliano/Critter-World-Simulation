package controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerImplTest {

    @Test
    void testCritterRead(){
        ControllerImpl c = new ControllerImpl();
        c.loadCritters("A5files/space_critter.txt",1);
    }

}