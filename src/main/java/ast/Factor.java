package ast;

public class Factor extends Term{
    private int n;

    public Factor(String n){
        System.out.println("Factor class storing: "+n);
        this.n=Integer.parseInt(n);
    }
}
