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

    World dummy(){
        return new World(15,10,"dummy");
    }

    @Test
    void testConstructorDefault() throws SyntaxError {
        String name = "forest critter";
        int[] arr = {5,0,-10,0,0,5,89};
        String s = "1 = 1 --> mem[3] := 3;\n mem[3] = 3 --> bud;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Critter c = new Critter(name,arr,p,dummy());
        int[] arr2 = {7,1,1,1,250,1,89};
        assert(Arrays.equals(c.getMemory(), arr2));
        int[] arr3 = {7,10,10,100,250,1,89};
        c = new Critter(name,arr3,p,dummy());
        assert(Arrays.equals(c.getMemory(), arr3));
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

        Critter c = new Critter(name,arr,p,dummy());
        c.getMemory()[0]=10;
        assert(Arrays.equals(c.getMemory(),arr));
    }

    @Test
    void testStep() throws SyntaxError, NoMaybeValue {
        String name = "forest critter";
        int[] arr = {5,0,0,3,0,5,89};
        String s = "mem[6] != 50 --> mem[6] := 50; mem[3] = 3 --> wait;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Critter c = new Critter(name,arr,p,dummy());
        assert(c.getLastRuleString().equals(Maybe.none()));
        int[] arr2 = {7,1,1,3,250,1,89};
        assert(Arrays.equals(c.getMemory(), arr2));

        c.step(); // turn 1: mem[6] set to 50; turn 2: performs wait
        int[] arr3 = {7,1,1,3,253,2,50};
        System.out.println(Arrays.toString(c.getMemory()));
        assert(Arrays.equals(c.getMemory(), arr3));
        assert(c.getLastRuleString().get().equals("mem[3] = 3 --> wait;\n"));
    }

    @Test
    void testStep999() throws SyntaxError, NoMaybeValue {
        String name = "forest critter";
        int[] arr = {7,1,1,1,1,1,13};
        String s = "mem[6] != 7 --> mem[6] := 7;\n mem[0] = 7 --> mem[2] := 10;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Critter c = new Critter(name,arr,p,dummy());
        assert(Arrays.equals(c.getMemory(), arr));
        c.step();
        // mem[6] is set to 7, wait is performed at the end
        int[] arr2 = {7,1,1,1,2,999,7};
        assert(Arrays.equals(c.getMemory(), arr2));
        assert(c.getLastRuleString().get().equals("mem[6] != 7 --> mem[6] := 7;\n"));
    }

    @Test
    void testAction_WAIT() throws SyntaxError, NoMaybeValue {
        String name = "active critter";
        int[] arr = {7,2,2,1,250,1,67};
        // size = 1, energy = 250
        String s = "mem[0] = 7 --> wait;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Critter c = new Critter(name,arr,p,dummy());
        c.step(); // performs wait, energy (mem[4]) set to 251
        int[] arr2 = {7,2,2,1,251,1,67};
        assert(Arrays.equals(c.getMemory(), arr2));
        assert(c.getLastRuleString().get().equals("mem[0] = 7 --> wait;\n"));
    }

    @Test
    void testAction_GROW() throws SyntaxError, NoMaybeValue {
        String name = "active critter";
        int[] arr = {7,2,2,1,250,1,67};
        // size = 1, energy = 250
        String s = "mem[4] > mem[3]*10 --> grow;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Critter c = new Critter(name,arr,p,dummy());
        c.step(); // performs grow, size (mem[3]) set to 2, energy decreases
        // complexity of critter = 102
        int[] arr2 = {7,2,2,2,148,1,67};
        assert(Arrays.equals(c.getMemory(), arr2));
        assert(c.getLastRuleString().get().equals("mem[4] > mem[3] * 10 --> grow;\n"));
    }

}