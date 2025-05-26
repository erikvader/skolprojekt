package calculator.symbolic;
import java.util.*;

public class Log extends Unary {

    public Log(Sexpr arg) {
        super(arg);
    }

    public String getName() {
        return "Log";
    }

    public Sexpr eval(HashMap<String,Sexpr> variables) {
        try{
            return Symbolic.log(argument.eval(variables));
        }catch(ArithmeticException ae){
            throw new EvalException(this.toString(), ae.getMessage());
        }
    }
}
