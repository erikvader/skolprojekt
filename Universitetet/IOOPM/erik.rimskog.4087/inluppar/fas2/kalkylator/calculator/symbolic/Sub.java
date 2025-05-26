package calculator.symbolic;
import java.util.*;

public class Sub extends Binary {

    public Sub(Sexpr left, Sexpr right) {
        super(left,right);
    }

    public String getName() {
        return "-";
    }

    public int priority() {
        return Priorities.PRIO_ADD_SUB;
    }

    public Sexpr eval(HashMap<String,Sexpr> variables) {
        return Symbolic.sub(left.eval(variables), right.eval(variables));
    }

}
