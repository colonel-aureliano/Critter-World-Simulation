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
        while (t.hasNext()){
            lr.add(parseRule(t));
        }
        return new ProgramImpl(lr);
    }

    public static Rule parseRule(Tokenizer t) throws SyntaxError {
        Condition con = parseCondition(t);
        consume(t, TokenType.ARR);
        Command com = parseCommand(t);
        consume(t, TokenType.SEMICOLON);
        return new Rule(con, com);
    }

    public static Command parseCommand(Tokenizer t) throws SyntaxError {
        Command c = new Command();
        while (t.peek().getType() != TokenType.SEMICOLON) {
            if (t.peek().isAction()) {
                c.add(parseAction(t));
                break;
            }
            c.add(parseUpdate(t));
        }
        return c;
    }

    public static Update parseUpdate(Tokenizer t) throws SyntaxError {
        consume(t, TokenType.MEM);
        consume(t, TokenType.LBRACKET);
        Expr first = parseExpression(t);
        consume(t, TokenType.RBRACKET);
        consume(t, TokenType.ASSIGN);
        Expr second = parseExpression(t);
        return new Update(first, second);
    }

    public static Action parseAction(Tokenizer t) throws SyntaxError {
        switch(t.peek().getType().toString()) {
            case "wait":
                consume(t, TokenType.WAIT);
                return new Action(Action.Operator.WAIT);
            case "forward":
                consume(t, TokenType.FORWARD);
                return new Action(Action.Operator.FORWARD);
            case "backward":
                consume(t, TokenType.BACKWARD);
                return new Action(Action.Operator.BACKWARD);
            case "left":
                consume(t, TokenType.LEFT);
                return new Action(Action.Operator.LEFT);
            case "right":
                consume(t, TokenType.RIGHT);
                return new Action(Action.Operator.RIGHT);
            case "eat":
                consume(t, TokenType.EAT);
                return new Action(Action.Operator.EAT);
            case "attack":
                consume(t, TokenType.ATTACK);
                return new Action(Action.Operator.ATTACK);
            case "grow":
                consume(t, TokenType.GROW);
                return new Action(Action.Operator.GROW);
            case "bud":
                consume(t, TokenType.BUD);
                return new Action(Action.Operator.BUD);
            case "mate":
                consume(t, TokenType.MATE);
                return new Action(Action.Operator.MATE);
            case "serve":
                consume(t, TokenType.SERVE);
                consume(t, TokenType.LBRACKET);
                Action a = new Action(Action.Operator.SERVE, parseExpression(t));
                consume(t, TokenType.RBRACKET);
                return a;
            default:
                throw new SyntaxError(t.peek().lineNumber(),"Action Syntax Error");
        }
    }


    public static Condition parseCondition(Tokenizer t) throws SyntaxError {
        // parse condition
        // e.g. "3<5 and 2=2"
        Condition con = parseConjunction(t);
        while(t.peek().getType() == TokenType.OR) {
            consume(t, TokenType.OR);
            con = new BinaryCondition(con, BinaryCondition.Operator.OR, parseConjunction(t));
        }
        return con;
    }

    public static Condition parseConjunction(Tokenizer t) throws SyntaxError {
        Condition con = parseRelation(t);
        while(t.peek().getType() == TokenType.AND) {
            consume(t, TokenType.AND);
            con = new BinaryCondition(con, BinaryCondition.Operator.AND, parseConjunction(t));
        }
        return con;
    }

    public static Condition parseRelation(Tokenizer t) throws SyntaxError{
        // parse relation
        // e.g. "1 > 0", "2 != 5"

        Condition con;
        if (t.peek().getType() == TokenType.LBRACE) {
            consume(t, TokenType.LBRACE);
            con = parseCondition(t);
            consume(t, TokenType.RBRACE);
            return con;
        }

        Expr left = parseExpression(t);
        Relation.Operator rel;
        switch(t.peek().getType().toString()){
            case "<":
                consume(t, TokenType.LT);
                rel = Relation.Operator.LESS_THAN;
                break;
            case "<=":
                consume(t, TokenType.LE);
                rel = Relation.Operator.LESS_THAN_OR_EQUAl;
                break;
            case ">":
                consume(t, TokenType.GT);
                rel = Relation.Operator.GREATER_THAN;
                break;
            case ">=":
                consume(t, TokenType.GE);
                rel = Relation.Operator.GREATER_THAN_OR_EQUAL;
                break;
            case "=":
                consume(t, TokenType.EQ);
                rel = Relation.Operator.EQUAL;
                break;
            case "!=":
                consume(t, TokenType.NE);
                rel = Relation.Operator.NOT_EQUAL;
                break;
            default:
                throw new SyntaxError(t.peek().lineNumber(), "Relation Syntax Error at " + t.peek().getType());
        }
        Expr right = parseExpression(t);

        con = new Relation(left, rel, right);
        return con;
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

        if (t.peek().isSensor()) return parseSensor(t);

        switch(t.peek().getType().toString()){
            case "<number>":
                e = new Factor(Integer.parseInt(t.next().toString()));
                break;
            case "mem":
                consume(t, TokenType.MEM);
                consume(t, TokenType.LBRACKET);
                e = new Factor(Factor.Operator.MEM,parseExpression(t));
                consume(t, TokenType.RBRACKET);
                break;
            case "(":
                consume(t, TokenType.LPAREN);
                e = parseExpression(t);
                consume(t, TokenType.RPAREN);
                break;
            case "-":
                consume(t, TokenType.MINUS);
                e = new Factor(Factor.Operator.NEGATIVE, parseFactor(t));
                break;
            default:
                throw new SyntaxError(t.peek().lineNumber(), "Factor Syntax Error at " + t.peek().getType());
        }

        return e;

    }

    public static Expr parseSensor(Tokenizer t) throws SyntaxError {
        Expr e;

        switch(t.peek().getType().toString()) {
            case "nearby":
                consume(t, TokenType.NEARBY);
                consume(t, TokenType.LBRACKET);
                e = new Sensor(Sensor.Operator.NEARBY, parseExpression(t));
                consume(t, TokenType.RBRACKET);
                break;
            case "ahead":
                consume(t, TokenType.AHEAD);
                consume(t, TokenType.LBRACKET);
                e = new Sensor(Sensor.Operator.AHEAD, parseExpression(t));
                consume(t, TokenType.RBRACKET);
                break;
            case "random":
                consume(t, TokenType.RANDOM);
                consume(t, TokenType.LBRACKET);
                e = new Sensor(Sensor.Operator.RANDOM, parseExpression(t));
                consume(t, TokenType.RBRACKET);
                break;
            case "smell":
                consume(t, TokenType.SMELL);
                consume(t, TokenType.LBRACKET);
                e = new Sensor(Sensor.Operator.SMELL);
                consume(t, TokenType.RBRACKET);
                break;
            default:
                throw new SyntaxError(t.peek().lineNumber(), "Sensor Syntax Syntax Error at " + t.peek().getType());
        }

        return e;
    }



    // TODO
    // add more as necessary...

    /**
     * Consumes a token of the expected type.
     *
     * @throws SyntaxError if the wrong kind of token is encountered.
     */
    public static void consume(Tokenizer t, TokenType tt) throws SyntaxError {
        if (t.peek().getType() == tt) t.next();
        else throw new SyntaxError(t.lineNumber(), "Expected Type: " + tt + ". Provided Type: " + t.peek().getType());
    }
}
