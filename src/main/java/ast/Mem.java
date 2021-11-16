package ast;

public class Mem extends Expr {

    /**
     * Create a Memory Object with expression e.
     *
     * @param e
     */
    public Mem(Expr e) {
        super(e);
    }

    @Override
    public String toString() {
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v) { return v.visit(this); }

    @Override
    public Node clone() {
        return new Mem((Expr) children.get(0).clone());
    }

    @Override
    public boolean classInv() {
        if (!(children.size() == 1 && children.get(0) instanceof Expr)){
            return false;
        }
        if(children.get(0) instanceof Factor){
            if(Integer.valueOf(children.get(0).toString())<0){
                return false;
            }
        }
        return true;
    }

    @Override
    public int value() {
        int i = ((Expr) children.get(0)).value();
        return interpreter.getMem(i); // interpreter must be initialized
    }
}
