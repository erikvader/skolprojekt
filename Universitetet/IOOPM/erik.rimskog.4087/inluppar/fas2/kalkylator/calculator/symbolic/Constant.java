package calculator.symbolic;
import java.util.*;

public class Constant extends Atom {
    protected final double value;

    public Constant(double value){
        this.value = value;
    }

    public Sexpr eval(HashMap<String,Sexpr>  variables) {
        return this;
    }

    public String getName() {
        return Double.toString(value);
    }

    public double getValue(){
        return this.value;
    }

    public boolean isConstant(){
        return true;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Constant){
            Constant other = (Constant)o;
            return super.equals(o) && Double.compare(this.value, other.value) == 0;
        }
        return false;
    }


}
