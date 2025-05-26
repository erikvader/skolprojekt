package calculator.symbolic;

public abstract class Unary extends Sexpr {
    protected final Sexpr argument;

    public Unary(Sexpr argument) {
        this.argument = argument;
     }

    public String toString() {
          return this.getName() + "(" + argument.toString() + ")";
    }

    public int priority() {
        return Priorities.PRIO_UNARY;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Unary){
            Unary other = (Unary)o;
            return super.equals(o) && this.argument.equals(other.argument);
        }
        return false;
    }

}
