package ast;

import cms.util.maybe.NoMaybeValue;
import exceptions.SyntaxError;
import org.junit.jupiter.api.Test;
import parse.Parser;
import parse.ParserFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class MutationImplTest_Yanny {
    @Test
    void test_canApply() throws SyntaxError {
        String s = "1 = 0 --> forward;\n1 = 0 --> forward;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(1);
        assert(!m.canApply(p.nodeAt(0)));
        assert(m.canApply(p.nodeAt(1))); // 1 = 0 --> forward;\n
        assert(!m.canApply(p.nodeAt(2))); // 1 = 0
    }

    @Test
    void test_mutation1() throws SyntaxError, NoMaybeValue {
        String s = "mem[4] > mem[3] * 400 --> grow;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        // p.nodeAt(4) is a BinaryExpr, expressing "SIZE * 400".
        Mutation m = new MutationImpl(1);

        Node n = m.apply(p,p.nodeAt(4)).get();
        System.out.println(n);

    }

    @Test
    void test_mutation3() throws SyntaxError, NoMaybeValue{
        String s = "nearby[3] = 0 and ENERGY > 2500 --> bud;\nnearby[0] > 0 and nearby[8] = 0 --> backward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        // p.nodeAt(3) is a Relation, expressing "nearby[3] = 0".

        Mutation m = new MutationImpl(3);

        Node n = m.apply(p,p.nodeAt(3)).get();
        System.out.println(n);
    }

    @Test
    void test_mutation4_1() throws SyntaxError, NoMaybeValue{
        String s = "nearby[3] = 0 and ENERGY > 2500 --> bud;\nnearby[0] > 0 and nearby[3] = 0 --> backward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        // p.nodeAt(23) is an Action, representing "backward"

        Mutation m = new MutationImpl(4);

        Node n = m.apply(p,p.nodeAt(23)).get(); // mutating "backward"
        System.out.println(n);
    }

    @Test
    void test_mutation4_2() throws SyntaxError, NoMaybeValue {
        String s = "1+3 = 0 and ENERGY > 2500 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        // p.nodeAt(4) is a BinaryExpr, expressing "1 + 3"

        Mutation m = new MutationImpl(4);

        Node n = m.apply(p,p.nodeAt(4)).get(); // mutating "+"
        System.out.println(n);
    }

    @Test
    void test_mutation4_3() throws SyntaxError, NoMaybeValue {
        String s = "1+3 = 0 and ENERGY > 2500 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        // p.nodeAt(2)) is a BinaryCondition, representing "1 + 3 = 0 and mem[4] > 2500"
        Mutation m = new MutationImpl(4);

        Node n = m.apply(p,p.nodeAt(2)).get(); // mutating "and"
        assert(n.toString().equals("1 + 3 = 0 or mem[4] > 2500 --> bud;\n"));
    }

    @Test
    void test_mutation4_4() throws SyntaxError, NoMaybeValue {
        String s = "1+3 = 0 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        // p.nodeAt(2) is a Relation, representing "1 + 3 = 0"

        Mutation m = new MutationImpl(4);

        Node n = m.apply(p,p.nodeAt(2)).get(); // mutating "="
        System.out.println(n);
    }

    @Test
    void test_mutation4_5() throws SyntaxError, NoMaybeValue {
        String s = "1+3 = 0 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        // p.nodeAt(4) is a Factor, representing "1"

        Mutation m = new MutationImpl(4);

        Node n = m.apply(p,p.nodeAt(4)).get(); // mutating "1"
        System.out.println(n);
    }

    @Test
    void test_mutation5_1() throws SyntaxError, NoMaybeValue {
        String s = "1+3 = 0 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(5);

        Node n = m.apply(p,p.nodeAt(4)).get(); // mutating "1", inserting a random Node at this Node
        System.out.println(n);
    }

    @Test
    void test_mutation5_2() throws SyntaxError, NoMaybeValue {
        String s = "1+3 = 0 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(5);

        Node n = m.apply(p,p.nodeAt(2)).get(); // mutating "1+3 = 0", inserting a random Node at this Node
        System.out.println(n);
    }

    @Test
    void test_mutation6_1() throws SyntaxError, NoMaybeValue {
        String s = "nearby[3] = 0 and ENERGY > 2500 --> bud;\nnearby[0] > 0 and nearby[3] = 0 --> backward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(6);

        Node n = m.apply(p,p.nodeAt(0)).get(); // mutating ProgramImpl, inserting a random rule from AST
        System.out.println(n);
    }

    @Test
    void test_mutation6_2() throws SyntaxError, NoMaybeValue {
        String s = "POSTURE != 17 --> POSTURE := 17;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(6);

        Node n = m.apply(p,p.nodeAt(5)).get(); // mutating Command "POSTURE := 17", inserting a random Update from AST
        assert(n.toString().equals("mem[6] != 17 --> mem[6] := 17\n\tmem[6] := 17;\n"));
    }
}