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
        return "mem["+children.get(0)+"]";
    }

    @Override
    public Node clone() {
        if(i!=-1){
            return new Mem(i);
        }
        else{
            return new Mem((Expr) children.get(0).clone());
        }
    }

    @Override
    public boolean classInv() {
        return (children.size()==1 && children.get(0) instanceof Expr || i!=-1);
    }
    
}
