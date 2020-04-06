package inter;

import lexer.*;

public class Not extends Logical {
    public Not(Token tok, Expr ex2) {
        super(tok, ex2, ex2);
    }

    public void jumping(int t, int f) {
        expr2.jumping(f, t);
    }

    public String toString() {
        return op.toString() + " " + expr2.toString();
    }
}