package controller;

import easyIO.EOF;
import easyIO.Scanner;
import easyIO.UnexpectedInput;
import model.ReadOnlyWorld;

import java.io.*;
import java.util.Arrays;

public class ControllerImpl implements Controller{

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

    @Override
    public boolean loadCritters(String filename, int n) {
        InputStream in = ClassLoader.getSystemResourceAsStream(filename);
        Reader r = new BufferedReader(new InputStreamReader(in));
        Scanner s = new Scanner(r,"");

        String name = null;
        int[] arr = new int[7];
        int i = 0;

        while(s.hasNext()){
            s.trailingWhitespace();
            try{
                s.consume("//");
                s.nextLine();
            } catch (UnexpectedInput unexpectedInput) {
            }
            s.trailingWhitespace();

            // Reading species name.
            if(name==null) {
                try {
                    if (!s.nextIdentifier().equals("species")) {
                        // First command word in critter file must be "species".
                        throw new UnexpectedInput();
                    }
                    s.consume(":");
                    s.trailingWhitespace();
                    name = s.nextLine();
                    name = name.substring(0, name.length() - 1);
                    continue;
                } catch (UnexpectedInput unexpectedInput) {
                    throw new IllegalArgumentException("Illegal critter file.");
                }
            }

            // Reading critter mem values.
            if(i < arr.length) {
                try {
                    if (i == 5) {
                        arr[i] = 1;
                        i++;
                        continue;
                    }
                    arr[i] = CritterReadHelper(s);
                    i++;
                } catch (UnexpectedInput e) {
                    e.printStackTrace();
                }
            }

            // TODO Reading critter program.

            try {
                s.nextLine();
            } catch (UnexpectedInput e) {
            }
        }

        System.out.println(name);
        System.out.println(Arrays.toString(arr));
        return false;
    }

    private int CritterReadHelper(Scanner s) throws UnexpectedInput {
        switch(s.nextIdentifier()){
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
