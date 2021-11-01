package model;

import easyIO.EOF;
import easyIO.Scanner;
import easyIO.UnexpectedInput;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CritterTest {

    @Test
    void testConstructor(){
        int[] m = {1,2,3,4,5,6};
        Critter Bob = new Critter("Bob",m);
    }

    @Test
    void t() throws UnexpectedInput, EOF {
        //InputStream in = ClassLoader.getSystemResourceAsStream("A5files/space_critter.txt");
        String str = "species: space critter\nmemsize: 7";
        InputStream in = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Scanner s = new Scanner(r,"");

        System.out.println(s.nextIdentifier());
        s.consume(": ");
        System.out.println(s.peek());
    }

}