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
        Program p = parser.parse(r);
        System.out.println(p);
    }

    @Test
    void testBinaryExprPrint() throws SyntaxError{
        String s = "-(5+0) = 7 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        System.out.println(p);

        /*for(int i = 0 ; i < p.size(); i++){
            System.out.println(p.nodeAt(i).getClass().getSimpleName()+": "+p.nodeAt(i));
        }*/
    }

}