package parse;

import ast.Program;
import exceptions.SyntaxError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;


class ParserImplTest {

    @Test
    void testSimple() throws SyntaxError {

        System.out.println("-----------------------");

        InputStream in = ClassLoader.getSystemResourceAsStream("files/simple_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        System.out.println(p);
        System.out.println("-----------------------");
    }

    @Test
    void testExample() throws SyntaxError {

        System.out.println("-----------------------");
        
        InputStream in = ClassLoader.getSystemResourceAsStream("files/example-rules.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        System.out.println(p);
    }

    @Test
    void testDrawCritter() throws SyntaxError {

        System.out.println("-----------------------");

        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        parser.parse(r);
    }

    @Test
    void testBinaryExprPrint() throws SyntaxError{
        String s = "(1+2)*3 - 3 mod (4 * 5) - 1 = 0 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        System.out.println(p);
    }

}