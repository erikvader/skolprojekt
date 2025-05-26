package calculator.symbolic;
import java.util.*;

public class Negation extends Unary {

    public Negation(Sexpr arg) {
        super(arg);
    }

    public String getName() {
        return "-";
    }

    public  String toString() {
        String a = argument.toString();
        if(argument.priority() < Priorities.PRIO_MAX){
            a = "(" + a +")";
        }
        return this.getName() + a;
        // if (argument.priority() < 4) {
        //     return this.getName() + "(" + argument.toString() + ")";
        // }else{
        //     return this.getName() + argument.toString();
        // }
    }


    public Sexpr eval(HashMap<String,Sexpr> variables) {
        return Symbolic.negation(argument.eval(variables));
    }
}
