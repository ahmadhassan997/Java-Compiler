package inter;

import symbols.*;

public class Else extends Stmt {
    Expr expr;
    Stmt stmt1, stmt2;

    public Else(Expr e, Stmt s1, Stmt s2) {
        this.expr = e;
        this.stmt1 = s1; this.stmt2 = s2;
        if (expr.type != Type.Bool) expr.error("Boolean required in IF");
    }

    public void gen(int b, int a) {
        int label1 = newlabel();
        int label2 = newlabel();
        expr.jumping(0, label2);

        emitlabel(label1);
        stmt1.gen(label1, a);
        emit("goto L" + a);

        emitlabel(label2);
        stmt2.gen(label2, a);
    }
}