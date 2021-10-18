package main;

public class ParseAndMutateApp {

    public static void main(String[] args) {
        int n = 0;
        String file = null;
        try {
            if (args.length == 1) {
                file = args[0];
            } else if (args.length == 3 && args[0].equals("--mutate")) {
                n = Integer.parseInt(args[1]);
                if (n < 0) throw new IllegalArgumentException();
                file = args[2];
            } else {
                throw new IllegalArgumentException();
            }

            // TODO print program and mutations
            throw new IllegalArgumentException();

        } catch (IllegalArgumentException e) {
            System.out.println("Usage:\n  <input_file>\n  --mutate <n> <input_file>");
        }
    }
}
