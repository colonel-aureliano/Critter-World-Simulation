package model;

import ast.Program;
import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import easyIO.EOF;
import easyIO.Scanner;
import easyIO.UnexpectedInput;
import exceptions.SyntaxError;
import org.junit.jupiter.api.Test;
import parse.Parser;
import parse.ParserFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CritterTest {

    @Test
    void testConstructor() throws SyntaxError {
        String name = "forest critter";
        int[] arr = {5,0,0,0,0,5,89};
        String s = "1 = 1 --> mem[3] := 3;\n mem[3] = 3 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Critter c = new Critter(name,arr,p);
        int[] arr2 = {7,1,1,1,1,5,89};
        assert(Arrays.equals(c.getMemory(), arr2));
    }

    @Test
    void testRepExposure() throws SyntaxError {
        String name = "forest critter";
        int[] arr = {7,1,1,1,1,1,13};
        String s = "1 = 1 --> mem[3] := 3;\n mem[3] = 3 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Critter c = new Critter(name,arr,p);
        c.getMemory()[0]=10;
        assert(Arrays.equals(c.getMemory(),arr));
    }

    @Test
    void testStep() throws SyntaxError, NoMaybeValue {
        String name = "forest critter";
        int[] arr = {5,0,0,0,0,5,89};
        String s = "1 = 1 --> mem[3] := 3;\n mem[3] = 3 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Critter c = new Critter(name,arr,p);
        assert(c.getLastRuleString().equals(Maybe.none()));

        c.step();
        int[] arr2 = {7,1,1,3,1,1,89};
        assert(Arrays.equals(c.getMemory(), arr2));

        assert(c.getLastRuleString().get().equals("mem[3] = 3 --> bud;\n"));
    }

    @Test
    void testStep999() throws SyntaxError {
        String name = "forest critter";
        int[] arr = {7,8,1,1,1,1,13};
        String s = "mem[1] = 8 --> mem[1] := 3;\n mem[1] = 3 --> mem[1] := 8;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Critter c = new Critter(name,arr,p);
        System.out.println(Arrays.toString(c.getMemory()));
        c.step();
        System.out.println(Arrays.toString(c.getMemory()));
    }

}