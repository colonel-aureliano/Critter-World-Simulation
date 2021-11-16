package controller;

import cms.util.maybe.NoMaybeValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerImplTest {

    @Test
    void testLoad(){
        ControllerImpl c = new ControllerImpl();
        assert(c.loadWorld("src/test/resources/A5files/test_world.txt",false,false));
        assert(c.loadWorld("src/test/resources/A5files/small_world.txt",false,false));
        assert(c.loadWorld("src/test/resources/A5files/empty_world.txt",false,false));
        assert(c.loadCritters("src/test/resources/A5files/eat-and-bud-critter.txt",3));
    }

    @Test
    void testExampleWorld() throws NoMaybeValue {
        ControllerImpl c = new ControllerImpl();
        assert(c.loadWorld("src/test/resources/A5files/empty_world.txt",false,false));
        for(int i = 0; i < 1; i++){
            //c.advanceTime(1);
            //c.printWorld(null);
            //System.out.println(c.getReadOnlyWorld().getReadOnlyCritter(25,9).get().getLastRuleString());
        }
    }

}