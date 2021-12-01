package ast;

public class Factor extends Expr {
    protected int value;
    protected Operator operator;

    /**
     * Create a factor for a number.
     * Requires: v is nonnegative.
     * @param v
     */
    public Factor (int v) {
        value = v;
    }

    /**
     * Create a factor for a negative factor.
     * Requires: op must be NEGATIVE
     * @param op
     * @param f
     */
    public Factor (Operator op, Factor f) {
        super(f);
        operator = op;
    }

    /**
     * Create a factor for a negative Mem.
     * Requires: op must be NEGATIVE
     * @param op
     * @param f
     */
    public Factor (Operator op, Mem f) {
        super(f);
        operator = op;
    }

    /**
     * Create a factor for a negative Sensor.
     * Requires: op must be NEGATIVE
     * @param op
     * @param f
     */
    public Factor (Operator op, Sensor f) {
        super(f);
        operator = op;
    }

    /**
     * Create a factor for a negative (BinaryExpr)
     * Requires: op must be NEGATIVE_PAREN
     * @param op
     * @param f
     */
    public Factor (Operator op, BinaryExpr f) {
        super(f);
        operator = op;
    }


    public enum Operator {
        NEGATIVE,
        NEGATIVE_PAREN
    }

    @Override
    public Node cloneHelper() {
        if (operator == null) return new Factor(value);
        else {
            try{
                return new Factor(operator, (Factor) children.get(0).clone());
            } catch (ClassCastException e){
                try{
                    return new Factor(operator, (BinaryExpr) children.get(0).clone());
                } catch (ClassCastException e2) {
                    return new Factor(0);
                }
            }
        }
    }

    @Override
    public String toString() {
        return visit(new PrintVisitor());
    }

    private String visit(Visitor v){
        return v.visit(this);
    }

    @Override
    public boolean classInv() {
        if (operator == null) return value >= 0 && children == null;
        else return ((children.get(0) instanceof Factor && operator==Operator.NEGATIVE)
                || (children.get(0) instanceof BinaryExpr && operator==Operator.NEGATIVE_PAREN))
                && children.size()==1;
    }

    @Override
    public int value(){
        if (operator == null) return value;
        return -(((Expr)children.get(0)).value());
    }
}
