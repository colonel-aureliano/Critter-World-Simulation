package ast;

import exceptions.SyntaxError;
import org.junit.jupiter.api.Test;
import parse.Parser;
import parse.ParserFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ProgramImplTest {

    @Test
    public void testClass() throws SyntaxError {
        String s = "(ahead[1] / 10 mod 100) != 17 and ahead[1] > 0 --> attack;";
        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        for (int i = 1; i < p.size(); i++) {
            System.out.println(p.nodeAt(i).toString() + '\t' + p.nodeAt(i).getClass().getSimpleName());
        }
    }

    @Test
    public void testPrint() throws SyntaxError {
        InputStream in = ClassLoader.getSystemResourceAsStream("files/example-rules.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);
        System.out.println(((AbstractNode)p.nodeAt(3)).getParent().getClass());
    }

    @Test
    public void test() {
        for (int i = 0; i < 30; i ++) {
            System.out.print((int) (Math.random() * 10) + ' ');
        }
    }


}