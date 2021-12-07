package model;

import ast.Program;
import cms.util.maybe.NoMaybeValue;
import exceptions.SyntaxError;
import org.junit.jupiter.api.Test;
import parse.Parser;
import parse.ParserFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    public Critter createCritter(String n, CritterObserver w){
        String name = n;
        int[] arr = {7, 1, 1, 1, 250, 1 , 89};
        String s = "1 = 1 --> mem[3] := 3; mem[3] = 3 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = null;
        try {
            p = parser.parse(r);
        } catch (SyntaxError e) {
            e.printStackTrace();
        }
        Critter c = new Critter(name,arr,p,w);
        return c;
    }

    // Figure 1 in A5 spec
    public World Figure1() {
        World w = new World(7, 9, "small");
        Critter c1 = createCritter("Forrest Critter", w);
        Critter c2 = createCritter("Ocean Critter", w);
        w.addCritter(2, 4, c1,1);
        w.addCritter(2, 0, c2,5);
        w.addRock(0,8);
        w.addRock(6,6);
        w.addRock(0,2);
        w.addFood(2,8, 10);
        return w;
    }

    @Test
    public void testPrint() {
        System.out.println(Figure1().print());
    }

    @Test
    public void testActions() {
        World w = Figure1();
        Critter c = createCritter("New Critter1", w);
        assert(w.getNumberOfAliveCritters() == 2);
        w.addCritter( 1, 7, c,5);
        assert(w.getNumberOfAliveCritters() == 3);
        assert(w.getSize() == 32);

        // sensing
        assert(w.onNearby(c, 0) == -1);
        assert(w.onNearby(c, 1) == 0);
        assert(w.onNearby(c, 2) == -11);
        assert(w.onNearby(c, 3) == 0);
        assert(w.onNearby(c, 4) == 0);
        assert(w.onNearby(c, 5) == 0);

        // moving and turning
        w.onMove(c, false);
        w.onTurn(c, true);
        w.onTurn(c, true);
        assert(w.onAhead(c,1) == 89);
        assert(w.onAhead(c,3) == 89);

        // mating
        w.onTurn(w.critters.get(0), true);
        assertFalse(w.wantToMate(w.critters.get(0)));
        assert(w.wantToMate(c));
        Critter baby = createCritter("Baby", w);
        w.onMate(c, w.critters.get(0), baby);
        assert(w.map[2][2] == 4);
        assert(w.directions.get(3) == 0);

        // food
        w.onTurn(c, true);
        w.onTurn(c, true);
        w.onTurn(c, true);
        assert(w.onEatFood(c, 30) == 10);
        assert(w.map[2][8] == 0);
        w.onServeFood(c, 10);
        assert(w.map[2][8] == -11);
        w.onServeFood(c, 20);
        assert(w.map[2][8] == -31);
        assertFalse(w.onMove(c, true));

        // death
        w.onDeath(w.critters.get(0), 10);
        assert(w.map[2][4] == -11);
        assert(w.getNumberOfAliveCritters() == 3);

        System.out.println(w.print());

    }

    @Test
    public void testMana() {
        World w = Figure1();
        w.loadParams(true, true);
        int n = 500;
        for (int i = 0; i < n; i ++) {
            w.step();
        }
        assert(w.getSteps() == n);
        System.out.println(w.print());
    }

    @Test
    void testSmell() throws SyntaxError, NoMaybeValue {
        String name = "Smelling Critter";
        int[] arr = {7,2,2,1,250,1,20};
        // size = 1, energy = 250
        String s = "smell=0 --> eat;\nsmell mod 1000 = 0 --> forward;\n" +
                "smell mod 1000<3 --> right;\nsmell mod 1000>=3 --> left;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        World w = new World(6, 10, "smelling world");
        Critter c = new Critter(name,arr,p,w);
        w.addCritter(0, 0, c, 1);
        w.addFood(5,5,10);
        w.step();
        w.step();
        w.step();
        w.step();
        w.step();
        assertEquals("smell = 0 --> eat;\n",c.getLastRuleString().get());
    }

    @Test
    void testSmell999() throws SyntaxError, NoMaybeValue {
        String name = "Smelling Critter";
        int[] arr = {7,2,2,1,250,1,20};
        // size = 1, energy = 250
        String s = "smell=0 --> eat;\nmem[0]=7 --> mem[0]:=6;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        World w = new World(6, 10, "smelling world");
        Critter c = new Critter(name,arr,p,w);
        w.addCritter(3, 3, c, 0);
        // No food in world.

        w.step();
        assertEquals(c.getMemory()[5],999);
        w.addFood(4,4,100);
        w.step();
        assertEquals(c.getMemory()[5],999);
    }

}