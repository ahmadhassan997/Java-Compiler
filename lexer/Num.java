package lexer;
public class Num extends Token {
    public final int value;
    Num(int value) {super(Tag.NUM); this.value = value;}
}