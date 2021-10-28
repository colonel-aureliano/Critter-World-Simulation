package main;

import ast.Program;
import ast.ProgramImpl;
import exceptions.SyntaxError;
import parse.Parser;
import parse.ParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ParseAndMutateApp {

    public static void main(String[] args) {
        int n = 0;
        String file = null;
        try {
            if (args.length == 1) {
                file = args[0];
            } else if (args.length == 3 && args[0].equals("--mutate")) {
                n = Integer.parseInt(args[1]);
                if (n < 0 || n > 5) throw new IllegalArgumentException();
                file = args[2];
            } else {
                throw new IllegalArgumentException();
            }

            InputStream in = ClassLoader.getSystemResourceAsStream(file);
            Reader r = new BufferedReader(new InputStreamReader(in));
            Parser parser = ParserFactory.getParser();
            Program p = parser.parse(r);

            System.out.println("Printing program:");
            System.out.println(p);

            if(n!=0){
                n++;
                p = ((ProgramImpl)p).mutateR(n);
                System.out.println("Printing mutation with Rule "+n+" on random Node:");
                System.out.println(p);
            }

        } catch (IllegalArgumentException | SyntaxError e) {
            System.out.println("Usage:\n  <input_file>\n  --mutate <n> <input_file>");
        }
    }
}
