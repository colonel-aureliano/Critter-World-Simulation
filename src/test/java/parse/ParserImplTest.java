package parse;

import ast.Expr;
import ast.Program;
import exceptions.SyntaxError;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;

class ParserImplTest {
    @Test
    void test() throws SyntaxError {

        System.out.println("-----------------------");
        
        InputStream in = ClassLoader.getSystemResourceAsStream("files/simple_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program prog = parser.parse(r);
    }

    @Test
    void testExpr() throws SyntaxError {
        InputStream in = ClassLoader.getSystemResourceAsStream("files/simple_critter.txt");
        Tokenizer t = new Tokenizer(new BufferedReader(new InputStreamReader(in)));
        Expr e = ParserImpl.parseExpression(t);

    }
}