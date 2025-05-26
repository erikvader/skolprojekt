package calculator.symbolic;
import java.util.*;

public class Cos extends Unary {

    public Cos(Sexpr arg) {
        super(arg);
    }

    public String getName() {
        return "Cos";
    }

    public Sexpr eval(HashMap<String,Sexpr> variables) {
        return Symbolic.cos(argument.eval(variables));
    }
}
