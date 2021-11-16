package controller;

import ast.Program;
import easyIO.EOF;
import easyIO.Scanner;
import easyIO.UnexpectedInput;
import exceptions.SyntaxError;
import model.Critter;
import model.ReadOnlyWorld;
import model.World;
import parse.Parser;
import parse.ParserFactory;

import java.io.*;
import java.util.Arrays;

public class ControllerImpl implements Controller {

    @Override
    public ReadOnlyWorld getReadOnlyWorld() {
        return null;
    }

    @Override
    public void newWorld() {

    }

    @Override
    public boolean loadWorld(String filename, boolean enableManna, boolean enableForcedMutation) {
        return false;
    }

    // load World file
    protected boolean readWorld(String filename) {
        InputStream in = ClassLoader.getSystemResourceAsStream(filename);
        Reader r = new BufferedReader(new InputStreamReader(in));
        easyIO.Scanner s = new Scanner(r, "");

        try {
            s.consume("name");
            s.trailingWhitespace();
            String name = s.nextLine();
            s.consume("size");
            s.trailingWhitespace();
            int width = s.nextInt();
            s.trailingWhitespace();
            int height = s.nextInt();
            ReadOnlyWorld world = new World(width, height, name);
        } catch (UnexpectedInput e) {
            System.out.println("Unexpected world format");
        }

        return true;

    }

    @Override
    public boolean loadCritters(String filename, int n) {
        Critter c = readCritter(filename);
        if (c == null) return false;
        //TODO load n number of critters into world
        return true;
    }

    // readCritter is declared protected for testing purposes only
    protected Critter readCritter(String filename) {
        InputStream in = ClassLoader.getSystemResourceAsStream(filename);
        Reader r = new BufferedReader(new InputStreamReader(in));
        easyIO.Scanner s = new Scanner(r, "");

        String name = null;
        int[] arr = new int[7];
        int i = 0;

        while (s.hasNext()) {
            try {
                s.newline();
            } catch (UnexpectedInput unexpectedInput) {
            }
            if (i >= arr.length) break;
            try {
                s.trailingWhitespace();
                s.consume("//");
                s.nextLine();
                continue;
            } catch (UnexpectedInput unexpectedInput) {
            }

            // Reading species name.
            if (name == null) {
                try {
                    if (!s.nextIdentifier().equals("species")) {
                        // First command word in critter file must be "species".
                        throw new UnexpectedInput();
                    }
                    s.consume(":");
                    s.trailingWhitespace();
                    name = s.nextLine();
                    if (name.contains("//")) {
                        int t = name.indexOf("//");
                        name = name.substring(0, t);
                    }
                    name = name.trim();
                    continue;
                } catch (UnexpectedInput e) {
                    System.out.println("Illegal critter file: invalid species name.");
                    return null;
                }
            }

            // Reading critter mem values.
            if (i < arr.length) {
                try {
                    if (i == 5) {
                        arr[i] = 1;
                        i++;
                        continue;
                    }
                    arr[i] = readCritterHelper(s);
                    i++;
                } catch (UnexpectedInput e) {
                    System.out.println("Illegal critter file: invalid mem values.");
                    return null;
                }
            }
        }

        Parser p = ParserFactory.getParser();
        Program m = null;
        try {
            m = p.parse(r);
        } catch (SyntaxError syntaxError) {
            System.out.println("Illegal critter file: invalid program rules.");
            return null;
        }

        Critter c = new Critter(name, arr, m);
        return c;
    }

    private int readCritterHelper(Scanner s) throws UnexpectedInput {
        switch (s.nextIdentifier()) {
            case "memsize":
            case "defense":
            case "offense":
            case "energy":
            case "posture":
            case "size":
                s.consume(":");
                s.trailingWhitespace();
                return s.nextInt();
            default:
                throw new UnexpectedInput();
        }
    }

    @Override
    public boolean advanceTime(int n) {
        return false;
    }

    @Override
    public void printWorld(PrintStream out) {

    }
}
