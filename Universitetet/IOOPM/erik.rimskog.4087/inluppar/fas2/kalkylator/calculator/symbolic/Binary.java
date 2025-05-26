package calculator.symbolic;

public abstract class Binary extends Sexpr {
    protected final Sexpr left;
    protected final Sexpr right;

    public Binary(Sexpr left, Sexpr right) {
        this.left = left;
        this.right = right;
      }

    public String toString() {

        String l = left.toString();
        String r = right.toString();

        if (left.priority() < this.priority()) {
            l = "(" + l + ")";
        }

        if (right.priority() < this.priority()) {
            r = "(" + r + ")";
        }

        return l + this.getName() + r;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Binary){
            Binary other = (Binary)o;
            return super.equals(o) && this.left.equals(other.left) && this.right.equals(other.right);
        }
        return false;
    }
}
