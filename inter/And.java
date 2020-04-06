package inter;

import lexer.*;

public class And extends Logical {
    public And(Token tok, Expr ex1, Expr ex2) {
        super(tok, ex1, ex2);
    }

    public void jumping(int t, int f) {
        int label = f != 0 ? f : newlabel();
        expr1.jumping(0, label);
        expr2.jumping(t, f);
        if (f == 0) emitlabel(label);
    }
}