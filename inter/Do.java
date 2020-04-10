package inter;

import symbols.*;

public class Do extends Stmt {
    Expr expr;
    Stmt stmt;

    public Do() {
        this.expr = null;
        this.stmt = null;
    }

    public void init(Stmt s, Expr x) {
        expr = x; stmt =s;
        if (expr.type != Type.Bool ) expr.error("Boolean required in DO");
    }

    public void gen(int b, int a) {
        after = a;
        int label = newlabel();
        stmt.gen(b, label);
        emitlabel(label);
        expr.jumping(b, 0);
    }
}