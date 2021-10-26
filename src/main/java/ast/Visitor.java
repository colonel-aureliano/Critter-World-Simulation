package ast;

public interface Visitor {

    String visit(BinaryCondition n);

    String visit(BinaryExpr b);

}
