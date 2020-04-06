package lexer;

public class Token {
    public final int tag;
    Token(int tag) {this.tag = tag;}
    public String toString() {return "" + (char)this.tag;}
}