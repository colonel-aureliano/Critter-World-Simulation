package ast;

public class PrintVisitor implements Visitor {
    @Override
    public String visit(BinaryCondition n, BinaryCondition.Operator operator) {
        Condition left = (Condition) n.getChildren().get(0);
        Condition right = (Condition) n.getChildren().get(1);
        StringBuilder sb = new StringBuilder();
        if(left instanceof BinaryCondition){
            sb.append("{"+left+"}");
        }
        else{
            sb.append(left);
        }
        sb.append(" "+operator.toString().toLowerCase()+" ");
        if(right instanceof BinaryCondition){
            sb.append("{"+right+"}");
        }
        else{
            sb.append(right);
        }
        return sb.toString();
    }

    @Override
    public String visit(BinaryExpr b) {
        return null;
    }
}
