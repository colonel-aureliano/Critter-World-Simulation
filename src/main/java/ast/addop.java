package ast;

public class addop extends Expression{
    private Operator o;

    enum Operator{
        PLUS,
        MINUS
    }

    public addop(String s){
        switch (s){
            case "+":
                o=Operator.PLUS;
                break;
            case "-":
                o=Operator.MINUS;
                break;
        }
    }
}
