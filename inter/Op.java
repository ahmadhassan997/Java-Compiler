package inter;

import lexer.*;
import symbols.*;

public class Op extends Expr {
    public Op(Token tok, Type p) {
        super(tok, p);
    }
}