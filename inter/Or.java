package inter;

import lexer.*;

public class Or extends Logical {
    public Or(Token tok, Expr ex1, Expr ex2) {
        super(tok, ex1, ex2);
    }

    public void jumping(int t, int f) {
        int label = t != 0 ? t : newlabel();
        expr1.jumping(label, 0);
        expr2.jumping(t, f);
        if (t == 0) emitlabel(label);
    }
}