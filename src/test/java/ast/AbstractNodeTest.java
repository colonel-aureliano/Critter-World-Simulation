package ast;

import cms.util.maybe.NoMaybeValue;
import exceptions.SyntaxError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import parse.Parser;
import parse.ParserFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class AbstractNodeTest {

    // Please add "-ea" to VM options field.

    @Test
    void test_nodeAt() throws SyntaxError {

        String s = "1 + 2 > 0 --> forward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        StringBuilder sb = new StringBuilder();
        //assert(p.prettyPrint(sb).toString().equals("1 = 0 --> forward;\n"));

        for (int i = 0; i < p.size(); i++) {
            System.out.println(p.nodeAt(i).getCategory());
            assert(p.nodeAt(i).classInv());
        }
        try {
            p.nodeAt(p.size());
        } catch (IndexOutOfBoundsException e) {
            assert(true);
        }


    }

    @Test
    void test_nodeAt_2() throws SyntaxError{

        String s = "POSTURE != 17 --> mem[6] := 51/3;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        assert(p.size()==11);
        assert(p.nodeAt(1) instanceof Rule);
        assert(p.nodeAt(3) instanceof Mem);
        assert(p.nodeAt(6) instanceof Update);
        assert(p.nodeAt(8) instanceof BinaryExpr);
        assert(p.nodeAt(8).toString().equals("51 / 3"));
        assert(p.nodeAt(10) instanceof Factor);

    }

    @Test
    void test_clone() throws SyntaxError {
        String s = "POSTURE != 17 --> mem[6] := 51/3;\n1 = 0 --> forward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        String original = p.toString();

        Program clone = (Program) p.clone();
        String cloned = clone.toString();
        assert(cloned.equals(original));
    }

    @Test
    void test_getChildren() throws SyntaxError {
        String s = "1 = 0 --> forward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        assert(p.getChildren().size()==1);
        assert(p.getChildren().get(0) instanceof Rule);
        assert(p.getChildren().get(0).getChildren().size() == 2);
        assert(p.getChildren().get(0).getChildren().get(0) instanceof Relation);
        assert(p.getChildren().get(0).getChildren().get(1) instanceof Command);
    }

    @Test
    void test_getParent() throws SyntaxError, NoMaybeValue {
        String s = "POSTURE != 17 --> mem[6] := 51/3;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        AbstractNode n = (AbstractNode) p.nodeAt(10);
        assert(n.getParent().get() instanceof BinaryExpr);
    }

    @Test
    void test_setParent1() throws SyntaxError {
        String s = "1 = 0 --> forward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Node n = p.getChildren().get(0).getChildren().get(0).getChildren().get(1);
        // n is holding onto a Factor Node
        assert(n instanceof Factor);
        assert(n.toString().equals("0"));

        Mem m = new Mem(new Factor(5));
        assert(((Factor) n).setParent(m));
        assert(p.toString().equals("1 = mem[0] --> forward;\n"));
    }

    @Test
    void test_setParent2() throws SyntaxError {
        String s = "1 = 0 --> forward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Node n = p.getChildren().get(0).getChildren().get(0);
        // n is holding onto a Relation Node
        assert(n instanceof Relation);
        assert(n.toString().equals("1 = 0"));

        BinaryCondition bc = new BinaryCondition(null,BinaryCondition.Operator.AND,null);
        assert(((Relation) n).setParent(bc));
        assert(p.toString().equals("1 = 0 and 1 = 0 --> forward;\n"));
    }

    @Test
    void testInvariant() throws SyntaxError {
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        for (int i = 0; i < p.size(); i++) {
            assert(p.nodeAt(i).classInv());
        }
        System.out.println(p);
    }

}