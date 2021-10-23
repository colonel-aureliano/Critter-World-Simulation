package ast;

public class mulop extends Term{
    private mulop.Operator o;

    enum Operator{
        MULTIPLY,
        DIVIDE,
        MOD
    }

    public mulop(String s){
        switch (s){
            case "*":
                o= mulop.Operator.MULTIPLY;
                break;
            case "/":
                o= mulop.Operator.DIVIDE;
                break;
            case "mod":
                o= mulop.Operator.MOD;
                break;
        }
    }
}
