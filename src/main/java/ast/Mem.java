package ast;

public class Mem extends Expr {
    private int i = -1;

    /**
     * Create a Memory Object with expression e.
     *
     * @param e
     */
    public Mem(Expr e) {
        super(e);
    }

    /**
     * Create a memory object in its syntactic sugar form.
     *
     * @param e
     */
    public Mem(int e) {
        this.i = e;
    }

    @Override
    public String toString() {
        if (i != -1) return "mem[" + i + "]";
        return "mem[" + children.get(0) + "]";
    }

    @Override
    public Node clone() {
        if (i != -1) {
            return new Mem(i);
        } else {
            return new Mem((Expr) children.get(0).clone());
        }
    }

    @Override
    public boolean classInv() {
        if ( i!= -1) return true;
        else return children.size() == 1 && children.get(0) instanceof Expr;
    }

}
