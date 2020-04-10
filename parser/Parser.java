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
        return null;
    }
}