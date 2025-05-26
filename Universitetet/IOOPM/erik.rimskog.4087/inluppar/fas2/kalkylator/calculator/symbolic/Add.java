package calculator.symbolic;
import java.util.*;

public class Add extends Binary {

    public Add(Sexpr left, Sexpr right) {
        super(left,right);
    }

    public int priority() {
        return Priorities.PRIO_ADD_SUB;
    }

    public String getName() {
        return "+";
    }
    public Sexpr eval(HashMap<String,Sexpr> variables) {
        return Symbolic.add(left.eval(variables), right.eval(variables));
    }
}
