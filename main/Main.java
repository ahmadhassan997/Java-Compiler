package main;

import java.io.*;
import lexer.*; import parser.*;

public class Main {
    public static void main(String []args) throws IOException {

        // for input stream : optional
        if ( args.length >= 1) {
            try {
                FileInputStream inFile = new FileInputStream(args[0]);
                System.setIn(inFile);
            }
            catch (FileNotFoundException err) {
                err.printStackTrace();
            }
        }

        // for output stream : optional
        if ( args.length >= 2) {
            PrintStream outFile = new PrintStream(new FileOutputStream(args[1]));
            System.setOut(outFile);
        }

        // Initialize front-end syntax directed translator
        Lexer lex = new Lexer();
        Parser parse = new Parser(lex);
        parse.program();
        System.out.write('\n');
    }
}