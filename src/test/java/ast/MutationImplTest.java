package ast;

import cms.util.maybe.NoMaybeValue;
import exceptions.SyntaxError;
import org.junit.jupiter.api.Test;
import parse.Parser;
import parse.ParserFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class MutationImplTest {
    @Test
    void test_canApply() throws SyntaxError {
        String s = "1 = 0 --> forward;\n1 = 0 --> forward;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(1);
        assert(!m.canApply(p.nodeAt(0)));
        assert(m.canApply(p.nodeAt(1)));
        assert(m.canApply(p.nodeAt(2)));
    }

    @Test
    void test_mutation1() throws SyntaxError, NoMaybeValue {
        String s = "nearby[3] = 0 and ENERGY > 2500 --> bud;";

        InputStream in = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        Program p = parser.parse(r);

        Mutation m = new MutationImpl(1);
        int index = 0;
        while (index < p.size()) {
            // System.out.println(p.nodeAt(index));
            try {
                Program c = (Program) p.clone();
                Node n = m.apply(c, c.nodeAt(index)).get();
                System.out.println(n);
            } catch (Exception e) {
                System.out.println("Node at " + index + " cannot be removed.");
            }
            index++;
        }
    }

}