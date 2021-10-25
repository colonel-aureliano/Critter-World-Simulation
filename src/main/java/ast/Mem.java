package ast;

public class Mem extends Expr{
    private Expr e;
    private int i = -1;

    public Mem(Expr e){
        this.e=e;
    }

    public Mem(int e){
        this.i=e;
    }

    @Override
    public String toString() {
        if(i!=-1) return "mem["+i+"]";
        return "mem["+e+"]";
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
