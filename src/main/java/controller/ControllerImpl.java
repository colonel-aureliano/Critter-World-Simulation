package controller;

import ast.Program;
import ast.ProgramImpl;
import easyIO.EOF;
import easyIO.Scanner;
import easyIO.UnexpectedInput;
import exceptions.SyntaxError;
import model.*;
import parse.Parser;
import parse.ParserFactory;

import java.io.*;
import java.util.Arrays;

public class ControllerImpl implements Controller {

    World w;

    @Override
    public ReadOnlyWorld getReadOnlyWorld() {
        return w;
    }

    @Override
    public void newWorld() {
        w= new World(Constants.WIDTH,Constants.HEIGHT,"Default World");
    }

    @Override
    public boolean loadWorld(String filename, boolean enableManna, boolean enableForcedMutation) {
        World world;
        try {
            world = readWorld(filename);
        } catch (FileNotFoundException e) {
            return false;
        }
        if(world==null) return false;
        w=world;
        return true;
    }

    private World readWorld(String filename) throws FileNotFoundException {
        Reader r = new BufferedReader(new FileReader(filename));
        easyIO.Scanner s = new Scanner(r, "");

        String name = null;
        int w = -1 ;
        int h = -1;

        World world;

        while(s.hasNext()) {
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
                    return null;
                }
            }

            // Reading size of world.
            if (w == -1 && h == -1) {
                try {
                    if (!s.nextIdentifier().equals("size")) {
                        throw new UnexpectedInput();
                    }
                    s.trailingWhitespace();
                    w = s.nextInt();
                    s.trailingWhitespace();
                    h = s.nextInt();
                    break;
                } catch (UnexpectedInput e) {
                    System.out.println("Illegal world file: invalid size values.");
                    return null;
                }
            }
        }
        world = new World(w,h,name);

        // Reading objects to put into world.
        while(s.hasNext()) {
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
                if (t.equals("rock")){
                    int col = s.nextInt();
                    s.trailingWhitespace();
                    int row = s.nextInt();
                    world.addRock(col,row);
                }
                else if (t.equals("food")){
                    int col = s.nextInt();
                    s.trailingWhitespace();
                    int row = s.nextInt();
                    s.trailingWhitespace();
                    int amount = s.nextInt();
                    world.addFood(col,row,amount);
                }
                else if (t.equals("critter")){
                    s.trailingWhitespace();
                    StringBuilder path = new StringBuilder();
                    File f = new File(filename);
                    path.append(f.getParent()+'/');

                    while(s.peek()!=32) path.append(s.next());

                    s.trailingWhitespace();
                    int col = s.nextInt();
                    s.trailingWhitespace();
                    int row = s.nextInt();
                    s.trailingWhitespace();
                    int direction = s.nextInt();
                    Critter c=readCritter(path.toString());
                    world.addCritter(col,row,c,direction);
                }
                continue;
            } catch (UnexpectedInput | EOF e) {
                System.out.println("Illegal world file: invalid objects in world.");
                return null;
            }
        }

        return world;
    }

    @Override
    public boolean loadCritters(String filename, int n) {
        if(w==null) return false; // cannot load critter before world is initialized
        Critter c;
        try {
            c = readCritter(filename);
        } catch (FileNotFoundException e) {
            return false;
        }
        if (c == null) return false;
        //TODO randomly put n critter(s) into world
        // w.addCritter(...)
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
        ((ProgramImpl) m).critterWorldSetUp(c,w);
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
        return false; //TODO
    }

    @Override
    public void printWorld(PrintStream out) {
        //TODO
    }
}
