package ast;

import exceptions.SyntaxError;
import org.junit.jupiter.api.Test;
import parse.Parser;
import parse.ParserFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class AbstractNodeTest {

    @Test
    void test_nodeAt() throws SyntaxError {
        System.out.println("-----------------------");

        // Please add "-ea" to VM options field.

        String s = "1 = 0 --> forward;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        StringBuilder sb = new StringBuilder();
        System.out.println(p.prettyPrint(sb).toString());

        assert(p.size() == 7);
        assert(p.nodeAt(0) instanceof ProgramImpl);
        assert(p.nodeAt(1) instanceof Rule);
        assert(p.nodeAt(2) instanceof Relation);
        assert(p.nodeAt(3) instanceof Factor);
        assert(p.nodeAt(4) instanceof Factor);
        assert(p.nodeAt(5) instanceof Command);
        assert(p.nodeAt(6) instanceof Action);

        System.out.println("-----------------------");

    }

    @Test
    void test_nodeAt_2() throws SyntaxError{
        System.out.println("-----------------------");

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

        System.out.println("-----------------------");
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


}