package inter;

public class Break extends Stmt {
    Stmt stmt;

    public Break() {
        if ( Stmt.Enclosing == null ) error("Unenclosed break");
        stmt = Stmt.Enclosing;
    }

    public void gen(int b, int a) {
        emit("goto L" + stmt.after);
    }
}