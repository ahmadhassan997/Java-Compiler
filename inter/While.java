package inter;

import symbols.*;

public class While extends Stmt {
    Expr expr;
    Stmt stmt;

    public While() {
        expr = null; stmt = null;
    }

    public void init(Expr x, Stmt s) {
        expr = x; stmt = s;
        if (expr.type != Type.Bool) expr.error("Boolean required in while");
    }

    public void gen(int b, int a) {
        after = a;
        int label = newlabel();
        expr.jumping(0, a);
        emitlabel(label);
        stmt.gen(label, b);
        emit("goto L" + b);
    }
}