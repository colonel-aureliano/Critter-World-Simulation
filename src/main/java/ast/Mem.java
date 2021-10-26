package ast;

public class Mem extends Expr{
    private int i = -1;

    public Mem(Expr e){
        super(e);
    }

    public Mem(int e){
        this.i=e;
    }

    @Override
    public String toString() {
        if(i!=-1) return "mem["+i+"]";
        return "mem["+single+"]";
    }

    @Override
    public Node clone() {
        if(i!=-1){
            return new Mem(i);
        }
        else{
            return new Mem((Expr) single.clone());
        }
    }

    @Override
    public boolean classInv() {
        return (single!=null || i!=-1);
    }
    
}
