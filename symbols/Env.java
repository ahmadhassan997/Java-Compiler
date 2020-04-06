package symbols;

import lexer.*;
import inter.*;
import java.util.*;

public class Env {
    private Hashtable<Token, Id> table;
    protected Env prev;
    public Env(Env prev) { table = new Hashtable<Token, Id> (); this.prev = prev;}
    public void put(Token w, Id i) {table.put(w, i);}
    public Id get(Token w) {
        for (Env e = this; e != null; e = e.prev) {
            Id found = (Id) (e.table.get(w));
            if (found != null) return found; 
        } 
        return null;
    }
}