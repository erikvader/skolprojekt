package exceptions;

/**
 * En {@link Exception} som slängs ifall det genererade trädet påträffar matematiska problem.
 * <p>
 * Ett problem som kan inträffa är att ett tal divideras med 0.
 * 
 * @author ErRi0401
 *
 */
public class MatteException extends Exception {

	private static final long serialVersionUID = 1L;

 	public MatteException() { super(); }
    public MatteException(String message) { super(message); }
    public MatteException(String message, Throwable cause) { super(message, cause); }
    public MatteException(Throwable cause) { super(cause); }
}
