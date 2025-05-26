package calculator.symbolic;
import java.util.*;

public abstract class Sexpr{

    public Sexpr() {}

    /**
     * @return the display name of this operator.
     *
     */
    public abstract String getName();

    /**
     * Evaluates this expression with the variables in variables.
     *
     * @param variables bindings from variable names to a Sexpr
     * @return the evaluated expression
     */
    public abstract Sexpr eval(HashMap<String,Sexpr> variables);

    /**
     * Returns the value of this expression. Only makes sense if this
     * object is a {@link Constant}. In every other case this method returns 0.
     *
     * @return the value of this expression if it is a Constant
     * @see isConstant
     */
    public double getValue() {
        return 0.0; //TODO: throw exception instead
    }

    /**
     *
     * @return true if this object is a {@link Constant}
     * @see getValue
     */
    public boolean isConstant() {
        return false;
    }

    protected int priority() {
        return 0;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Sexpr){ //onodigt att kolla detta nu
            return this.getClass().equals(o.getClass());
                // && this.getName().equals(((Sexpr)o).getName())
        }
        return false;
    }

    //to remove a warning
    @Override
    public int hashCode(){
        return super.hashCode();
    }

}
