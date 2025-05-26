package calculator.symbolic;
import java.util.*;

public class Assignment extends Binary {

    public Assignment(Sexpr left, Sexpr right) {
        super(left,right);
    }

    public int priority() {
        return Priorities.PRIO_ASS;
    }
    public String getName() {
        return "=";
    }

    public Sexpr eval(HashMap<String,Sexpr> variables) {
        return Symbolic.assignment(left.eval(variables), right, variables);
    }
}
