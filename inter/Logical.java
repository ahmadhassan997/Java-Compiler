package inter;

import lexer.*;
import symbols.*;

public class Logical extends Expr {
    public Expr expr1, expr2;

    Logical(Token tok, Expr ex1, Expr ex2) {
        super(tok, null);
        this.expr1 = ex1;
        this.expr2 = ex2;
        type = check(expr1.type, expr2.type);
        if (type == null) error("type error");
    }

    public Type check(Type p1, Type p2) {
        if (p1 == Type.Bool && p2 == Type.Bool) return Type.Bool;
        else return null; 
    }

    public Expr gen() {
        int f = newlabel();
        int a = newlabel();
        Temp t = new Temp(type);
        this.jumping(0, f);
        emit(t.toString() + " = true");
        emit("goto L" + a);
        emitlabel(f);
        emit(t.toString() + " = false");
        emitlabel(a);
        return t;
    }

    public String toString() {
        return expr1.toString() + " " + op.toString() + " " + expr2.toString();
    }
}