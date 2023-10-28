package exceptions;

/**
 * En {@link Exception} som sl�ngs ifall det genererade tr�det p�tr�ffar matematiska problem.
 * <p>
 * Ett problem som kan intr�ffa �r att ett tal divideras med 0.
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
