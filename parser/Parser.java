package parser;

import java.io.*; import lexer.*;
import symbols.*; import inter.*;

/**
 * The parser reads a stream of tokens and 
 * builds a syntax tree by calling the
 * appropriate constructor functions
 */

public class Parser {
    private Lexer lex; // lexical analyzer for this 
    private Token look; // lookahead token
    Env top = null; // top symbol table
    int used = 0; // storage used for declarations

    public Parser(Lexer l) throws IOException {
        this.lex = l;
         move();
    }

    void move() throws IOException { 
        look = lex.scan(); 
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) throws IOException {
        if ( look .tag == t) move();
        else error("syntax error");
    }

    /**
     * The function calls block() procedure to 
     * parse the input stream and build the syntax tree.
     * @throws IOException
     */

    public void program() throws IOException {
        Stmt s = block();
        int begin = s.newlabel();
        int after = s.newlabel();

        s.emitlabel(begin);
        s.gen(begin, after);
        s.emitlabel(after);
    }

    /**
     * This procedure will explicitly handle symbol-tables.
     * Variable $top holds the top symbol table while 
     * variable $savedEnv is a link to the previous table.
     * @return
     * @throws IOException
     */

    Stmt block() throws IOException {
        // match the block entry pattern and store the current
        match('{');  
        Env savedEnv = top;
        // fill the symbol table with identifiers using the declarations
        top = new Env(top);
        decls();
        Stmt s = stmts();
        // restore the top symbol table and match the block exit pattern
        match('}');
        top = savedEnv;
        return s;
    }

    /**
     * Fill the symbol table entries by going over
     * the declaration statements in code.
     * @throws IOException
     */

    void decls() throws IOException {
        while ( look.tag == Tag.BASIC ) {
            // match pattern for a declaration statement
            Type p = type();
            Token tok = look;
            match(Tag.ID);
            match(';');
            Id id = new Id( (Word)tok, p, used);
            top.put(tok, id);
            used += p.width;
        }
    }

    Type type() throws IOException {
        Type p = (Type) look; // except for look.tag == Tag.BASIC
        match(Tag.BASIC);
        if ( look.tag != '[') return p; // T-> Basic
        else return dims(p); // return array type
    }

    Type dims(Type p) throws IOException {
       // match the pattern for an array access
       match('[');
       Token tok = look;
       match(Tag.NUM);
       match(']');
       // check recursively for nested array
       if ( look.tag == '[' ) p = dims(p);
       return new Array( ((Num)tok).value, p );
    }

    Stmt stmts() throws IOException {
        // return only when the statement block has ended 
        if (look.tag == '}') return Stmt.Null;
        // else return sequence of statements
        else return new Seq(stmt(), stmts());
    }

    /**
     * Procedure stmt has a switch statement with cases 
     * corresponding to the productions for nonterminal Stmt.
     * The nodes for while and do statements are constructed 
     * when the parser sees the opening keyword.
     * The nodes are constructed before the statement is parsed 
     * to allow any enclosed break statement to point back to its 
     * enclosing loop.
     * Nested loops are handled by using variable Stmt. Enclosing in 
     * class Stmt and savedStmt to maintain the current enclosing loop.
     * @return statements
     * @throws IOException
     */

    Stmt stmt() throws IOException {
        Expr x;
        Stmt s, s1, s2;
        Stmt savedStmt; // save enclosing loop for breaks
        // check each case for tags
        switch( look.tag ){
            case ';':
                move();
                return Stmt.Null;
            case Tag.IF:
                // pattern matching for IF block
                match(Tag.IF);
                match('(');
                x = bool();
                match(')');
        }
        
    }

    /**
     * the code for assignments appears in this auxiliary procedure
     * @return return assignment statement
     * @throws IOException
     */

    Stmt assign() throws IOException {
        Stmt stmt;
        Token t = look;
        match(Tag.ID);
        Id id = top.get(t); // check previous declaration in symbol table
        if ( id == null ) error(t.toString() + " undeclared");
        if ( look.tag == '=') {
            move();
            stmt = new Set(id, bool());
        }
        else {
            Access x = offset(id);
            match('=');
            stmt = new SetElem(x, bool());
        }
        match(';');
        return stmt;
    }

    Expr bool() throws IOException {
        Expr x = join();
        while( look.tag == Tag.OR ) {
            Token tok = look;
            move();
            x = new Or(tok, x, join());
        }
        return x;
    }

    Expr join() throws IOException {
        Expr x = equality();
        while ( look.tag == Tag.AND ) {
            Token tok = look;
            move();
            x = new And(tok, x, equality());
        }
        return x;
    }

    Expr equality() throws IOException {
        Expr x = rel();
        while ( look.tag == Tag.EQ || look.tag == Tag.NE ) {
            Token tok = look;
            move();
            x = new Rel(tok, x, rel());
        }
        return x;
    }

    Expr rel() throws IOException {
        Expr x = expr();
        switch (look.tag) {
            case '<': case Tag.LE: case Tag.GE: case '>':
                Token tok = look;
                move();
                return new Rel(tok, x, expr());
            default:
                return x;
        }
    }

    Expr expr() throws IOException {
        Expr x = term();
        while ( look.tag == '+' || look.tag == '-') {
            Token tok = look;
            move();
            x = new Arith(tok, x, term());
        }
        return x;
    }

    Expr term() throws IOException {
        Expr x = unary();
        while ( look.tag == '*' || look.tag == '/') {
            Token tok = look;
            move();
            x = new Arith(tok, x, unary());
        }
        return x;
    }

    Expr unary() throws IOException {
        if ( look.tag == '-') {
            move();
            return new Unary(Word.minus, unary());
        }
        else if ( look.tag == '!' ) {
            Token tok = look;
            move();
            return new Not(tok, unary());
        }
        else return factor();
    }

    Expr factor() throws IOException {
        Expr x = null;
        switch ( look.tag ) {
            case '(':
                move();
                x = bool();
                match(')');
                return x;
            case Tag.NUM:
                x = new Constant(look, Type.Int);
                move();
                return x;
            case Tag.REAL:
                x = new Constant(look, Type.Float);
                move();
                return x;
            case Tag.TRUE:
                x = Constant.True;
                move();
                return x;
            case Tag.FALSE:
                x = Constant.False;
                move();
                return x;
            default:
                error("syntax error");
                return x;
            case Tag.ID:
                String s = look.toString();
                Id id = top.get(look);
                if ( id == null ) error(look.toString() + " undeclared");
                move();
                if ( look.tag != '[' ) return id;
                else return offset(id);
        }
    }

    Access offset(Id a) throws IOException {
        return null;
    }
}