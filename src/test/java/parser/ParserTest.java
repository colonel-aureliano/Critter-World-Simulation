package parser;

import ast.Program;

import java.io.*;
import java.nio.charset.StandardCharsets;

import exceptions.SyntaxError;
import org.junit.jupiter.api.Test;
import parse.Parser;
import parse.ParserFactory;

/** This class contains tests for the Critter parser. */
public class ParserTest {

    /** Checks that a valid critter program is not {@code null} when parsed. */
    @Test
    public void testProgramIsNotNone() {
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        try{Program prog = parser.parse(r);
            System.out.println(prog);}
        catch(SyntaxError e){
            System.out.println("A valid program should not have syntax errors");
        }
    }

    /** Checks that a valid critter program is not {@code null} when parsed. */
    @Test
    void testExample(){
        InputStream in = ClassLoader.getSystemResourceAsStream("files/example-critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        try{Program prog = parser.parse(r);
            System.out.println(prog);}
        catch(SyntaxError e){
            System.out.println("A valid program should not have syntax errors");
        }
    }
    
    @Test
    void testNegativePrint(){
        String s = "-(5+0) = 7 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        try {
            Program prog = parser.parse(r);
            assert (prog.toString().equals("-(5 + 0) = 7 --> bud;\n"));
        }
        catch(SyntaxError e) {
            System.out.println("A valid program should not have syntax errors");
        }
    }

    @Test
    void testIgnoreComment(){
        String s = "-(5+0) = 7 --> bud; // Hello World!\n  -5 = 7 --> wait;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        try {
            Program prog = parser.parse(r);
            assert (prog.toString().equals("-(5 + 0) = 7 --> bud;\n-5 = 7 --> wait;\n"));
        }
        catch(SyntaxError e) {
            System.out.println("A valid program should not have syntax errors");
        }
    }

    @Test
    void testNegativeMem_Sensor() throws SyntaxError {
        String s = "-(nearby[5]+5) = 7 --> bud;\n-mem[4] = 7 --> wait;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        System.out.println(p);
    }

}
