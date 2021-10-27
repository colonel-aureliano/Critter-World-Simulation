package ast;

public interface Visitor {

    String visit(Rule n, String operator);

    String visit(Command n);

    String visit(Update n, String operator);

    String visit(Action n, Action.Operator operator);

    String visit(BinaryCondition n, BinaryCondition.Operator operator);

    String visit(Relation n, Relation.Operator operator);

    String visit(BinaryExpr n, BinaryExpr.Operator operator);

    String visit(Factor n, int which, int value);

    String visit(Sensor n, Sensor.Operator operator);

    String visit(Mem n);

}
