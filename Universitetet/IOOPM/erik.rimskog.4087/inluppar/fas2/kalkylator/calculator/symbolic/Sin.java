package calculator.symbolic;
import java.util.*;

public class Sin extends Unary {

    public Sin(Sexpr arg) {
        super(arg);
    }

    public String getName() {
        return "Sin";
    }

    public Sexpr eval(HashMap<String,Sexpr> variables) {
        return Symbolic.sin(argument.eval(variables));
    }
}
