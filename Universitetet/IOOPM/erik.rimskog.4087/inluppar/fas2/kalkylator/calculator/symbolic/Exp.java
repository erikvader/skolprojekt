package calculator.symbolic;
import java.util.*;

public class Exp extends Unary {

    public Exp(Sexpr arg) {
        super(arg);
    }

    public String getName() {
        return "Exp";
    }

    public Sexpr eval(HashMap<String,Sexpr> variables) {
        return Symbolic.exp(argument.eval(variables));
    }
}
