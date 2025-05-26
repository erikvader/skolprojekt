package calculator.symbolic;
import java.util.*;

public class Mult extends Binary {

    public Mult(Sexpr left, Sexpr right) {
        super(left,right);
    }

    public String getName() {
        return "*";
    }

    public int priority() {
        return Priorities.PRIO_DIV_MULT;
    }

    public Sexpr eval(HashMap<String,Sexpr> variables) {
        return Symbolic.mult(left.eval(variables), right.eval(variables));
    }
}
