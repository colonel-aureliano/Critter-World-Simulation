package ast;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import exceptions.SyntaxError;
import org.junit.jupiter.api.Test;
import parse.Parser;
import parse.ParserFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ProgramImplTest {

    @Test
    public void testRandomMutate() throws SyntaxError {
        String s = "nearby[3] = 0 and ENERGY > 2500 --> POSTURE := 17;\n" +
                "nearby[0] > 0 and nearby[8] = 0 --> backward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        p.mutate();
    }

    @Test
    public void testFindNodeOfType() throws SyntaxError {
        String s = "nearby[3] = 0 and ENERGY > 2500 --> bud;\nnearby[0] > 0 and nearby[8] = 0 --> backward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        try {
            NodeCategory n = NodeCategory.CONDITION;
            assert (p.findNodeOfType(n).get().toString().equals("nearby[3] = 0 and mem[4] > 2500"));
            n = NodeCategory.ACTION;
            assert (p.findNodeOfType(n).get().toString().equals("bud"));
            n = NodeCategory.UPDATE;
            assert (p.findNodeOfType(n).equals(Maybe.none()));
            n = NodeCategory.RULE;
            assert (p.findNodeOfType(n).get().toString().equals("nearby[3] = 0 and mem[4] > 2500 --> bud;\n"));
        } catch (NoMaybeValue noMaybeValue) {
            noMaybeValue.printStackTrace();
        }
    }

    @Test
    public void testClone() throws SyntaxError {
        String s = "nearby[3] = 0 and ENERGY > 2500 --> bud;\nnearby[0] > 0 and nearby[8] = 0 --> backward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Program newP = (Program) p.clone();

        for (int i = 0; i < newP.size(); i++){
            assert(newP.nodeAt(i)!=p.nodeAt(i));
            assert(newP.nodeAt(i).toString().equals(p.nodeAt(i).toString()));
        }
    }

    @Test
    public void testClassInv() throws SyntaxError {
        String s = "nearby[3] = 0 and ENERGY > 2500 --> bud;\nnearby[0] > 0 and nearby[8] = 0 --> backward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        assert p.classInv();
        p.getChildren().add(new Factor(3));
        assert !p.classInv();
        p.getChildren().remove(p.getChildren().size()-1);
        assert p.classInv();
    }

    @Test
    void testClassInv2() throws SyntaxError {
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        for (int i = 0; i < p.size(); i++) {
            assert(p.nodeAt(i).classInv());
        }
    }

}