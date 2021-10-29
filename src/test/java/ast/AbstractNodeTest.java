package ast;

import cms.util.maybe.NoMaybeValue;
import exceptions.SyntaxError;
import org.junit.jupiter.api.Test;
import parse.Parser;
import parse.ParserFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

class AbstractNodeTest {

    @Test
    public void test_nodeAt() throws SyntaxError {
        String s = "1 + 3 = 0 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        assert (p.size() == 9);
        assert (p.nodeAt(0).getClass().getSimpleName().equals("ProgramImpl"));
        assert (p.nodeAt(1).getClass().getSimpleName().equals("Rule"));
        assert (p.nodeAt(2).getClass().getSimpleName().equals("Relation"));
        assert (p.nodeAt(3).getClass().getSimpleName().equals("BinaryExpr"));
        assert (p.nodeAt(4).getClass().getSimpleName().equals("Factor"));
        assert (p.nodeAt(5).getClass().getSimpleName().equals("Factor"));
        assert (p.nodeAt(6).getClass().getSimpleName().equals("Factor"));
        assert (p.nodeAt(7).getClass().getSimpleName().equals("Command"));
        assert (p.nodeAt(8).getClass().getSimpleName().equals("Action"));

        try {
            p.nodeAt(9);
            assert false;
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Test
    void test_size() throws SyntaxError {
        String s = "1 + 3 = 0 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        assert(p.nodeAt(0).size()==9);
        assert(p.nodeAt(1).size()==8);
        assert(p.nodeAt(2).size()==5);
        assert(p.nodeAt(3).size()==3);
        assert(p.nodeAt(4).size()==1);
        assert(p.nodeAt(5).size()==1);
        assert(p.nodeAt(6).size()==1);
        assert(p.nodeAt(7).size()==2);
        assert(p.nodeAt(8).size()==1);
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
        String s = "1 + 3 = 0 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        assert(((AbstractNode)p.nodeAt(4)).getParent().get() instanceof BinaryExpr);
    }

    @Test
    void test_replace() throws SyntaxError{
        String s = "1 + 3 = 0 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Update u = new Update(new Factor(5),new Factor(3));
        assert(((AbstractNode)p.nodeAt(8)).replace(u));
        String n = "1 + 3 = 0 --> mem[5] := 3;\n";
        assert(p.toString().equals(n));
    }

}