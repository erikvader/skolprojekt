package calculator.symbolic;
import java.util.*;

public class Div extends Binary {

    public Div(Sexpr left, Sexpr right) {
        super(left,right);
    }

    public String getName() {
        return "/";
    }

    public int priority() {
        return Priorities.PRIO_DIV_MULT;
    }

    public Sexpr eval(HashMap<String,Sexpr> variables) {
        try{
            return Symbolic.div(left.eval(variables), right.eval(variables));
        }catch(ArithmeticException ae){
            throw new EvalException(this.toString(), ae.getMessage());
        }
    }

}
