package parse;

import ast.*;
import exceptions.SyntaxError;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

class ParserImpl implements Parser {

    @Override
    public Program parse(Reader r) throws SyntaxError {
        Tokenizer t = new Tokenizer(r);
        return parseProgram(t);
    }

    /**
     * Parses a program from the stream of tokens provided by the Tokenizer, consuming tokens
     * representing the program. All following methods with a name "parseX" have the same spec
     * except that they parse syntactic form X.
     *
     * @return the created AST
     * @throws SyntaxError if the input tokens have invalid syntax
     */
    public static ProgramImpl parseProgram(Tokenizer t) throws SyntaxError {
        List<Rule> lr = new ArrayList<>();
        // check that t is not empty
        while (t.hasNext()){
            lr.add(parseRule(t));
            t.next(); // ? need to call t.next()
        }
        return new ProgramImpl(lr);
    }

    public static Rule parseRule(Tokenizer t) throws SyntaxError {
        Condition con = parseCondition(t);
        if (t.peek().getType()!= TokenType.ARR){ // checking for "-->"
            throw new SyntaxError(t.peek().lineNumber(),"A rule must have a command.");
        }
        Command com = parseCommand(t);

        return new Rule(con, com);
    }

    public static Condition parseCondition(Tokenizer t) throws SyntaxError {
        // parse condition
        // e.g. "3<5 and 2=2"

        Condition left;

        if (t.peek().getType()==TokenType.LBRACE){
            // {Condition}
            t.next();
            left = parseCondition(t);
            if (t.peek().getType()!=TokenType.RBRACE){
                throw new SyntaxError(t.peek().lineNumber(), "Braces should be closed.");
            }
        }
        else{
            left = parseRelation(t);
        }

        if (t.peek().getType()!=TokenType.AND &&
                t.peek().getType()!=TokenType.OR){
            return left;
        }
        else{
            Condition right;
            BinaryCondition bc;

            String s = t.next().toString();
            right = parseCondition(t);
            BinaryCondition.Operator o = null;
            switch(s){
                case "and":
                    o= BinaryCondition.Operator.AND;
                    break;
                case "or":
                    o= BinaryCondition.Operator.OR;
                    break;
            }
            bc = new BinaryCondition(left,o,right);
            return bc;
        }
    }

    public static Relation parseRelation(Tokenizer t) throws SyntaxError{
        // parse relation
        // e.g. "1 > 0", "2 != 5"
        //Expr l = parseExpression(t);
        if (!t.peek().isRelation()){
            throw new SyntaxError(t.peek().lineNumber(), "Missing relational operator.");
        }
        Relation.Operator rel = null;
        switch(t.next().getType().toString()){
            case "<":
                rel = Relation.Operator.LESS_THAN;
                break;
            case "<=":
                rel = Relation.Operator.LESS_THAN_OR_EQUAl;
                break;
            case ">":
                rel = Relation.Operator.GREATER_THAN;
                break;
            case ">=":
                rel = Relation.Operator.GREATER_THAN_OR_EQUAL;
                break;
            case "=":
                rel = Relation.Operator.EQUAL;
                break;
            case "!=":
                rel = Relation.Operator.NOT_EQUAL;
                break;
        }
        //Expr r = parseExpression(t);
        //Relation re = new Relation(l,rel,r);
        return null;
    }


    public static Expr parseExpression(Tokenizer t) throws SyntaxError {
        // parse expression
        // e.g. "1", "1+1", "3-2", "3-5-2-1+6"

        Expr e = parseTerm(t);
        while (t.peek().isAddOp()) {
            BinaryExpr.Operator op;
            switch(t.peek().getType().toString()) {
                case "+":
                    consume(t, TokenType.PLUS);
                    op = BinaryExpr.Operator.PLUS;
                    break;
                case "-":
                    consume(t, TokenType.MINUS);
                    op = BinaryExpr.Operator.MINUS;
                    break;
                default:
                    throw new SyntaxError(t.peek().lineNumber(), "Expected AddOp");
            }
            e = new BinaryExpr(e, op, parseTerm(t));
        }

        return e;
    }

    public static Expr parseTerm(Tokenizer t) throws SyntaxError {

        Expr e = parseFactor(t);
        while (t.peek().isMulOp()) {
            BinaryExpr.Operator op;
            switch(t.peek().getType().toString()) {
                case "*":
                    consume(t, TokenType.MUL);
                    op = BinaryExpr.Operator.MULTIPLY;
                    break;
                case "/":
                    consume(t, TokenType.DIV);
                    op = BinaryExpr.Operator.DIVIDE;
                    break;
                case "mod":
                    consume(t, TokenType.MOD);
                    op = BinaryExpr.Operator.MOD;
                    break;
                default:
                    throw new SyntaxError(t.peek().lineNumber(), "Expected MulOp");
            }
            e = new BinaryExpr(e, op, parseFactor(t));
        }
        return e;
    }

    public static Expr parseFactor(Tokenizer t) throws SyntaxError {

        Expr e;

        if (t.peek().isSensor()) e = parseSensor(t);

        switch(t.peek().getType().toString()){
            case "<number>":
                e= new Factor(Integer.parseInt(t.next().toString()));
                break;
            case "mem":
                consume(t, TokenType.MEM);
                e = new Factor(Factor.Operator.MEM,parseExpression(t));
                break;
            case "(":
                consume(t, TokenType.LPAREN);
                e = parseExpression(t);
                System.out.println("Finish lbrace");
                consume(t, TokenType.RPAREN);
                break;
            default:
                throw new SyntaxError(t.peek().lineNumber(), "Factor Syntax Error");
        }

        return e;

    }

    public static Expr parseSensor(Tokenizer t) throws SyntaxError {
        //TODO
        return null;
    }

    public static Command parseCommand(Tokenizer t) throws SyntaxError {
        // TODO
        throw new UnsupportedOperationException();
    }

    // TODO
    // add more as necessary...

    /**
     * Consumes a token of the expected type.
     *
     * @throws SyntaxError if the wrong kind of token is encountered.
     */
    public static void consume(Tokenizer t, TokenType tt) throws SyntaxError {
        if (t.peek().getType().equals(tt)) t.next();
        else throw new SyntaxError(t.lineNumber(), "Consumer type error");
    }
}
