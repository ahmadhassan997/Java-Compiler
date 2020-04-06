package inter;

import lexer.*;
import symbols.*;

public class Op extends Expr {
    public Op(Token tok, Type p) {
        super(tok, p);
    }

    public Expr reduce() {
        Expr x = gen();
        Temp temp = new Temp(type);
        emit(temp.toString() + " = " + x.toString());
        return temp;
    }
}