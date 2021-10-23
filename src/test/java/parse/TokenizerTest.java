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
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));

        Tokenizer t = new Tokenizer(r);
        for (int i = 0; i < 4; i++){
            Token token = t.next();
            System.out.println(token);
            switch(i){
                case 0:
                    System.out.println(token.isMemSugar()); // POSTURE
                    break;
                case 1:
                    System.out.println(token.isRelation()); // !=
                    break;
                case 2:
                    System.out.println(token.isNum()); // 17
                    break;
                case 3:
                    System.out.println(token.getType()); // -->
                    break;
            }
        }
        System.out.println("-------------------");
    }
}