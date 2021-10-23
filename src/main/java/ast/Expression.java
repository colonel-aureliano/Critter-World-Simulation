package ast;

import java.util.List;

public class Expression extends Expr{

    public Expression(List<Node> ln){
        super(ln);
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean classInv() {
        return false;
    }
}
