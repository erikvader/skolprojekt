package calculator.symbolic;

import java.util.*;

/**
 * Methods for some mathematical operations
 * returns the matematical value of this operations
 * in a symbolic expression
 * <p>
 * if it is not possible to return a constant the
 * symbolic expression of the matematical
 * operation is returned ( new Add(left, right)
 * <p>
 * Ex.
 *     Cos returns the value in a Symbolc expression
 *     if it is not possible to return a constant an
 *     expresion containg cos will be returned.
 *
 */

public class Symbolic  {

    public static Sexpr sin(Sexpr arg) {
        if (arg.isConstant()) {
            return new Constant(Math.sin(arg.getValue()));
        } else {
            return new Sin(arg);
        }
    }

    public static Sexpr cos(Sexpr arg) {
        if (arg.isConstant()) {
            return new Constant(Math.cos(arg.getValue()));
        } else {
            return new Cos(arg);
        }
    }


    public static Sexpr exp(Sexpr arg) {
        if (arg.isConstant()) {
            return new Constant(Math.exp(arg.getValue()));
        } else {
            return new Exp(arg);
        }
    }

    public static Sexpr log(Sexpr arg) {
        if (arg.isConstant()) {
            double v = Math.log(arg.getValue());
            if(Double.isNaN(v)){
                throw new ArithmeticException("log on a negative number!");
            }
            return new Constant(v);
        } else {
            return new Log(arg);
        }
    }

    public static Sexpr negation(Sexpr arg) {
        if (arg.isConstant()) {
            return new Constant(-(arg.getValue()));
        }
        return new Negation(arg);

    }

    public static Sexpr add(Sexpr left, Sexpr right) {
        if (left.isConstant() && right.isConstant()) {
            return new Constant(left.getValue() + right.getValue());
        } else {
            return new Add(left,right);
        }
    }

    public static Sexpr sub(Sexpr left, Sexpr right) {
        if (left.isConstant() && right.isConstant()) {
            return new Constant(left.getValue() - right.getValue());
        } else {
            return new Sub(left,right);
        }
    }

    public static Sexpr mult(Sexpr left, Sexpr right) {
        if (left.isConstant() && right.isConstant()) {
            return new Constant(left.getValue() * right.getValue());
        } else {
            return new Mult(left,right);
        }
    }

    public static Sexpr div(Sexpr left, Sexpr right) {
        if (left.isConstant() && right.isConstant()) {
            double r = right.getValue();
            if(Double.compare(0, r) == 0){
                throw new ArithmeticException("divide by zero!");
            }
            return new Constant(left.getValue() / r);
        } else {
            return new Div(left,right);
        }
    }

    public static Sexpr assignment(Sexpr left, Sexpr right , HashMap<String,Sexpr> variables) {
        variables.put(right.toString(), left);
        return left;
    }



    public static Sexpr variable(String arg, HashMap<String,Sexpr> variables){
        if (variables.containsKey(arg)) {
            return variables.get(arg);
        } else {
            return new Variable(arg);
        }
    }
}
