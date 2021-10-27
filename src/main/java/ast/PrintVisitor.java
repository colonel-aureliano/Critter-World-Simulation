package ast;

import java.util.List;

public class PrintVisitor implements Visitor {

    @Override
    public String visit(Rule n, String operator) {
        return n.getChildren().get(0) + " " + operator + " " + n.getChildren().get(1) + ";\n";
    }

    @Override
    public String visit(Command n) {
        List<Node> children = n.getChildren();
        StringBuilder sb = new StringBuilder();
        sb.append(children.get(0));
        for (int i = 1; i < children.size(); i++) {
            sb.append("\n\t" + children.get(i));
        }
        return sb.toString();
    }

    @Override
    public String visit(Update n, String operator) {
        return n.getChildren().get(0) + " " + operator + " " + n.getChildren().get(1);
    }

    @Override
    public String visit(Action n, Action.Operator operator) {
        if (operator == Action.Operator.SERVE) {
            return operator.toString().toLowerCase() + "[" + n.getChildren().get(0) + "]";
        }
        return operator.toString().toLowerCase();
    }

    @Override
    public String visit(BinaryCondition n, BinaryCondition.Operator operator) {
        Condition left = (Condition) n.getChildren().get(0);
        Condition right = (Condition) n.getChildren().get(1);
        StringBuilder sb = new StringBuilder();
        if (left instanceof BinaryCondition) sb.append("{" + left + "}");
        else sb.append(left);
        sb.append(" " + operator.toString().toLowerCase() + " ");
        if (right instanceof BinaryCondition) sb.append("{" + right + "}");
        else sb.append(right);
        return sb.toString();
    }

    @Override
    public String visit(Relation n, Relation.Operator operator) {
        StringBuilder sb = new StringBuilder();
        sb.append(n.getChildren().get(0));
        switch (operator) {
            case EQUAL:
                sb.append(" = ");
                break;
            case LESS_THAN:
                sb.append(" < ");
                break;
            case NOT_EQUAL:
                sb.append(" != ");
                break;
            case GREATER_THAN:
                sb.append(" > ");
                break;
            case GREATER_THAN_OR_EQUAL:
                sb.append(" >= ");
                break;
            case LESS_THAN_OR_EQUAl:
                sb.append(" <= ");
                break;
        }
        sb.append(n.getChildren().get(1));
        return sb.toString();
    }

    @Override
    public String visit(BinaryExpr n, BinaryExpr.Operator operator) {
        StringBuilder sb = new StringBuilder();
        sb.append(n.getChildren().get(0));
        switch (operator) {
            case PLUS:
                sb.append(" + ");
                break;
            case MINUS:
                sb.append(" - ");
                break;
            case MULTIPLY:
                sb.append(" * ");
                break;
            case DIVIDE:
                sb.append(" / ");
                break;
            case MOD:
                sb.append(" mod ");
                break;
        }
        sb.append(n.getChildren().get(1));
        return sb.toString();
    }

    @Override
    public String visit(Factor n, int which, int value) {
        switch(which){
            case 0:
                return String.valueOf(value);
            case 1:
                return "-"+ n.getChildren().get(0);
            case 2:
                return "("+ n.getChildren().get(0)+")";
        }
        return "Factor class toString() error.";
    }

    @Override
    public String visit(Sensor n, Sensor.Operator operator) {
        StringBuilder sb = new StringBuilder();
        sb.append(operator.toString().toLowerCase());
        switch(operator){
            case SMELL:
                break;
            default:
                sb.append('[');
                sb.append(n.getChildren().get(0));
                sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public String visit(Mem n) {
        return "mem[" + n.getChildren().get(0) + "]";
    }
}
