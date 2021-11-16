package controller;

import ast.Program;
import easyIO.EOF;
import easyIO.Scanner;
import easyIO.UnexpectedInput;
import exceptions.SyntaxError;
import model.*;
import parse.Parser;
import parse.ParserFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

public class ControllerImpl implements Controller {

    World w;

    @Override
    public ReadOnlyWorld getReadOnlyWorld() {
        return w;
    }

    @Override
    public void newWorld() {
        w = new World(Constants.WIDTH, Constants.HEIGHT, "Default World");
        int n = 20; // putting 20 rocks into world

        while(n>0){
            int[] t = w.getEmptySpace();
            w.addRock(t[0],t[1]);
            n--;
        }
    }

    @Override
    public boolean loadWorld(String filename, boolean enableManna, boolean enableForcedMutation) {
        try {
            readWorld(filename);
        } catch (FileNotFoundException e) {
            return false;
        }
        if (w == null) return false;
        return true;
    }

    private void readWorld(String filename) throws FileNotFoundException {
        Reader r = new BufferedReader(new FileReader(filename));
        easyIO.Scanner s = new Scanner(r, "");

        String name = null;
        int wi = -1;
        int h = -1;

        while (s.hasNext()) {
            try {
                s.newline();
            } catch (UnexpectedInput unexpectedInput) {
            }
            try {
                s.trailingWhitespace();
                s.consume("//");
                s.nextLine();
                continue;
            } catch (UnexpectedInput unexpectedInput) {
            }

            // Reading world name.
            if (name == null) {
                try {
                    if (!s.nextIdentifier().equals("name")) {
                        // First command word in world file must be "name".
                        throw new UnexpectedInput();
                    }
                    s.trailingWhitespace();
                    name = s.nextLine();
                    if (name.contains("//")) {
                        int t = name.indexOf("//");
                        name = name.substring(0, t);
                    }
                    name = name.trim();
                    continue;
                } catch (UnexpectedInput e) {
                    System.out.println("Illegal world file: invalid world name.");
                    return;
                }
            }

            // Reading size of world.
            if (wi == -1 && h == -1) {
                try {
                    if (!s.nextIdentifier().equals("size")) {
                        throw new UnexpectedInput();
                    }
                    s.trailingWhitespace();
                    wi = s.nextInt();
                    s.trailingWhitespace();
                    h = s.nextInt();
                    break;
                } catch (UnexpectedInput e) {
                    System.out.println("Illegal world file: invalid size values.");
                    return;
                }
            }
        }
        w = new World(wi, h, name);

        // Reading objects to put into world.
        while (s.hasNext()) {
            try {
                s.newline();
                continue;
            } catch (UnexpectedInput unexpectedInput) {
            }
            try {
                s.trailingWhitespace();
                s.consume("//");
                s.nextLine();
                continue;
            } catch (UnexpectedInput unexpectedInput) {
            }

            try {
                String t = s.nextIdentifier();
                s.trailingWhitespace();
                if (t.equals("rock")) {
                    int col = s.nextInt();
                    s.trailingWhitespace();
                    int row = s.nextInt();
                    w.addRock(col, row);
                } else if (t.equals("food")) {
                    int col = s.nextInt();
                    s.trailingWhitespace();
                    int row = s.nextInt();
                    s.trailingWhitespace();
                    int amount = s.nextInt();
                    w.addFood(col, row, amount);
                } else if (t.equals("critter")) {
                    s.trailingWhitespace();
                    StringBuilder path = new StringBuilder();
                    File f = new File(filename);
                    path.append(f.getParent() + '/');

                    while (s.peek() != 32) path.append(s.next());

                    s.trailingWhitespace();
                    int col = s.nextInt();
                    s.trailingWhitespace();
                    int row = s.nextInt();
                    s.trailingWhitespace();
                    int direction = s.nextInt();
                    Critter c = readCritter(path.toString());
                    w.addCritter(col, row, c, direction);
                }
                s.trailingWhitespace();
                continue;
            } catch (UnexpectedInput | EOF e) {
                System.out.println("Illegal world file: invalid objects in world.");
                return;
            }
        }

        return;
    }

    @Override
    public boolean loadCritters(String filename, int n) {
        if (w == null) return false; // cannot load critter before world is initialized
        Critter c;
        try {
            c = readCritter(filename);
        } catch (FileNotFoundException e) {
            return false;
        }
        if (c == null) return false;

        Random r = new Random();

        while(n>0){
            int[] t = w.getEmptySpace();
            if(t[0]==-1 && t[1]==-1) return false; // not enough empty space in world to put critters
            int col = t[0];
            int row = t[1];
            if(w.addCritter(col,row,c,r.nextInt(6))) n--;
        }

        return true;
    }

    // readCritter is declared protected for testing purposes only
    private Critter readCritter(String filename) throws FileNotFoundException {
        Reader r = new BufferedReader(new FileReader(filename));
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
        Program m;
        try {
            m = p.parse(r);
        } catch (SyntaxError syntaxError) {
            System.out.println("Illegal critter file: invalid program rules.");
            return null;
        }

        Critter c = new Critter(name, arr, m, w);
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
        while (n > 0) {
            if (!w.step()) return false;
            n--;
        }
        return true;
    }

    @Override
    public void printWorld(PrintStream out) {
        try {
            out.write(w.print().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
