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

class MutationImplTest {
    @Test
    void test_canApply() throws SyntaxError {
        String s = "1 = 0 --> forward;\n1 = 0 --> forward;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(1);
        assert(!m.canApply(p.nodeAt(0)));
        assert(m.canApply(p.nodeAt(1)));
        assert(m.canApply(p.nodeAt(2)));
    }

    @Test
    void test_mutation1() throws SyntaxError, NoMaybeValue {
        String s = "nearby[3] = 0 and ENERGY > 2500 --> ENERGY := 2500 bud;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(1);
        int index = 0;
        while (index < p.size()) {
            Program c = (Program) p.clone();
            Maybe<Program> maybe = m.apply(c, c.nodeAt(index));
            if (maybe.isPresent()) System.out.print(maybe.get());
            else System.out.println("Mutation at index " + index + " is unsuccessful");
            index++;
        }
    }

    @Test
    void test_mutation2() throws SyntaxError, NoMaybeValue {
        String s = "(ahead[1] / 10 mod 100) != 17 and ahead[1] > 0 --> attack;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(2);
        int index = 0;
        while (index < p.size()) {
            Program c = (Program) p.clone();
            Maybe<Program> maybe = m.apply(c, c.nodeAt(index));
            if (maybe.isPresent()) System.out.print(maybe.get());
            else System.out.println("Mutation at index " + index + " is unsuccessful");
            index++;
        }
    }

    @Test
    void test_mutation3() throws SyntaxError, NoMaybeValue {
        String s = "ahead[0] < -1 and ENERGY < 500 * SIZE --> ENERGY := 500 eat;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(3);
        int index = 0;
        while (index < p.size()) {
            Program c = (Program) p.clone();
            Maybe<Program> maybe = m.apply(c, c.nodeAt(index));
            if (maybe.isPresent()) System.out.print(maybe.get());
            else System.out.println("Mutation at index " + index + " is unsuccessful");
            index++;
        }
    }

    @Test
    void test_mutation4() throws SyntaxError{
        String s = "(ahead[1] / 10 mod 100) != 17 and ahead[1] > 0 --> attack;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(4);
        int index = 0;
        while (index < p.size()) {
            try {
                Program c = (Program) p.clone();
                Node n = m.apply(c, c.nodeAt(index)).get();
                System.out.println(n);
            } catch (Exception e) {
                System.out.println(e.getMessage() + "at index "+ index);
            }
            index++;
        }
    }

    @Test
    void test_mutation5() throws SyntaxError{
        String s = "POSTURE != 17 --> POSTURE := 17;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(5);
        int index = 0;
        while (index < p.size()) {
            try {
                Program c = (Program) p.clone();
                Node n = m.apply(c, c.nodeAt(index)).get();
                System.out.println(n);
            } catch (Exception e) {
                System.out.println(e.getMessage() + " at index "+ index);
            }
            index++;
        }
    }

    @Test
    void test_mutation6() throws SyntaxError{
        String s = "mem[5] = 1 --> mem[6] := mem[4] " +
                "mem[5] := 2" +
                "bud;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(6);
        int index = 0;
        while (index < p.size()) {
            try {
                Program c = (Program) p.clone();
                Node n = m.apply(c, c.nodeAt(index)).get();
                System.out.println(n);
            } catch (Exception e) {
                System.out.println("Node at " + index + " could not be duplicated");
            }
            index++;
        }
    }



}