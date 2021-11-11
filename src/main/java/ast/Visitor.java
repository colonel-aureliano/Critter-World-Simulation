package ast;

public interface Visitor {

    String visit(Rule n);

    String visit(Command n);

    String visit(Update n);

    String visit(Action n);

    String visit(BinaryCondition n);

    String visit(Relation n);

    String visit(BinaryExpr n);

    String visit(Factor n);

    String visit(Sensor n);

    String visit(Mem n);

}
