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
     * @throws SyntaxError if there the input tokens have invalid syntax
     */
    public static ProgramImpl parseProgram(Tokenizer t) throws SyntaxError {
        List<Rule> lr = new ArrayList<>();
        lr.add(parseRule(t));
        while (t.hasNext()){
            lr.add(parseRule(t));
        }
        return new ProgramImpl(lr);
    }

    public static Rule parseRule(Tokenizer t) throws SyntaxError {
        Condition con = parseCondition(t);
        if (t.peek().getType()!= TokenType.ARR){ // checking for "-->"
            throw new SyntaxError(t.peek().lineNumber(),"A rule must have a command.");
        }
        Command com = parseCommand(t);

        List<Node> ln = new ArrayList<>();
        ln.add(con); // a Condition node
        ln.add(com); // a Command node

        return new Rule(ln);
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

        return left; // Currently only a unary condition
    }

    public static Relation parseRelation(Tokenizer t) throws SyntaxError{
        // parse relation
        // e.g. "1 > 0", "2 != 5"
        Expr l = parseExpression(t);
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
        Expr r = parseExpression(t);
        Relation re = new Relation(l,rel,r);
        return re;
    }

    public static Expr parseExpression(Tokenizer t) throws SyntaxError {
        // parse expression
        // e.g. "1", "1+1", "3-2", "3-5-2-1+6"

        Expr e = new Expression();

        if(!t.peek().isNum()) {
            throw new SyntaxError(t.next().lineNumber(), "Missing a number.");
        }
        e.add(parseTerm(t));
        while(t.peek().isAddOp()){
            Expr operator = new addop(t.next().toString());
            e.add(operator);
            if(!t.peek().isNum()) {
                throw new SyntaxError(t.next().lineNumber(), "Missing a number.");
            }
            e.add(parseTerm(t));
        }

        return e;
    }

    public static Expr parseTerm(Tokenizer t) throws SyntaxError {
        Expr e = new Term();

        e.add(parseFactor(t));
        while(t.peek().isMulOp()){
            Expr operator = new mulop(t.next().toString());
            e.add(operator);
            if(!t.peek().isNum()) {
                throw new SyntaxError(t.next().lineNumber(), "Error.");
            }
            e.add(parseFactor(t));
        }

        return e;
    }

    public static Expr parseFactor(Tokenizer t) throws SyntaxError {
        Expr e = null;
        if(t.peek().isNum()){
            e=new Factor(t.next().toString());
        }
        return e;
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
        // TODO
        throw new UnsupportedOperationException();
    }
}
