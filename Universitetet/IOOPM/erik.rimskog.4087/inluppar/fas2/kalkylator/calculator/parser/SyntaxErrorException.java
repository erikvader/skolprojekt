package calculator.parser;

/**
 * Exception indicating that an error occured when parsing a {@link Sexpr}
 * with {@link Parser}. The message is a description on what
 * went wrong.
 *
 */
public class SyntaxErrorException extends RuntimeException{

    public static final long serialVersionUID = 1l;

    private int column;
   
    public SyntaxErrorException(){
        super();
        column = -1;
    }

    public SyntaxErrorException(String msg){
        this(msg, -1);
    }

    public SyntaxErrorException(String msg, int column){
        super(msg);
        this.column = column;
    }

    public int getColumn(){
        return this.column;
    }
}
