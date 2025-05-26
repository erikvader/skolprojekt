package calculator.symbolic;

/**
 * This exception is thrown when something went wrong with evaluating
 * an {@link Sexpr}. The message is a String on the form "($1):->"$2"" where
 * $1 is the expression that failed to evaluate, and $2 is what went wrong (eg. division by zero).
 *
 */
public class EvalException extends RuntimeException{

    public static final long serialVersionUID = 1l;

    private EvalException parent;

    public EvalException(String msg){
        super(msg);
    }

    public EvalException(String msg, EvalException parent){
        super(msg);
        this.parent = parent;
    }

    public EvalException(String sexpr, String msg){
        super(String.format("(%s):->\"%s\"", sexpr, msg));
    }

}
