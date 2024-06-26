package ast;

import java.util.List;

public class PrintVisitor implements Visitor {

    @Override
    public String visit(Rule n) {
        return n.getChildren().get(0) + " " + n.operator + " " + n.getChildren().get(1) + ";\n";
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
    public String visit(Update n) {
        return "mem[" + n.getChildren().get(0) + "] "
                + n.operator + " " + n.getChildren().get(1);
    }

    @Override
    public String visit(Action n) {
        if (n.operator == Action.Operator.SERVE) {
            return n.operator.toString().toLowerCase() + "[" + n.getChildren().get(0) + "]";
        }
        return n.operator.toString().toLowerCase();
    }

    @Override
    public String visit(BinaryCondition n) {
        Condition left = (Condition) n.getChildren().get(0);
        Condition right = (Condition) n.getChildren().get(1);
        StringBuilder sb = new StringBuilder();
        if (left instanceof BinaryCondition) sb.append("{" + left + "}");
        else sb.append(left);
        sb.append(" " + n.operator.toString().toLowerCase() + " ");
        if (right instanceof BinaryCondition) sb.append("{" + right + "}");
        else sb.append(right);
        return sb.toString();
    }

    @Override
    public String visit(Relation n) {
        StringBuilder sb = new StringBuilder();
        sb.append(n.getChildren().get(0));
        switch (n.operator) {
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
    public String visit(BinaryExpr n) {
        StringBuilder sb = new StringBuilder();
        Node left = n.getChildren().get((0));
        if (left instanceof BinaryExpr &&
                LNeedParen(n.operator, ((BinaryExpr)left).operator)) sb.append("(" + left + ")");
        else sb.append(left);
        switch (n.operator) {
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
        Node right = n.getChildren().get((1));
        if (right instanceof BinaryExpr &&
                RNeedParen(n.operator, ((BinaryExpr)right).operator)) sb.append("(" + right + ")");
        else sb.append(right);
        return sb.toString();
    }

    @Override
    public String visit(Factor n) {
        if (n.operator == null) return String.valueOf(n.value);
        else if (n.operator== Factor.Operator.NEGATIVE) return "-" + n.getChildren().get(0);
        else return "-(" + n.getChildren().get(0)+")";
    }

    @Override
    public String visit(Sensor n) {
        StringBuilder sb = new StringBuilder();
        sb.append(n.operator.toString().toLowerCase());
        switch (n.operator) {
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

    private boolean LNeedParen(BinaryExpr.Operator nodeOp, BinaryExpr.Operator childOp) {
        switch(nodeOp) {
            case PLUS:
            case MINUS:
                return false;
            case MULTIPLY:
            case DIVIDE:
            case MOD:
                return (childOp == BinaryExpr.Operator.MINUS | childOp == BinaryExpr.Operator.PLUS); // child is AddOp
            default:
                return true;
        }
    }

    private boolean RNeedParen(BinaryExpr.Operator nodeOp, BinaryExpr.Operator childOp) {
        switch(nodeOp) {
            case MINUS:
                if (childOp == BinaryExpr.Operator.PLUS) return true;
            case PLUS:
                return LNeedParen(nodeOp, childOp);
            case MOD:
                if (childOp == BinaryExpr.Operator.DIVIDE) return true;
            case DIVIDE:
                if (childOp == BinaryExpr.Operator.MULTIPLY) return true;
            case MULTIPLY:
                return LNeedParen(nodeOp, childOp);
            default:
                return true;
        }
    }



}
