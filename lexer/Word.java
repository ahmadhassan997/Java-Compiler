package lexer;
public class Word extends Token {
    public final String lexeme;
    Word(int tag, String lexeme) {super(tag); this.lexeme = new String(lexeme);}
}