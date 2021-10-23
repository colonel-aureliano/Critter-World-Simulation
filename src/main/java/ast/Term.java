package ast;

import java.util.ArrayList;
import java.util.List;

public class Term extends Expression{

    List<Expression> le = new ArrayList<>();

    @Override
    public void add(Expr e) {
        le.add((Expression) e);
    }

}
