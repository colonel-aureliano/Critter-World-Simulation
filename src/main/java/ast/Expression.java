package ast;

import java.util.ArrayList;
import java.util.List;

public class Expression extends Expr{

    List<Expr> le = new ArrayList<>();

    @Override
    public void add(Expr e){
        le.add(e);
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
