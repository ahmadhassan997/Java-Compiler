package inter;

import symbols.*;

public class If extends Stmt {
    Expr expr;
    Stmt stmt;

    public If(Expr e, Stmt s) {
        this.expr = e;
        this.stmt = s;
        if (expr.type != Type.Bool ) expr.error("Boolean Required in IF")
    }

    public void gen(int b, int a) {
        int label = newlabel();
        expr.jumping(0, a);
        emitlabel(label);
        stmt.gen(label, a);
    }
}