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
    void test_canApply1() throws SyntaxError {
        String s = "1 = 0 --> forward;\n1 = 0 --> forward;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getRemove();;
        assert(!m.canApply(p.nodeAt(0)));
        assert(m.canApply(p.nodeAt(1)));
        assert(!m.canApply(p.nodeAt(2)));
    }

    @Test
    void test_mutation1() throws SyntaxError, NoMaybeValue {
        String s = "nearby[3] = 0 and ENERGY > 2500 --> mem[-4+4] := 2500 bud;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getRemove();;
        int index = 0;
        while (index < p.size()) {
            Program c = (Program) p.clone();
            if(m.canApply(c.nodeAt(index)))System.out.println("Mutating "+c.nodeAt(index).getClass().getSimpleName()+":");
            Maybe<Program> maybe = m.apply(c, c.nodeAt(index));
            if (maybe.isPresent()) System.out.print(maybe.get());
            else System.out.println("Mutation at index " + index + " is illegal.");
            index++;
        }
    }

    @Test
    void test_mutation1_2() throws SyntaxError, NoMaybeValue {
        String s = "mem[4] > mem[3] * 400 --> grow;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getRemove();;
        Node n = m.apply(p,p.nodeAt(5)).get(); // p.nodeAt(5) is a BinaryExpr, expressing "SIZE * 400".
        System.out.println(n);

    }

    @Test
    void test_mutation2() throws SyntaxError, NoMaybeValue {
        String s = "(ahead[1] / 10 mod 100) != 17 and ahead[1] > 0 --> attack;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getSwap();;
        int index = 0;
        while (index < p.size()) {
            Program c = (Program) p.clone();
            if(m.canApply(c.nodeAt(index)))System.out.println("Mutating "+c.nodeAt(index).getClass().getSimpleName()+":");
            Maybe<Program> maybe = m.apply(c, c.nodeAt(index));
            if (maybe.isPresent()) System.out.print(maybe.get());
            else System.out.println("Mutation at index " + index + " is illegal.");
            index++;
        }
    }

    @Test
    void test_mutation3() throws SyntaxError, NoMaybeValue {
        String s = "1 = 1 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getReplace();;
        int index = 0;
        while (index < p.size()) {
            Program c = (Program) p.clone();
            if(m.canApply(c.nodeAt(index)))System.out.println("Mutating "+c.nodeAt(index).getClass().getSimpleName()+":");
            Maybe<Program> maybe = m.apply(c, c.nodeAt(index));
            if (maybe.isPresent()) System.out.print(maybe.get());
            else System.out.println("Mutation at index " + index + " is illegal.");
            index++;
        }
    }

    @Test
    void test_mutation3_2() throws SyntaxError, NoMaybeValue{
        String s = "nearby[3] = 0 and ENERGY > 2500 --> bud;\nnearby[0] > 0 and nearby[8] = 0 --> backward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        /*Mutation m = MutationFactory.getReplace();;
        Node n = m.apply(p,p.nodeAt(3)).get(); // p.nodeAt(3) is a Relation, expressing "nearby[3] = 0"
        System.out.println(n);*/
        Mutation m = MutationFactory.getReplace();;
        int index = 0;
        while (index < p.size()) {
            Program c = (Program) p.clone();
            if(m.canApply(c.nodeAt(index)))System.out.println("Mutating "+c.nodeAt(index).getClass().getSimpleName()+":");
            Maybe<Program> maybe = m.apply(c, c.nodeAt(index));
            if (maybe.isPresent()) System.out.print(maybe.get());
            else System.out.println("Mutation at index " + index + " is illegal.");
            index++;
        }
    }

    @Test
    void test_mutation3_canApply() throws SyntaxError{
        String s = "nearby[3] = 0 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getReplace();;
        assert(!m.canApply(p.nodeAt(6))); // p.nodeAt(6) is a Command, expressing "bud".
        assert(!m.canApply(p.nodeAt(7))); // p.nodeAt(7) is an Action, expressing "bud".
        assert(!m.canApply(p.nodeAt(2)));
    }

    @Test
    void test_mutation4() throws SyntaxError, NoMaybeValue {
        String s = "(ahead[1] / 10 mod 100) != 17 and ahead[1] > 0 --> attack;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getTransform();;
        int index = 0;
        while (index < p.size()) {
            Program c = (Program) p.clone();
            if(m.canApply(c.nodeAt(index)))System.out.println("Mutating "+c.nodeAt(index).getClass().getSimpleName()+":");
            Maybe<Program> maybe = m.apply(c, c.nodeAt(index));
            if (maybe.isPresent()) System.out.print(maybe.get());
            else System.out.println("Mutation at index " + index + " is illegal.");
            index++;
        }
    }

    @Test
    void test_mutation4_2() throws SyntaxError, NoMaybeValue{
        String s = "nearby[3] = 0 and ENERGY > 2500 --> bud;\nnearby[0] > 0 and nearby[3] = 0 --> backward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getTransform();;
        Node n = m.apply(p,p.nodeAt(24)).get(); // p.nodeAt(24) is an Action, representing "backward"
        System.out.println(n);
    }

    @Test
    void test_mutation4_3() throws SyntaxError, NoMaybeValue {
        String s = "1+3 = 0 and ENERGY > 2500 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getTransform();;
        // p.nodeAt(4) is a BinaryExpr, expressing "1 + 3"
        Node n = m.apply(p,p.nodeAt(4)).get(); // mutating "+"
        System.out.println(n);
    }

    @Test
    void test_mutation4_4() throws SyntaxError, NoMaybeValue {
        String s = "1+3 = 0 and ENERGY > 2500 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        // p.nodeAt(2)) is a BinaryCondition, representing "1 + 3 = 0 and mem[4] > 2500"
        Mutation m = MutationFactory.getTransform();;
        m.apply(p,p.nodeAt(2)).get(); // mutating "and"
        assert(p.toString().equals("1 + 3 = 0 or mem[4] > 2500 --> bud;\n"));
    }

    @Test
    void test_mutation4_5() throws SyntaxError, NoMaybeValue {
        String s = "1+3 = 0 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getTransform();;
        m.apply(p,p.nodeAt(4)).get(); // p.nodeAt(4) is a Factor, representing "1"
        System.out.println(p);
    }

    @Test
    void test_mutation5() throws SyntaxError, NoMaybeValue {
        String s = "POSTURE != 17 --> POSTURE := 17;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getInsert();;
        int index = 0;
        while (index < p.size()) {
            Program c = (Program) p.clone();
            if(m.canApply(c.nodeAt(index)))System.out.println("Mutating "+c.nodeAt(index).getClass().getSimpleName()+":");
            Maybe<Program> maybe = m.apply(c, c.nodeAt(index));
            if (maybe.isPresent()) System.out.print(maybe.get());
            else System.out.println("Mutation at index " + index + " is illegal.");
            index++;
        }
    }

    @Test
    void test_mutation5_2() throws SyntaxError, NoMaybeValue {
        String s = "1+3 = 0 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getInsert();;

        assert(!m.canApply(p.nodeAt(0))); // ProgramImpl
        assert(!m.canApply(p.nodeAt(1))); // Rule
        assert(!m.canApply(p.nodeAt(2))); // Relation, there is only one Relation object in the AST
        assert(!m.canApply(p.nodeAt(8))); // Action "bud"
        assert(!m.canApply(p.nodeAt(7))); // Command "bud"

        m.apply(p,p.nodeAt(3)).get(); // mutating "1+3", inserting a random Node at this Node
        System.out.println(p);
    }

    @Test
    void test_mutation5_3() throws SyntaxError, NoMaybeValue {
        String s = "1+3 = 0 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getInsert();;

        m.apply(p,p.nodeAt(4)).get(); // mutating "1", inserting a random Node at this Node
        System.out.println(p);
    }

    @Test
    void test_mutation6() throws SyntaxError, NoMaybeValue {
        String s = "mem[5] = 1 --> mem[6] := mem[4] mem[5] := 2 ENERGY := 2500 bud;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getDuplicate();;
        int index = 0;
        while (index < p.size()) {
            Program c = (Program) p.clone();
            Maybe<Program> maybe = m.apply(c, c.nodeAt(index));
            if(m.canApply(c.nodeAt(index)))System.out.println("Mutating "+c.nodeAt(index).getClass().getSimpleName()+":");
            if (maybe.isPresent()) System.out.print(maybe.get());
            else System.out.println("Mutation at index " + index + " is illegal.");
            index++;
        }
    }

    @Test
    void test_mutation6_2() throws SyntaxError, NoMaybeValue {
        String s = "nearby[3] = 0 and ENERGY > 2500 --> bud;\nnearby[0] > 0 and nearby[3] = 0 --> backward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getDuplicate();;

        m.apply(p,p.nodeAt(0)).get(); // mutating ProgramImpl, inserting a random rule from AST
        System.out.println(p);
    }

    @Test
    void test_mutation6_3() throws SyntaxError, NoMaybeValue {
        String s = "POSTURE != 17 --> POSTURE := 17;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getDuplicate();;

        m.apply(p,p.nodeAt(6)).get(); // mutating Command "POSTURE := 17", inserting a random Update from AST
        assert(p.toString().equals("mem[6] != 17 --> mem[6] := 17\n\tmem[6] := 17;\n"));
    }

    @Test
    public void testNegativeMem() throws SyntaxError, NoMaybeValue {
        String s = "mem[-3-2] > 2500 --> POSTURE := -17;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getReplace();; // may yield mem[-17]
        m.apply(p,p.nodeAt(4)).get();
        System.out.println(p);
    }

    @Test
    public void testEqual(){
        Mutation m1 = MutationFactory.getReplace();;
        Mutation m2 = MutationFactory.getReplace();;
        assert(m1.equals(m2));
        Mutation m3 = MutationFactory.getSwap();;
        assert(!m1.equals(m3));
    }

    @Test
    public void testMutate() throws SyntaxError {
        String s = "1 = 2 --> eat;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        p.mutate();
        System.out.println(p);
    }

    @Test
    public void testClone() throws SyntaxError {
        String s = "1 = 2 --> eat;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = MutationFactory.getDuplicate();
        m.apply(p,p.nodeAt(0));
        m = MutationFactory.getReplace();
        m.apply(p,p.nodeAt(p.size()-1));
        assert(p.toString().equals("1 = 2 --> eat;\n1 = 2 --> eat;\n"));
        m = MutationFactory.getInsert();
        m.apply(p,p.nodeAt(2));
        System.out.println(p);
    }
}