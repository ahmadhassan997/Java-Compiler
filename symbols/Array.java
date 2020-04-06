package symbols;

import lexer.*;

public class Array extends Type {
    public Type of;
    public int size = 1;

    public Array (int sz, Type p) {
        super("[]", Tag.INDEX, sz * p.width);
        this.size = sz;
        this.of = p;
    }

    public String toString() {
        return "[" + size + "]" + of.toString();
    }
}