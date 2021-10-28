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
    public void testClass() throws SyntaxError {
        String s = "(ahead[1] / 10 mod 100) != 17 and ahead[1] > 0 --> attack;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        for (int i = 1; i < p.size(); i++) {
            System.out.println(p.nodeAt(i).toString() + '\t' + p.nodeAt(i).getClass().getSimpleName());
        }
    }

    @Test
    public void testPrint() throws SyntaxError {
        InputStream in = ClassLoader.getSystemResourceAsStream("files/example-rules.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        System.out.println(((AbstractNode)p.nodeAt(3)).getParent().getClass());
    }

    @Test
    public void test() {
        for (int i = 0; i < 30; i ++) {
            System.out.print((int) (Math.random() * 10) + ' ');
        }
    }

    public void testMutate() throws SyntaxError{
        String s = "nearby[3] = 0 and ENERGY > 2500 --> bud;\nnearby[0] > 0 and nearby[8] = 0 --> backward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        p=p.mutate();
        System.out.println(p);
    }

    @Test
    public void testFindNodeOfType() throws SyntaxError{
        String s = "nearby[3] = 0 and ENERGY > 2500 --> bud;\nnearby[0] > 0 and nearby[8] = 0 --> backward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        try{
            NodeCategory n = NodeCategory.CONDITION;
            assert(p.findNodeOfType(n).get().toString().equals("nearby[3] = 0 and mem[4] > 2500"));
            n = NodeCategory.ACTION;
            assert(p.findNodeOfType(n).get().toString().equals("bud"));
            n = NodeCategory.UPDATE;
            assert(p.findNodeOfType(n).equals(Maybe.none()));
        } catch (NoMaybeValue noMaybeValue) {
            noMaybeValue.printStackTrace();
        }
    }
}