package main;

import ast.Program;
import exceptions.SyntaxError;
import parse.Parser;
import parse.ParserFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class ParseAndMutateApp {

    public static void main(String[] args) {
        int n = 0;
        String file;
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

            Reader r = new BufferedReader(new FileReader(file));
            Parser parser = ParserFactory.getParser();
            Program p = parser.parse(r);

            System.out.println("Program is vaild, printing program:");
            System.out.println(p);

            ((Program) p).mutate();

        } catch (IllegalArgumentException | SyntaxError | FileNotFoundException e) {
            if(!(e instanceof SyntaxError)) System.out.println(e.getMessage());
            else {
                System.out.println("Syntax error.");
                if (args.length == 1) {
                    System.out.println("Usage:\n  " + args[0] + "\n");
                } else {
                    System.out.println("Usage:\n  --mutate " + args[1] + " " + args[2]);
                }
            }
        }
    }
}
