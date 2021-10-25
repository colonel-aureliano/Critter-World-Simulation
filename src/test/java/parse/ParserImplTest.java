package parse;

import ast.Program;
import exceptions.SyntaxError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


class ParserImplTest {

    @Test
    void testSimple() throws SyntaxError {

        System.out.println("-----------------------");

        InputStream in = ClassLoader.getSystemResourceAsStream("files/simple_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
    }

    @Test
    void testExample() throws SyntaxError {

        System.out.println("-----------------------");
        
        InputStream in = ClassLoader.getSystemResourceAsStream("files/example-rules.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        parser.parse(r);
    }

    @Test
    void testDrawCritter() throws SyntaxError {

        System.out.println("-----------------------");

        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        parser.parse(r);
    }

    @Disabled
    @Test
    void testCondition() throws SyntaxError {
        InputStream in = ClassLoader.getSystemResourceAsStream("files/simple_critter.txt");
        Tokenizer t = new Tokenizer(new BufferedReader(new InputStreamReader(in)));
        ParserImpl.parseCondition(t);
    }
}