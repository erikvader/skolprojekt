package calculator.symbolic;

public abstract class Atom extends Sexpr {

    public int priority() {
        return Priorities.PRIO_ATOM;
    }

    public String toString() {
        return this.getName();
    }
}
