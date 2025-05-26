package calculator.symbolic;
import java.util.*;

public class Variable extends Atom {
    protected final String ident;

    public Variable(String ident){
        this.ident = ident;
    }

    public Sexpr eval(HashMap<String,Sexpr>  variables) {
         return Symbolic.variable(ident, variables);
     }

    public String getName() {
        return ident;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Variable){
            Variable other = (Variable)o;
            return super.equals(o) && this.ident.equals(other.ident);
        }
        return false;
    }

}
