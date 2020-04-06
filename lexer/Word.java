package lexer;

public class Word extends Token {
    public final String lexeme;
    public Word(String lexeme, int tag) {super(tag); this.lexeme = new String(lexeme);}
    public String toString() {return this.lexeme;}
    public static final Word
        and = new Word("&&", Tag.AND), or = new Word("||", Tag.OR),
        eq = new Word("==", Tag.EQ), ne = new Word("!=", Tag.NE),
        le = new Word("<=", Tag.LE), ge = new Word(">=", Tag.GE),
        minus = new Word("minus", Tag.MINUS), temp = new Word("t", Tag.TEMP),
        True = new Word("true", Tag.TRUE), False = new Word("false", Tag.FALSE);
}