package parse;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {
    @Test
    void test(){
        System.out.println("-------------------");
        InputStream in = ClassLoader.getSystemResourceAsStream("files/simple_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));

        Tokenizer t = new Tokenizer(r);
        while (t.peek().getType() != TokenType.EOF) {
            System.out.println(t.peek().getType());
            t.next();
        }
        System.out.println("-------------------");
    }

    @Test
    void testComment() {

    }


}