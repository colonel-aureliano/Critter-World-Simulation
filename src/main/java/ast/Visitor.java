package ast;

public interface Visitor {

    String visit(BinaryCondition n, BinaryCondition.Operator operator);

    String visit(BinaryExpr b);

}
